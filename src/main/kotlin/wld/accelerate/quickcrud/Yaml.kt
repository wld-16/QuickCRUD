package wld.accelerate.quickcrud

import org.yaml.snakeyaml.Yaml
import wld.accelerate.quickcrud.extension.ENDPOINT
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

fun parseYaml(fileUrl: String): Map<String, Any>? {
    val inputStream: InputStream = FileInputStream(File(fileUrl))

    val yaml = Yaml()
    val data = yaml.load<Map<String, Any>>(inputStream)
    return data
}

fun writePackagePath(packagePath: String): String {
    return "packagePath: " + packagePath
}

fun writeYamlEntities(map: Map<String, Map<String, Any>>): String {
    return "entities:\n  " + map.asSequence()
        .joinToString(separator = "\n  ") { entityEntry -> entityEntry.key + ":\n    " + entityEntry.value.entries.joinToString(separator = "\n    ") { it.key + ": " + it.value } }
}

fun writeYamlEnums(map: Map<String, List<String>>): String {
    return "enums:" + map.asSequence().joinToString("") {enumEntry ->
        "\n  " + enumEntry.key + ": [" + enumEntry.value.joinToString(",") + "]" }
}
fun writeYamlControllersEnum(map: Map<String, Map<ENDPOINT, Boolean>>): String {
    return "\ncontrollers:" + map.asSequence().joinToString { "\n  ${it.key}:\n" +
            "    CREATE: ${it.value[ENDPOINT.CREATE]!!}\n" +
            "    READ: ${it.value[ENDPOINT.READ]!!}\n" +
            "    READ_ALL: ${it.value[ENDPOINT.READ_ALL]!!}\n" +
            "    UPDATE: ${it.value[ENDPOINT.UPDATE]!!}\n" +
            "    DELETE: ${it.value[ENDPOINT.DELETE]!!}" }
}

fun writeYamlControllers(map: Map<String, Map<String, Boolean>>): String {
    return "\ncontrollers:" + map.asSequence().joinToString(separator = "") { "\n  ${it.key}:\n" +
            "    CREATE: ${it.value[ENDPOINT.CREATE.name]!!}\n" +
            "    READ: ${it.value[ENDPOINT.READ.name]!!}\n" +
            "    READ_ALL: ${it.value[ENDPOINT.READ_ALL.name]!!}\n" +
            "    UPDATE: ${it.value[ENDPOINT.UPDATE.name]!!}\n" +
            "    DELETE: ${it.value[ENDPOINT.DELETE.name]!!}" }
}