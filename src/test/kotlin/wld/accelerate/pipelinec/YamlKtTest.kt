package wld.accelerate.pipelinec

import org.junit.Test
import java.nio.file.Path
import kotlin.io.path.absolutePathString


class YamlKtTest() {
    @Test
    fun parseYaml() {
        print(Path.of("src/resources").absolutePathString())
        parseYaml(Path.of("./src/resources/sample/example.yaml").toString())
        assert(true)
    }
}