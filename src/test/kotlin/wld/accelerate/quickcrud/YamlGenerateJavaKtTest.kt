package wld.accelerate.quickcrud

import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@Ignore
class YamlGenerateJavaKtTest() {
    @Test
    fun test_parseYaml() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        assertTrue(file.endsWith("example.yaml"))
    }

    @Test
    fun test_writeJavaEnums() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val linkedHashMap = yamlMap["enums"] as LinkedHashMap<String, List<String>>

        val enumClassRepresentations = writeJavaEnums(linkedHashMap, yamlMap["packagePath"] as String)

        enumClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/java/" + it.key +".java").writeText(it.value)
        }
    }

    @Test
    fun test_writeJavaEnumController() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val linkedHashMap = yamlMap?.get("enums") as LinkedHashMap<String, String>

        val enumControllerContent = writeJavaEnumControllerClass(linkedHashMap.keys.map { it.toString() }, yamlMap["packagePath"] as String + ".java.controller")

        File("src/test/kotlin/wld/accelerate/quickcrud/java/controller/EnumController.java").writeText(enumControllerContent)

    }

    @Test
    fun test_writeJavaControllers() {
        test_writeJavaRepositories()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        writeJavaControllerClasses(listOf(), (yamlMap?.get("packagePath") as String) + ".controller")

        val controllerClassRepresentations = writeJavaControllerClasses((yamlMap["entities"] as Map<String, *>).keys.toList(), (yamlMap["packagePath"] as String) + ".java.controller")

        controllerClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/java/controller/" + it.key + "Controller.java").writeText(it.value)
        }
    }
    @Test
    fun test_writeJavaRepositories() {
        test_writeEntityDataClass_java()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val repositoryClassRepresentation = writeJavaRepositoryDataClass((yamlMap?.get("entities") as Map<String, Map<String, Any>>).keys.toList(), (yamlMap["packagePath"] as String) + ".java.repository")

        repositoryClassRepresentation.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/java/repository/" + it.key + "Repository.java").writeText(it.value)
        }
    }

    @Test
    fun test_writeEntityDataClass_java() {
        test_writeJavaEnums()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeEntityDataClassJava(yamlMap?.get("entities") as Map<String, Map<String, Any>>, yamlMap["packagePath"] as String)

        entityClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/java/entity/" + it.key +".java").writeText(it.value)
        }
    }

    @Test
    fun test_writeModelClassJava() {
        test_writeJavaEnums()

        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val modelClassRepresentations = writeJavaModelDataClass(yamlMap?.get("entities") as Map<String, Map<String, Any>>, yamlMap["packagePath"] as String)

        modelClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/quickcrud/java/model/" + it.key + "Model.java").writeText(it.value)
        }
    }

    @Test
    fun test_writeServiceClass() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val serviceClassRepresentations = writeJavaServiceClass(yamlMap?.get("entities") as Map<String, Map<String, Any>>, yamlMap["packagePath"] as String)

        serviceClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/java/service/" + it.key + "Service.java").writeText(it.value)
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
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/java/service/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/sql/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/components/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/quickcrud/vue/plugins/"))
        }
    }
}