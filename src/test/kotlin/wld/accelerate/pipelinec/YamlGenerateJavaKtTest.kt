package wld.accelerate.pipelinec

import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

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

        val linkedHashMap = yamlMap?.get("enums") as LinkedHashMap<String, String>

        val enumClassRepresentations = writeJavaEnums(linkedHashMap, yamlMap["packagePath"] as String)

        enumClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/pipelinec/java/" + it.key +".java").writeText(it.value)
        }
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
            File("src/test/kotlin/wld/accelerate/pipelinec/java/controller/" + it.key + "Controller.java").writeText(it.value)
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
            File("src/test/kotlin/wld/accelerate/pipelinec/java/repository/" + it.key + "Repository.java").writeText(it.value)
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
            File("src/test/kotlin/wld/accelerate/pipelinec/java/entity/" + it.key +".java").writeText(it.value)
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
            File("src/test/kotlin/wld/accelerate/pipelinec/java/model/" + it.key + "Model.java").writeText(it.value)
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
            File("src/test/kotlin/wld/accelerate/pipelinec/java/service/" + it.key + "Service.java").writeText(it.value)
        }
    }

    @Test
    fun test_writeSQLDDL() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)
        val entityClassRepresentations = writeDDL(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

        Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/sql/"))

        entityClassRepresentations.forEach{
            File("src/test/kotlin/wld/accelerate/pipelinec/sql/" + it.key +".sql").writeText(it.value)
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun test_createDirectories(): Unit {
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/kotlin/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/kotlin/entity/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/kotlin/controller/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/kotlin/model/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/kotlin/repository/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/java/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/java/entity/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/java/controller/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/java/model/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/java/repository/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/sql/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/vue/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/vue/components/"))
            Files.createDirectories(Path.of("src/test/kotlin/wld/accelerate/pipelinec/vue/plugins/"))
        }
    }
}