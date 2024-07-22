package wld.accelerate.quickcrud

import wld.accelerate.quickcrud.extension.snakeCase
import wld.accelerate.quickcrud.extension.unCapitalize
import java.util.regex.Pattern

fun writeDDLRelationships(entities: Map<String, Map<String, Any>>): Map<String, String> {
    // If 1-n
    val oneToNTemplate: (String, String) -> String = { entityName: String, fieldName: String ->
        val fieldNameWithoutList = fieldName.split("List")[0]
        val uncapitalizedEntityName = String.unCapitalize(entityName)
        "\nalter table $uncapitalizedEntityName add $fieldNameWithoutList integer;\n" +
                "alter table $uncapitalizedEntityName add constraint ${uncapitalizedEntityName}_${fieldNameWithoutList}_id_fk \n\tforeign key ($fieldNameWithoutList) references $fieldNameWithoutList;"}

    // If n-m
    val nToMTemplate: (String, String) -> String = { entityName: String, fieldName: String ->
        val fieldNameWithoutList = fieldName.split("List")[0]
        val uncapitalizedEntityName = String.unCapitalize(entityName)
        """
        create table ${uncapitalizedEntityName}_$fieldNameWithoutList
        (
            id       serial,
            $uncapitalizedEntityName integer
                constraint ${uncapitalizedEntityName}_${fieldNameWithoutList}_${uncapitalizedEntityName}_id_fk
                    references $uncapitalizedEntityName,
            $fieldNameWithoutList  integer
                constraint ${uncapitalizedEntityName}_${fieldNameWithoutList}_${fieldNameWithoutList}_id_fk
                    references $fieldNameWithoutList
        );
    """.trimIndent()}

    return entities.entries.associate {
        it.key to it.value.entries.joinToString("") { entry ->
            if (entry.value.toString().contains("1n")) oneToNTemplate(it.key, entry.key)
            else if(entry.value.toString().contains("nm")) nToMTemplate(it.key, entry.key)
            else "" }
    }.filter { it.value.isNotEmpty() }
}

fun writeDDLForeignKeys(entities: Map<String, Map<String, Any>>) : String {
    val sqlForeignKeyTemplate: (String, String) -> String = { entityName, fieldName ->
        "" +
                "ALTER TABLE ${entityName.lowercase()} ADD CONSTRAINT ${entityName}_${fieldName.lowercase()}__fk " +
                "FOREIGN KEY (${fieldName.lowercase()}_id) REFERENCES ${fieldName.lowercase()};"
    }
    return entities.entries.joinToString("\n") { entity ->
        entity.value.entries.filter { it.value as String == "Object" }
            .joinToString(separator = "\n") { sqlForeignKeyTemplate(entity.key, it.key) }
    }
}

fun writeDDL(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val sqlTableHead: (String) -> String = { "CREATE TABLE ${String.snakeCase(it)} (\n" }
    val sqlTableFieldTemplate: (String, String) -> String = { fieldName, fieldType ->
        "\t$fieldName " +
                if (fieldType == "Integer") {
                    "INTEGER,"
                } else if (fieldType == "Enum") {
                    "VARCHAR,"
                } else if (fieldType == "Object"){
                    "INTEGER,"
                } else {
                    "VARCHAR,"
                }
    }
    val sqlTableFoot = "\n);"



    val sqlTableField = entities.entries.associate { entity ->
        val fields = (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
            sqlTableFieldTemplate(
                if(it.value == "Object") {
                    it.key.split(Pattern.compile("(?=[A-Z])")).joinToString(separator = "_") {
                            literal -> literal.lowercase() + "_id" }
                } else {
                    it.key.split(Pattern.compile("(?=[A-Z])")).joinToString(separator = "_") {
                            literal -> literal.lowercase() }
                }, it.value as String
            )
        }
        val returnValue = sqlTableHead(entity.key) +
                "\tID SERIAL CONSTRAINT ${String.snakeCase(entity.key).uppercase()}_PK PRIMARY KEY, \n" +
                fields.substring(0, fields.length - 1).uppercase() +
                sqlTableFoot +
                "\nALTER TABLE ${String.snakeCase(entity.key)} ALTER COLUMN id SET DEFAULT nextval('${entity.key.split(
                    Pattern.compile("(?=[A-Z])")).joinToString(separator = "_") {
                        literal -> literal.lowercase() }}_id_seq'::regclass);\n" +
                "\n"
        entity.key to returnValue
    }
    return sqlTableField
}

fun writeShellScript(entities: Map<String, Map<String, Any>>): String {
    val executeEntitySQLTemplate: (String) -> String = { "psql --user postgres sample -a -f /docker-entrypoint-initdb.d/sql/$it.sql" }
    val executeEntityRelationshipSQLTemplate: (String) -> String = { "psql --user postgres sample -a -f /docker-entrypoint-initdb.d/sql/${it}_relationship.sql" }
    val entitiesWithRelationships: Set<String> = entities.filter { entity -> entity.value.values.map { it.toString() }.contains("List-nm") }.keys

    return entities.keys.joinToString(separator = "\n") { executeEntitySQLTemplate(it) } +
            "\npsql --user postgres sample -a -f /docker-entrypoint-initdb.d/sql/ForeignKeys.sql\n" +
            entitiesWithRelationships.joinToString(separator = "\n") { executeEntityRelationshipSQLTemplate(it) }
}