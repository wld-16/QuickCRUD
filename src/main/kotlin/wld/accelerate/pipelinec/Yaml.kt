package wld.accelerate.pipelinec

import org.yaml.snakeyaml.Yaml
import wld.accelerate.pipelinec.extension.ENDPOINT
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

fun parseYaml(fileUrl: String): Map<String, Any>? {
    val inputStream: InputStream = FileInputStream(File(fileUrl))

    val yaml = Yaml()
    val data = yaml.load<Map<String, Any>>(inputStream)
    println(data)
    return data
}

fun writeYamlEntities(map: Map<String, Map<String, Any>>): String {
    return "entities:\n  " + map.asSequence()
        .joinToString(separator = "\n  ") { entityEntry -> entityEntry.key + ":\n    " + entityEntry.value.entries.joinToString(separator = "\n    ") { it.key + ": " + it.value } }
}

// WIP
fun writeYamlControllers(entityName: String, map: Map<ENDPOINT, Boolean>): String {
    return "controllers:\n  $entityName:\n" +
            "    CREATE: ${map[ENDPOINT.CREATE]!!}\n" +
            "    READ: ${map[ENDPOINT.READ]!!}\n" +
            "    READ_ALL: ${map[ENDPOINT.READ_ALL]!!}\n" +
            "    UPDATE: ${map[ENDPOINT.UPDATE]!!}\n" +
            "    DELETE: ${map[ENDPOINT.DELETE]!!}"
}