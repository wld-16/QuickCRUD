package wld.accelerate.pipelinec

import wld.accelerate.pipelinec.extension.capitalize
import wld.accelerate.pipelinec.extension.snakeCase
import wld.accelerate.pipelinec.extension.unCapitalize
import java.util.regex.Pattern

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

    val sqlForeignKeyTemplate: (String, String) -> String = { entityName, fieldName ->
        "" +
                "ALTER TABLE ${entityName.lowercase()} ADD CONSTRAINT ${entityName}_${fieldName.lowercase()}__fk " +
                "FOREIGN KEY (${fieldName.lowercase()}_id) REFERENCES ${fieldName.lowercase()};"
    }

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
                "\nALTER TABLE ${String.snakeCase(entity.key)} ALTER COLUMN id SET DEFAULT nextval('${entity.key.split(Pattern.compile("(?=[A-Z])")).joinToString(separator = "_") {
                        literal -> literal.lowercase() }}_id_seq'::regclass);\n" +
                "\n" +
                entity.value.entries.filter { it.value as String == "Object" }.joinToString(separator = ",") { sqlForeignKeyTemplate(entity.key, it.key) }
        entity.key to returnValue
    }
    return sqlTableField
}

fun writeJavaEnums(enums: LinkedHashMap<String, String>, packagePath: String): Map<String, String> {
    val head: (String) -> String = { "package $packagePath.java;\n\npublic enum $it {" }
    val entry: (String) -> String = { "\n\t$it," }
    val foot: String = "\n}"

    val enumsFileMap = enums.entries.associate {
        it.key to
                head(it.key) + (it.value.filterNot { it == '[' || it == ']' || it == ' ' }
                    .split(",")
                    .map { entry(it) }
            .joinToString(separator = "")) + foot
    }

    return enumsFileMap
}

fun writeKotlinEnums(enums: LinkedHashMap<String, String>, packagePath: String): Map<String, String> {
    val head: (String) -> String = { "package $packagePath.kotlin\n\nenum class $it {" }
    val entry: (String) -> String = { "\n\t$it," }
    val foot: String = "\n}"

    val enumsFileMap = enums.entries.associate {
        it.key to
                head(it.key) + (it.value.filterNot { it == '[' || it == ']' || it == ' ' }.split(",").map { entry(it) }
            .joinToString(separator = "")) + foot
    }

    return enumsFileMap
}

fun writeJavaControllerClasses(controllerEntities: List<String>, packagePath: String): Map<String, String> {
    val packageStatement: String = "package $packagePath;\n"

    val importStatements: (String) -> String = {
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.annotation.*;;\n" +
                "import wld.accelerate.pipelinec.java.entity.${it};\n" +
                "import wld.accelerate.pipelinec.java.model.${it}Model;\n" +
                "import wld.accelerate.pipelinec.java.service.${it}Service;\n" +
                "import java.util.List;"
    }

    val classStatement: (String) -> String = {
        "\n@RestController\n" +
                "public class ${it}Controller {"
    }

    val classClose: String = "}"

    val autoWiredComponentsStatement: (String) -> String = {
        "\n\t@Autowired\n" +
                "\tprivate ${it}Service ${String.unCapitalize(it)}Service;\n"
    }

    val getByIdEndpointTemplate: (String) -> String = {
        "\t@GetMapping(\"/${String.unCapitalize(it)}/{id}\")\n" +
                "\tpublic ResponseEntity<${it}Model> get${it}(@PathVariable Integer id) {\n" +
                "\t\treturn ResponseEntity.ok(${it}Model.from${it}(${String.unCapitalize(it)}Service.findById(id)));\n" +
                "\t}"
    }

    val getAllEndpointTemplate: (String) -> String = {
        "\t@GetMapping(\"/${String.unCapitalize(it)}/\")\n" +
                "\tpublic ResponseEntity<List<${it}Model>> getAll${it}() {\n" +
                "\t\treturn ResponseEntity.ok(${String.unCapitalize(it)}Service.findAll().stream().map(${it}Model::from${it}).toList());\n" +
                "\t}"
    }

    val saveEndpointTemplate: (String) -> String = {
        "\t@PostMapping(\"/${String.unCapitalize(it)}/\")\n" +
                "\tpublic ResponseEntity<${it}Model> save${it}(@RequestBody ${it}Model ${String.unCapitalize(it)}Model) {\n" +
                "\t\t${it} ${String.unCapitalize(it)} = ${String.unCapitalize(it)}Service.create${it}(${String.unCapitalize(it)}Model);\n" +
                "\t\treturn ResponseEntity.ok(${it}Model.from${it}(${String.unCapitalize(it)}));\n" +
                "\t}"
    }

    val updateEndpointTemplate: (String) -> String = {
        "\t@PostMapping(\"/${String.unCapitalize(it)}/{id}\")\n" +
                "\tpublic ResponseEntity<${it}Model> update${it}(@PathVariable Integer id, ${it}Model ${String.unCapitalize(it)}Model) {\n" +
                "\t\t${it} ${String.unCapitalize(it)} = ${String.unCapitalize(it)}Service.update${it}(id, ${String.unCapitalize(it)}Model);\n" +
                "\t\treturn ResponseEntity.ok(${it}Model.from${it}(${String.unCapitalize(it)}));\n" +
                "\t}"
    }

    return controllerEntities.associateWith {
                packageStatement + "\n" +
                importStatements(it) + "\n" +
                classStatement(it) + "\n" +
                autoWiredComponentsStatement(it) + "\n" +
                getByIdEndpointTemplate(it) + "\n" +
                getAllEndpointTemplate(it) + "\n" +
                saveEndpointTemplate(it) + "\n" +
                updateEndpointTemplate(it) + "\n" +
                classClose
    }
}

fun writeKotlinControllerClasses(controllerEntities: List<String>, packagePath: String): Map<String, String> {
    val importPath: (String) -> String = {
        "import org.springframework.data.jpa.repository.JpaRepository\n" +
                "import org.springframework.stereotype.Repository\n" +
                "import wld.accelerate.pipelinec.$it"
    }

    val interfaceString: (String) -> String = {
        "\n@Repository\n" +
                "interface ${it}Repository: JpaRepository<${it}, Int> {\n" +
                "}"
    }
    return controllerEntities.associateWith {
        "package " + packagePath + "\n\n" + importPath(it) + "\n\n" + interfaceString(
            it
        )
    }
}

fun writeJavaRepositoryDataClass(entities: List<String>, packagePath: String): Map<String, String> {
    val importPath: (String) -> String = {
        "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "import wld.accelerate.pipelinec.java.entity.$it;"
    }

    val interfaceString: (String) -> String = {
        "\n@Repository\n" +
                "public interface ${it}Repository extends JpaRepository<${it}, Integer> {\n" +
                "}"
    }
    return entities.associateWith { "package " + packagePath + ";" + "\n\n" + importPath(it) + "\n\n" + interfaceString(it) }
}

fun writeKotlinRepositoryDataClass(entities: List<String>, packagePath: String): Map<String, String> {
    val importPath: (String) -> String = {
        "import org.springframework.data.jpa.repository.JpaRepository\n" +
                "import org.springframework.stereotype.Repository\n" +
                "import wld.accelerate.pipelinec.kotlin.entity.$it"
    }

    val interfaceString: (String) -> String = {
        "\n@Repository\n" +
                "interface ${it}Repository: JpaRepository<${it}, Int> {\n" +
                "}"
    }
    return entities.associateWith { "package " + packagePath + "\n\n" + importPath(it) + "\n\n" + interfaceString(it) }
}

fun writeJavaModelDataClass(entityToField: Map<String, Map<String, Any>>, packagePath: String): Map<String, String> {
    val enumImportTemplate: (String) -> String =
        { if (it == "String" || it == "Integer" || it == "Object") "" else "\nimport $packagePath.java.$it;" }
    val defaultIdTemplate: String = "\tprivate Long id;\n"

    val importStatements: (String) -> (String) = { "\nimport $packagePath.java.entity.$it;" }

    val fieldTemplate: (String, String) -> String = { fieldName: String, fieldType: String ->
        "\tprivate ${if(fieldType == "Object") String.capitalize(fieldName)+"Model" else fieldType} $fieldName;"
    }

    val getMethodTemplate: (String, String) -> String = { fieldName, fieldType ->
        "\n\tpublic ${if(fieldType == "Object")String.capitalize(fieldName)+"Model" else fieldType} get${String.capitalize(fieldName)}(){\n\t\treturn $fieldName;\n\t}\n"
    }

    val setMethodTemplate: (String, String) -> String = { fieldName, fieldType ->
        "\n\tpublic void set${String.capitalize(fieldName)}(${if(fieldType == "Object") String.capitalize(fieldName) + "Model" else fieldType} $fieldName) {" +
                "\n\t\tthis.$fieldName = $fieldName;\n\t}\n"
    }

    val getIdMethodTemplate: String =
        "public Long getId() {\n" +
            "\t\treturn id;\n" +
            "\t}"

    val setIdMethodTemplate: String =
        "public void setId(Long id) {\n" +
                "\t\tthis.id = id;\n" +
                "\t}"

    val fromTemplate: (String, Map<Pair<String, Any>, String>) -> String = { it, map ->
        "\tpublic static ${it}Model from$it($it ${String.unCapitalize(it)}) {\n" +
                "\t\t${it}Model ${String.unCapitalize(it)}Model = new ${it}Model();\n" +
                "\t\t${String.unCapitalize(it)}Model.id = ${String.unCapitalize(it)}.getId();\n" +
                map.entries.joinToString(separator = "") { entry ->
                    "\t\t${String.unCapitalize(it)}Model." + entry.value + " = " +
                            (if(entry.key.second == "Object") String.capitalize(entry.value) + "Model.from" + String.capitalize(entry.value) + "(" else "") +
                            "${String.unCapitalize(entry.key.first)}.get${String.capitalize(entry.value)}()" + (if(entry.key.second == "Object") ")" else "") +  ";\n" } +
                "\t\treturn ${String.unCapitalize(it)}Model;\n" +
                "\t}\n"
    }

    val toModelTemplate: (String, Map<Pair<String, Any>, String>) -> String = { it, map ->
        "\tpublic static $it to$it(${it}Model ${String.unCapitalize(it)}Model){\n" +
                "\t\t$it ${String.unCapitalize(it)} = new $it();\n" +
                "\t\t${String.unCapitalize(it)}.setId(${String.unCapitalize(it)}Model.getId());\n" +
                map.entries.joinToString(separator = "") {
                    entry -> "\t\t${String.unCapitalize(it)}.set${String.capitalize(entry.value)}(" +
                        (if(entry.key.second == "Object") String.capitalize(entry.value) + "Model.to" + String.capitalize(entry.value) + "(" else "") +
                        "${"${String.unCapitalize(it)}Model.get${String.capitalize(entry.value)}()" }" +
                        (if(entry.key.second == "Object") ")" else "") +
                        ");\n" } +
                "\t\treturn ${String.unCapitalize(it)};\n" +
                "\t}"

    }

    val dataPackageHead: (String) -> String = { "package $it.java.model;\n" }

    val dataClassHead: (String) -> String = { "\n\npublic class ${it}Model {\n" }
    val dataClassFoot = "\n}"

    val classesAsStrings = entityToField
        .entries.associate { entityToFieldEntry ->
            val returnValue = dataPackageHead(packagePath) +
                    importStatements(entityToFieldEntry.key) +
                    (entityToFieldEntry.value as Map<String, *>).entries.joinToString(separator = "") {
                        enumImportTemplate(it.value as String)
                    } +
                    dataClassHead(entityToFieldEntry.key) +
                    defaultIdTemplate +
                    (entityToFieldEntry.value as Map<String, *>).entries.joinToString(separator = "\n") {
                        fieldTemplate(it.key, it.value as String)
                    } + "\n" +
                    fromTemplate(
                        entityToFieldEntry.key,
                        entityToFieldEntry.value.entries.associate { fieldEntry -> Pair(entityToFieldEntry.key, fieldEntry.value) to fieldEntry.key}) +
                    toModelTemplate(
                        entityToFieldEntry.key, entityToFieldEntry.value.entries.associate { entry -> Pair(entityToFieldEntry.key, entry.value) to entry.key}) + "\n" +
                    (entityToFieldEntry.value as Map<String, *>).entries.joinToString(separator = "") {
                        getMethodTemplate(it.key, it.value as String)
                    } +
                    (entityToFieldEntry.value as Map<String, *>).entries.joinToString(separator = "") {
                        setMethodTemplate(it.key, it.value as String)
                    } +
                    "\n" + getIdMethodTemplate +
                    "\n" + setIdMethodTemplate +
                    dataClassFoot
            entityToFieldEntry.key to returnValue
        }

    return classesAsStrings
}

fun writeModelDataClass(models: Map<String, Map<String, Any>>, packagePath: String): Map<String, String> {
    val enumImportTemplate: (String) -> String =
        { if (it == "String" || it == "Integer") "" else "\nimport $packagePath.kotlin.$it" }

    val fieldTemplate: (String, String) -> String = { fieldName: String, fieldType: String ->
        var returningFieldType = fieldType
        if (fieldType == "Integer") {
            returningFieldType = "Int"
        } else if(fieldType == "Object") {
            returningFieldType == fieldName + "Model"
        }
        "\tval $fieldName: $returningFieldType,"
    }

    val dataPackageHead: (String) -> String = { "package $it.kotlin.model\n" }

    val dataClassHead: (String) -> String = { "\n\ndata class $it (\n" }
    val dataClassFoot = "\n)"

    val classesAsStrings = models
        .entries.associate { entity ->
            val returnValue = dataPackageHead(packagePath) +
                    (entity.value as Map<String, *>).entries.joinToString(separator = "") {
                        enumImportTemplate(it.value as String)
                    } +
                    dataClassHead(entity.key) +
                    (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
                        fieldTemplate(it.key, it.value as String)
                    } +
                    dataClassFoot
            entity.key to returnValue
        }

    return classesAsStrings
}

fun writeJavaServiceClass(entities: Map<String, Map<String, Any>>, packagePath: String): Map<String, String> {
    val importStatements: (String) -> String = {
        "import org.springframework.beans.factory.annotation.Autowired;\n" +
        "import org.springframework.stereotype.Service;\n" +
        "import wld.accelerate.pipelinec.java.entity.$it;\n" +
        "import wld.accelerate.pipelinec.java.model.${it}Model;\n" +
        "import wld.accelerate.pipelinec.java.repository.${it}Repository;\n" +
        "import java.util.List;"
    }

    val classDeclaration: (String) -> String = {
        "@Service\npublic class ${it}Service {"
    }
    val closeClass = "}"

    val autowiredComponents: (String) -> String = {
        "\t@Autowired\n" +
        "\tpublic ${String.capitalize(it)}Repository ${String.unCapitalize(it)}Repository;\n" +
        "\n"
    }

    val packageStatement = "package $packagePath.java.service;\n"

    val findByIdMethodTemplate: (String) -> String = {
        "\tpublic $it findById(Integer id) {\n" +
                "\t\treturn ${String.unCapitalize(it)}Repository.findById(id).orElseThrow();\n" +
                "\t}"
    }

    val findAllTemplate: (String) -> String = {
        "\tpublic List<$it> findAll() {\n" +
                "\t\treturn ${String.unCapitalize(it)}Repository.findAll();\n" +
                "\t}"
    }

    val createTemplate: (String) -> String = {
        "\tpublic $it create$it(${it}Model ${String.unCapitalize(it)}Model){\n" +
                "\t\t${it} ${String.unCapitalize(it)} = ${it}Model.to$it(${String.unCapitalize(it)}Model);\n" +
                "\t\treturn ${String.unCapitalize(it)}Repository.save(${String.unCapitalize(it)});\n" +
                "\t}"
    }

    val updateTemplate: (String, Map<String, String>) -> String = { it, map ->
        "\tpublic $it update$it(Integer id, ${it}Model ${String.unCapitalize(it)}Model){\n" +
                "\t\t$it ${String.unCapitalize(it)} = findById(id);\n" +
                map.entries.joinToString(separator = "") {
                        entry -> "\t\t${String.unCapitalize(it)}.set${String.capitalize(entry.key)}(${entry.value});\n" } +
                "\t\treturn ${String.unCapitalize(it)}Repository.save(${String.unCapitalize(it)});\n" +
                "\t}"
    }

    val classesAsStrings = entities
        .entries.associate { entity ->
            val returnValue =
                packageStatement + "\n" +
                        importStatements(entity.key) + "\n" +
                        classDeclaration(entity.key) + "\n" +
                        autowiredComponents(entity.key) + "\n" +
                        findByIdMethodTemplate(entity.key) + "\n" +
                        findAllTemplate(entity.key) + "\n" +
                        createTemplate(entity.key) + "\n" +
                        updateTemplate(entity.key, entity.value.entries.associate { entry -> entry.key to "${String.unCapitalize(entity.key)}.get${String.capitalize(entry.key)}()" }) + "\n" +
                        closeClass

            entity.key to returnValue
    }

    return classesAsStrings
}

// Function to generate a Java Entity Class
fun writeEntityDataClassJava(entities: Map<String, Map<String, Any>>, packagePath: String): Map<String, String> {
    val enumImportTemplate: (String) -> String =
        { if (it == "String" || it == "Integer" || it == "Object") "" else "\nimport $packagePath.java.$it;" }

    val defaultIdTemplate: (String) -> String = { "\n\t@Id\n" +
            "\t@SequenceGenerator(name=\"${String.unCapitalize(it)}_id_seq\", sequenceName=\"${String.unCapitalize(it)}_id_seq\", allocationSize=1)\n" +
            "\t@GeneratedValue(strategy = GenerationType.SEQUENCE, generator=\"${String.unCapitalize(it)}_id_seq\")\n" +
            "\tprivate Long id = null;\n"
    }

    val getIdMethodTemplate: String =
        "\tpublic Long getId() {\n" +
                "\t\treturn id;\n" +
                "\t}"

    val setIdMethodTemplate: String =
        "\tpublic void setId(Long id) {\n" +
                "\t\tthis.id = id;\n" +
                "\t}"


    val fieldTemplate: (String, String) -> String = { fieldName: String, fieldType: String ->
        (if(fieldType == "Object") "\t@ManyToOne\n" else "\t@Column(nullable = false)\n\t") +
                "${if(fieldType == "Object") String.capitalize(fieldName) else fieldType} $fieldName = " +
                when (fieldType) {
                    "String" -> "\"\";\n"
                    "Enum" -> "[];\n"
                    "Object" -> "null;\n"
                    else -> "null;\n"
                }
    }

    val getMethodTemplate: (String, String) -> String = { fieldName, fieldType ->
        "\n\tpublic ${if(fieldType == "Object") String.capitalize(fieldName) else fieldType} get${String.capitalize(fieldName)}(){\n\t\treturn $fieldName;\n\t}\n"
    }

    val setMethodTemplate: (String, String) -> String = { fieldName, fieldType ->
        "\n\tpublic void set${String.capitalize(fieldName)}(${if(fieldType == "Object") String.capitalize(fieldName) else fieldType} $fieldName) {" +
                "\n\t\tthis.$fieldName = $fieldName;\n\t}\n"
    }

    val dataImportHead = "" +
            "import java.io.Serializable;\n" +
            "import jakarta.persistence.*;"

    val dataPackageHead: (String) -> String = { "package $it.java.entity;\n\n" }

    val dataClassHead: (String) -> String = { "\n@Entity\n" +
            "public class $it implements Serializable {" }
    val dataClassFoot = "\n}"

    val classesAsStrings = entities
        .entries.associate { entity ->
            val returnValue =
                dataPackageHead(packagePath) + (entity.value as Map<String, *>).entries.joinToString(separator = "") {
                    enumImportTemplate(it.value as String)
                } + "\n" + dataImportHead + dataClassHead(entity.key) + defaultIdTemplate(entity.key) +
                        (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
                            fieldTemplate(it.key, it.value as String)
                        } +
                        (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
                            getMethodTemplate(it.key, (it.value as String))
                        } +
                        (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
                            setMethodTemplate(it.key, it.value as String)
                        } +
                        "\n" + getIdMethodTemplate +
                        "\n" + setIdMethodTemplate +
                        dataClassFoot

            entity.key to returnValue
        }

    return classesAsStrings
}

fun writeEntityDataClass(entities: Map<String, Map<String, Any>>, packagePath: String): Map<String, String> {
    val enumImportTemplate: (String) -> String =
        { if (it == "String" || it == "Integer" || it == "Object") "" else "\nimport $packagePath.kotlin.$it" }

    val defaultIdTemplate: (String) -> String = {
        "\t@Id\n" +
        "\t@SequenceGenerator(name=\"${it}_id_seq\", sequenceName=\"${it}_id_seq\", allocationSize=1)\n" +
        "\t@GeneratedValue(strategy = GenerationType.SEQUENCE, generator=\"${it}_id_seq\")\n" +
        "\tprivate val id: Long? = null\n" }

    val fieldTemplate: (String, String) -> String = { fieldName: String, fieldType: String ->
        "\n\t@Column(nullable = false)\n\t" +
                "var $fieldName: $fieldType? = " +
                when (fieldType) {
                    "String" -> "\"\""
                    "Enum" -> "[]"
                    else -> "null"
                }
    }

    val dataImportHead = "" +
            "import java.io.Serializable\n\n" +
            "import jakarta.persistence.*\n"

    val dataPackageHead: (String) -> String = { "package $it.kotlin.entity\n\n" }

    val dataClassHead: (String) -> String = { "\n@Entity\nclass $it : Serializable {\n" }
    val dataClassFoot = "\n}"

    val classesAsStrings = entities
        .entries.associate { entity ->
            val returnValue =
                dataPackageHead(packagePath) + (entity.value as Map<String, *>).entries.joinToString(separator = "") {
                    enumImportTemplate(it.value as String)
                } + "\n" + dataImportHead + dataClassHead(entity.key) + defaultIdTemplate(entity.key) +
                        (entity.value as Map<String, *>).entries.joinToString(separator = "\n") {
                            fieldTemplate(it.key, it.value as String)
                        } +
                        dataClassFoot

            entity.key to returnValue
        }

    return classesAsStrings
}