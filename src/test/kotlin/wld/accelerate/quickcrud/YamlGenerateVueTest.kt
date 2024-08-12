package wld.accelerate.quickcrud

import junit.framework.TestCase.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class YamlGenerateVueTest() {
    @Test
    fun test_parseYaml() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        assertTrue(file.endsWith("example.yaml"))
    }

    @Test
    fun test_writeVueDetailsComponent() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeVueDetailsComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

        entityClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/vue/components/" + it.key + "Details" +".vue").writeText(it.value)
        }
    }

    @Test
    fun test_writeVueListComponent() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeVueListComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

        entityClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/vue/components/" + it.key + "List" +".vue").writeText(it.value)
        }
    }


    @Test
    fun test_writeNavigationComponent() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val navigationComponent = writeVueAppNavigation((yamlMap?.get("entities") as Map<String, *>).keys.toList())

        File("src/test/kotlin/wld/accelerate/quickcrud/vue/components/RouteNavigationComponent.vue").writeText(navigationComponent)
    }
    @Test
    fun test_writeVueLandingPage() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val landingPage = writeVueLandingPageComponentTemplate((yamlMap?.get("entities") as Map<String, *>).keys.toList())

        File("src/test/kotlin/wld/accelerate/quickcrud/vue/components/LandingPage.vue").writeText(landingPage)
    }

    @Test
    fun test_writeVueRouter() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val routerPlugin = writeVueRouterJs(yamlMap["entities"] as Map<String, Map<String, Any>>)

        File("src/test/kotlin/wld/accelerate/quickcrud/vue/plugins/router.js").writeText(routerPlugin)
    }

    @Test
    fun test_writeVueCreateForm() {
        val resourceName = "sample/example.yaml"

        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(resourceName)!!.file)

        val yamlMap = parseYaml(file.absolutePath)

        val entityClassRepresentations = writeVueCreateForm(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

        entityClassRepresentations.forEach {
            File("src/test/kotlin/wld/accelerate/quickcrud/vue/components/" + it.key + "CreateForm" +".vue").writeText(it.value)
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