package wld.accelerate.pipelinec

import org.yaml.snakeyaml.Yaml
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