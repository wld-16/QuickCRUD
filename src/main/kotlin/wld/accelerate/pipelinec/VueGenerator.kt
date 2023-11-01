package wld.accelerate.pipelinec

import wld.accelerate.pipelinec.extension.unCapitalize

fun writeVueRouterJs(entities: Map<String, Map<String, Any>>): String {
    val staticImportStatements = "import { createRouter, createWebHistory } from 'vue-router'\n"
    val dynamicImportStatements: (String) -> String = {
        "import ${it}Details from \"@/components/${it}Details.vue\";\n" +
        "import ${it}List from \"@/components/${it}List.vue\";\n" +
        "import ${it}CreateForm from \"@/components/${it}CreateForm.vue\";\n"
    }
    val routesHead =
        "const routes = [\n" +
                "\t{\n" +
                "\t\tpath: '/',\n" +
                "\t\tname: 'landing-page',\n" +
                "\t\tcomponent: LandingPage\n" +
                "\t},\n"
    val routesTemplate: (String) -> String = {
                "\t{\n" +
                "\t\tpath: '/${String.unCapitalize(it)}/:id',\n" +
                "\t\tname: '${String.unCapitalize(it)}-details',\n" +
                "\t\tcomponent: ${it}Details\n" +
                "\t}" +
                ", {\n" +
                "\t\tpath: '/${String.unCapitalize(it)}/list',\n" +
                "\t\tname: '${String.unCapitalize(it)}-list',\n" +
                "\t\tcomponent: ${it}List\n" +
                "\t}" +
                ", {\n" +
                "\t\tpath: '/${String.unCapitalize(it)}/create',\n" +
                "\t\tname: '${String.unCapitalize(it)}-create',\n" +
                "\t\tcomponent: ${it}CreateForm\n" +
                "\t}"
    }
    val routesFoot = "]\n"

    val creationStatement =
        "const router = createRouter({\n" +
                "    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.\n" +
                "\thistory: createWebHistory(),\n" +
                "\troutes, // short for `routes: routes`\n" +
                "})\n" +
                "\n" +
                "export default router"

    return staticImportStatements +
            entities.keys.joinToString(separator = "") { dynamicImportStatements(it) } +
            "import LandingPage from \"@/components/LandingPage.vue\";\n" +
            "\n" +
            routesHead +
            entities.keys.joinToString { routesTemplate(it) } +
            routesFoot +
            creationStatement
}

//fun writeVueIndexPage(entities: List<String>): String {

//}

fun writeVueListComponentTemplate(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val scriptsTagHead = "<script>\nexport default {\n\tsetup() {\n\t"
    val scriptActivityList: (String) -> String = { "\tconst ${it}List = []\n" }
    val scriptHeadersHead = "\t\tconst headers = ["
    val scriptHeadersFoot = "\n\t\t]\n\t"
    val scriptHeadersTemplate: (String) -> String = {field ->
        "\n\t\t\t{ title: '${field}', key: '${field}' }"
    }

    val returnStatement: (String) -> String = {
        "\treturn {\n\t\t\t${it}List,\n\t\t\theaders\n\t}"
    }

    val scriptClosingStatement = "}\n}\n</script>"

    val templateStatement: (String) -> String = { "<template>\n" +
            "  <v-data-table\n" +
            "      :headers=\"headers\"\n" +
            "      :items=\"${it}List\"\n" +
            "      class=\"elevation-1\"\n" +
            "      items-per-page=\"5\"\n" +
            "  ></v-data-table>\n" +
            "</template>" }

    return entities.entries.associate {entry ->
        entry.key to
                scriptsTagHead +
                scriptActivityList(entry.key) +
                scriptHeadersHead +
                entry.value.keys.joinToString { scriptHeadersTemplate(it) } +
                scriptHeadersFoot +
                returnStatement(entry.key) +
                scriptClosingStatement + "\n" +
                templateStatement(entry.key)
    }
}

fun writeVueLandingPageComponentTemplate(entities: List<String>): String {
    val scriptsTag =
        "<script>\n" +
                "export default {\n" +
                "  setup() {\n" +
                "    const name = 'LandingPage'\n" +
                "    return {\n" +
                "      name\n" +
                "    }\n" +
                "  },\n" +
                "}\n" +
                "</script>"

    val templateTagHead =
            "<template>\n" +
            "  <v-col>\n"

    val templateTagFoot =
        "  </v-col>\n" +
                "</template>"

    val templateTagTemplate: (String) -> String = {
            "    <v-row>\n" +
            "      <v-col>\n" +
            "        <router-link :to=\"{ name: '${String.unCapitalize(it)}-details', params: { id: '1' }}\">\n" +
            "          ${it}:1\n" +
            "        </router-link>\n" +
            "      </v-col>\n" +
            "      <v-col>\n" +
            "        <router-link :to=\"{ name: '${String.unCapitalize(it)}-list' }\">\n" +
            "          ${it}-List\n" +
            "        </router-link>\n" +
            "      </v-col>\n" +
            "      <v-col>\n" +
            "        <router-link :to=\"{ name: '${String.unCapitalize(it)}-create' }\">\n" +
            "          ${it}-Create\n" +
            "        </router-link>\n" +
            "      </v-col>\n" +
            "    </v-row>\n"
    }

    return scriptsTag + templateTagHead + entities.joinToString(separator = ""){ templateTagTemplate(it) } + templateTagFoot
}

fun writeVueCreateForm(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val scriptTag: (String, Map<String, Any>) -> String = { entity, fields ->
        "<script>\n" +
                "import { reactive } from 'vue';\n" +
                "import { postData } from \"@/composables/server\";\n" +
                "export default {\n" +
                "  setup() {\n" +
                "    let ${entity}CreateForm = reactive({\n" +
                fields.entries.joinToString(separator = ",\n") {
                    if (it.value == "String") {
                        "      ${it.key}: \"\""
                    } else if (it.value == "Integer") {
                        "      ${it.key}: 0"
                    } else if (it.value == "Boolean") {
                        "      ${it.key}: false"
                    } else {
                        "      ${it.key}: undefined"
                    }
                } +
                "    \n})\n" +
                "    function save() {\n" +
                "      postData(\"https://localhost:8080/${entity}/\", name)\n" +
                "      console.log(${entity}CreateForm)\n" +
                "    }\n" +
                "\n" +
                "    return {\n" +
                "      ${entity}CreateForm,\n" +
                "      save\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "</script>"
    }

    val templateTag: (String) -> String = {
        "<template>\n" +
                "  <v-form>\n" +
                "    <v-row>\n" +
                "      <v-col>\n" +
                "        <v-text-field v-model=\"${it}CreateForm.name\" label=\"name\"></v-text-field>\n" +
                "      </v-col>\n" +
                "      <v-col>\n" +
                "        <v-btn @click=\"save()\">Create ${it}</v-btn>\n" +
                "      </v-col>\n" +
                "    </v-row>\n" +
                "  </v-form>\n" +
                "</template>"
    }

    return entities.entries.associate { it.key to scriptTag(it.key, it.value) + "\n" + templateTag(it.key) }
}

fun writeVueDetailsComponentTemplate(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val scriptsTagHead: (String) -> String =
        { "<script setup>\nimport { reactive } from 'vue'\nconst ${String.unCapitalize(it)}Details = reactive({\n" }
    val scriptsTagFields: (String) -> String = {
        "  $it: undefined"
    }
    val scriptsTagFoot = "\n\t})\n</script>"

    val scriptTags = entities.entries.associate {
        val fields = it.value.keys.joinToString(separator = ",\n") { field -> scriptsTagFields(field) }
        it.key to (scriptsTagHead(it.key) + fields + scriptsTagFoot + "\n" + "\n")
    }

    val templateTags = entities.keys.associateWith {
        "<template>\n" +
                "  <v-table>\n" +
                "    <tbody>\n" +
                "    <tr v-for=\"(value, key) in ${String.unCapitalize(it)}Details\" class=\"text-left\" v-bind:key=\"value\">\n" +
                "      <td>{{ key }}</td>\n" +
                "      <td>{{ value }}</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "  </v-table>\n" +
                "</template>"
    }

    return entities.keys.associateWith { scriptTags[it] + templateTags[it] }
}
