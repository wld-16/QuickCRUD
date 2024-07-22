package wld.accelerate.quickcrud

import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class YamlGenerateKotlinTest() {
    @Test
    fun test_parseYaml() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        assertTrue(file.endsWith("example.yaml"))
    }

    @Test
    fun test_writeKotlinEnums() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val linkedHashMap = yamlMap?.get("enums") as LinkedHashMap<String, String>

        val enumClassRepresentations = writeKotlinEnums(linkedHashMap, yamlMap["packagePath"] as String)

        enumClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/kotlin/" + it.key + ".kt").writeText(it.value)
        }
    }

    /*
    @Test
    fun test_writeKotlinControllers() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        writeKotlinControllerClasses(listOf(), (yamlMap?.get("packagePath") as String) + ".controller")

        val entityClassRepresentations = writeKotlinRepositoryDataClass((yamlMap?.get("entities") as Map<String, Map<String, Any>>).keys.toList(), (yamlMap["packagePath"] as String) + ".kotlin.controller")

        //entityClassRepresentations.forEach{
        //    File("src/test/kotlin/wld/accelerate/quickcrud/kotlin/repository/" + it.key + "Repository.kt").writeText(it.value)
        //}
    }
    */


    @Test
    fun test_writeRepositories_kotlin() {
        test_writeEntityDataClass()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeKotlinRepositoryDataClass(
            (yamlMap?.get("entities") as Map<String, Map<String, Any>>).keys.toList(),
            (yamlMap["packagePath"] as String) + ".repository"
        )

        entityClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/kotlin/repository/" + it.key + "Repository.kt").writeText(it.value)
        }
    }

    @Test
    fun test_writeEntityDataClass() {
        test_writeKotlinEnums()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeEntityDataClass(
            yamlMap?.get("entities") as Map<String, Map<String, Any>>,
            yamlMap["packagePath"] as String
        )

        entityClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/kotlin/entity/" + it.key + ".kt").writeText(it.value)
        }
    }


    @Test
    fun test_writeModelClass() {
        test_writeKotlinEnums()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val modelClassRepresentations = writeModelDataClass(
            yamlMap?.get("entities") as Map<String, Map<String, Any>>,
            yamlMap["packagePath"] as String
        )

        modelClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/kotlin/model/" + it.key + "Model.kt").writeText(it.value)
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun test_createDirectories(): Unit {
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/kotlin/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/kotlin/entity/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/kotlin/controller/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/kotlin/model/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/kotlin/repository/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/entity/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/controller/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/model/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/repository/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/components/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/plugins/"))
        }
    }
}