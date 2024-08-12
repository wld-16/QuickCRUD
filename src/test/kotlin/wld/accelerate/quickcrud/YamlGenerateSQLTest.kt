package wld.accelerate.quickcrud

import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class YamlGenerateSQLTest {
    @Test
    fun test_writeSQLDDL_without_relationship() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)
        val entities = yamlMap?.get("entities") as Map<String, Map<String, Any>>

        val entitiesWithoutRelationship = entities.entries.associate {
            it.key to it.value.filter { entry ->
                !entry.value.toString().contains("List")
            }
        }
        val entityClassRepresentations = writeDDL(entitiesWithoutRelationship)

        Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))

        entityClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/sql/" + it.key +".sql").writeText(it.value)
        }
    }

    @Test
    fun test_writeSQLForeignKeys() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))

        File("src/test/kotlin/wld/accelerate/quickcrud/sql/ForeignKeys.sql").writeText(
            writeDDLForeignKeys(
                yamlMap?.get(
                    "entities"
                ) as Map<String, Map<String, Any>>
            )
        )
    }

    @Test
    fun test_writeSQLDDL_relationships() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)
        val entityClassRepresentations = writeDDLRelationships(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

        Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))

        entityClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/sql/" + it.key +"_relationship.sql").writeText(it.value)
        }
    }

    @Test
    fun test_write_shellScript() {
        val resourceName = "sample/example.yaml"
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)
        val yamlMap = parseYaml(file.absolutePath)
        val entities = yamlMap["entities"] as Map<String, Map<String, Any>>
        File("src/test/kotlin/wld/accelerate/quickcrud/sh/executeSQL.sh").writeText(writeShellScript(entities))
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun test_createDirectories(): Unit {
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sh/"))
        }
    }
}