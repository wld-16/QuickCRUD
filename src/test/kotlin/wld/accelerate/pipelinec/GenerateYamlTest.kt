package wld.accelerate.pipelinec

import junit.framework.TestCase.assertEquals
import org.junit.Test
import wld.accelerate.pipelinec.extension.ENDPOINT

class GenerateYamlTest {
    @Test
    fun test_writeYamlEntities() {
        val entitiesMap: Map<String, Map<String, String>> = mapOf("testEntity" to mapOf("asd" to "asd","wer" to "wer"), "t2" to mapOf("yxc" to "yxc"))
        val joinToString = "entities:\n  " + entitiesMap.asSequence()
            .joinToString(separator = "\n  ") { entityEntry -> entityEntry.key + ":\n    " + entityEntry.value.entries.joinToString(separator = "\n    ") { it.key + ": " + it.value } }
        val resultString = "entities:" + "\n  testEntity:\n    asd: asd\n    wer: wer\n  t2:\n    yxc: yxc"
        assertEquals(resultString, joinToString)
        assertEquals(resultString, writeYamlEntities(entitiesMap))
    }

    @Test
    fun test_writeYamlEnums() {
        val entitiesMap: Map<String, List<String>> = mapOf(
            "testEntity" to listOf("asd, qwe, yxc"),
            "testa" to listOf("der, die, das"),
        )
        val resultString = "enums:" + "\n  testEntity: [asd, qwe, yxc]\n  testa: [der, die, das]"
        assertEquals(resultString, writeYamlEnums(entitiesMap))
    }

    @Test
    fun test_writeYamlControllers() {
        val controllersMap: Map<String, Map<String, Boolean>> =
                mapOf("testEntity" to mapOf(
                    ENDPOINT.CREATE.name to true,
                    ENDPOINT.READ.name to true,
                    ENDPOINT.READ_ALL.name to false,
                    ENDPOINT.UPDATE.name to true,
                    ENDPOINT.DELETE.name to false))
        val joinToString = writeYamlControllers(controllersMap)
        val resultString = "controllers:" + "\n  testEntity:\n    CREATE: true\n    READ: true\n    READ_ALL: false\n    UPDATE: true\n    DELETE: false"
        assertEquals(resultString, joinToString)
    }
}