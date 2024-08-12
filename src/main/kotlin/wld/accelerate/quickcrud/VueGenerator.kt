package wld.accelerate.quickcrud

import wld.accelerate.quickcrud.extension.capitalize
import wld.accelerate.quickcrud.extension.unCapitalize

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
    val scriptsTagHead = "<script setup>\n"
    val scriptsImport = "import {onMounted, ref} from \"vue\";\n" +
            "import {getData} from \"@/composables/server\";\n\n"

    val scriptEntityList: (String) -> String = { "const ${it}List = ref([])\n" }
    val scriptHeadersHead = "const headers = ["
    val scriptHeadersId = "{ title: 'id', key: 'id' },"
    val scriptHeadersFoot = "\n]\n"
    val scriptHeadersTemplate: (String) -> String = {field ->
        "\n\t\t\t{ title: '${field}', key: '${field}' }"
    }

    val onMounted: (String) -> String = {
        "\nonMounted(() => {\n" +
                "  getData(\"http://localhost:8080/${String.unCapitalize(it)}/\").then(response => {\n" +
                "    ${String.capitalize(it)}List.value = response\n" +
                "  })\n" +
                "})\n"
    }

    val scriptClosingStatement = "</script>"

    val templateStatement: (String) -> String = { "<template>\n" +
            "  <v-data-table\n" +
            "      :headers=\"headers\"\n" +
            "      :items=\"${it}List\"\n" +
            "      class=\"elevation-1\"\n" +
            "      items-per-page=\"5\"\n" +
            "  >\n" +
            "    <template v-slot:item.name=\"{ item }\">\n" +
            "      <router-link :to=\"'' + item.id\">{{ item.name.toUpperCase() }}</router-link>\n" +
            "    </template>\n" +
            "  </v-data-table>\n" +
            "</template>" }

    return entities.entries.associate {entry ->
        entry.key to
                scriptsTagHead +
                scriptsImport +
                scriptEntityList(entry.key) +
                scriptHeadersHead +
                scriptHeadersId +
                entry.value.keys.joinToString { scriptHeadersTemplate(it) } +
                scriptHeadersFoot +
                onMounted(entry.key) +
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
        "<template>" +
        "<v-navigation-drawer>\n" +
                "    <v-list>\n" +
                "      <v-list-item title=\"My Application\" subtitle=\"Vuetify\"></v-list-item>\n" +
                "      <v-divider></v-divider>\n"

    val templateTagFoot =
        "    </v-list>\n" +
                "</v-navigation-drawer>" +
                "</template>"

    val templateTagTemplate: (String) -> String = {
                    "      <v-list-item link=\"${String.unCapitalize(it)}-list\">\n" +
                    "        ${it}-List\n" +
                    "      </v-list-item>\n" +
                    "      <v-list-item link=\"${String.unCapitalize(it)}-create\">\n" +
                    "        ${it}-Create\n" +
                    "      </v-list-item>\n"
    }

    return scriptsTag + templateTagHead + entities.joinToString(separator = ""){ templateTagTemplate(it) } + templateTagFoot
}

fun writeVueAppNavigation(entities: List<String>): String {
    val scriptsTag =
        "<script>\n" +
                "export default {\n" +
                "  setup() {\n" +
                "    const name = 'RouteNavigationComponent'\n" +
                "    return {\n" +
                "      name\n" +
                "    }\n" +
                "  },\n" +
                "}\n" +
                "</script>"

    val templateTagHead =
        "<template>" +
                "<v-navigation-drawer>\n" +
                "    <v-list>\n" +
                "      <v-list-item title=\"My Application\" subtitle=\"Vuetify\"></v-list-item>\n" +
                "      <v-divider></v-divider>\n"

    val templateTagFoot =
        "    </v-list>\n" +
                "</v-navigation-drawer>" +
                "</template>"

    val templateTagTemplate: (String) -> String = {
                "      <v-list-item to=\"/${String.unCapitalize(it)}/list\">\n" +
                "        ${it}-List\n" +
                "      </v-list-item>\n" +
                "      <v-list-item to=\"/${String.unCapitalize(it)}/create\">\n" +
                "        ${it}-Create\n" +
                "      </v-list-item>\n"
    }

    return scriptsTag + templateTagHead + entities.joinToString(separator = ""){ templateTagTemplate(it) } + templateTagFoot
}

fun writeVueCreateForm(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val scriptTag: (String, Map<String, Any>, Map<String, Any>) -> String = { entity, fields, selectEntities ->
        "<script>\n" +
                "import { reactive, onMounted${if (selectEntities.size > 0) ", ref" else ""}} from 'vue';\n" +
                "import { postData${if (selectEntities.size > 0) ", getData" else ""} } from \"@/composables/server\";\n" +
                "import router from \"../plugins/router\";\n" +
                "export default {\n" +
                "  setup() {\n" +
                selectEntities.entries.joinToString (separator = ""){
                    if(it.value.toString() == "Object"){
                        "    let all${String.capitalize(it.key)} = ref()\n"
                    } else if(it.value.toString().contains("List")) {
                        "    let all${String.capitalize(it.key)} = ref()\n"
                    } else {
                        "    let all${it.value} = ref()\n"
                    }
                } +
                "    let ${entity}CreateForm = reactive({\n" +
                fields.entries.joinToString(separator = ",\n") {
                    if (it.value == "String") {
                        "      ${it.key}: \"\""
                    } else if (it.value == "Integer") {
                        "      ${it.key}: 0"
                    } else if (it.value == "Boolean") {
                        "      ${it.key}: false"
                    } else if (it.value.toString().contains("List")) {
                        "      ${it.key}: []"
                    } else {
                        "      ${it.key}: undefined"
                    }
                } +
                "    \n})\n" +
                "   onMounted(() => {\n" +
                selectEntities.entries.joinToString (separator = ""){
                    if(it.value.toString().contains("List")) {
                        "    getData(\"http://localhost:8080/${it.key.dropLast(4)}/\").then(response => {\n"
                    } else {
                        "    getData(\"http://localhost:8080/${it.key}/\").then(response => {\n"
                    } +
                        "        all${String.capitalize(it.key)}.value = response\n" +
                        "    })\n"
                } +
                "})\n" +
                "    function save() {\n" +
                "      postData(\"http://localhost:8080/${String.unCapitalize(entity)}/\", ${entity}CreateForm).then(response => {\n" +
                "        router.push(\"/${String.unCapitalize(entity)}/\"+response.id)\n" +
                "      })\n" +
                "    }\n" +
                "\n" +
                "    return {\n" +
                "      ${entity}CreateForm,\n" +
                selectEntities.keys.joinToString(separator = "") { "       all${String.capitalize(it)},\n" } +
                "      save\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "</script>"
    }

    val textFieldTag: (Pair<String, String>) -> String = {
        "        <v-text-field v-model=\"${it.first}CreateForm.${it.second}\" label=\"${it.second}\"></v-text-field>\n"
    }

    val selectFieldTag: (Triple<String, String, String>, Boolean) -> String = { entry, isObject ->
        "        <v-select v-model=\"${entry.first}CreateForm.${entry.second}\" label=\"${entry.second}\" ${if (isObject) "item-title=\"name\" return-object" else ""} :items=\"all${String.capitalize(entry.third)}\"></v-select>\n"
    }
    val autocompleteFieldTag: (Triple<String, String, String>) -> String = { entry ->
        "        <v-autocomplete v-model=\"${entry.first}CreateForm.${entry.second}\" multiple=\"true\" label=\"${entry.second}\" item-title=\"name\" return-object :items=\"all${String.capitalize(entry.third)}\"></v-autocomplete>\n"
    }

    val templateTag: (String, Map<String, Any>) -> String = { entity, fields ->
        "<template>\n" +
                "  <v-form>\n" +
                "    <v-row>\n" +
                "      <v-col>\n" +
                fields.entries.joinToString(separator = ""){ fieldEntry ->
                    if(fieldEntry.value.toString() == "String") {
                        textFieldTag(entity to fieldEntry.key)
                    } else if(fieldEntry.value.toString() == "Integer") {
                        textFieldTag(entity to fieldEntry.key)
                    } else if(fieldEntry.value.toString() == "Object") {
                        selectFieldTag(Triple(entity, fieldEntry.key, String.capitalize(fieldEntry.key)), true)
                    } else if(fieldEntry.value.toString().contains("List")) {
                        autocompleteFieldTag(Triple(entity, fieldEntry.key, String.capitalize(fieldEntry.key)))
                    } else {
                        selectFieldTag(Triple(entity, fieldEntry.key, fieldEntry.value.toString()), false)
                    }
                } +
                "      </v-col>\n" +
                "      <v-col>\n" +
                "        <v-btn @click=\"save()\">Create ${entity}</v-btn>\n" +
                "      </v-col>\n" +
                "    </v-row>\n" +
                "  </v-form>\n" +
                "</template>"
    }

    return entities.entries.associate { classEntry ->
        classEntry.key to scriptTag(classEntry.key, classEntry.value, classEntry.value.filter { it.value.toString() != "String" && it.value.toString() != "Integer" }) +
                "\n" +
            templateTag(classEntry.key, classEntry.value)
        }
}

fun writeVueDetailsComponentTemplate(entities: Map<String, Map<String, Any>>): Map<String, String> {
    val scriptsTagHead: (String) -> String =
        {
            "<script setup>\n" +
                    "import { onMounted, ref } from 'vue'\n" +
                    "import { useRoute } from \"vue-router\";\n" +
                    "import { getData } from \"@/composables/server\";\n"
        }

    val scriptsTagFields: (String) -> String = {
        "  const route = useRoute()\n" +
            "const ${String.capitalize(it)}Details = ref([])\n" +
            "const id = route.params.id\n"
    }

    val scriptsTagLifecycleHooks: (String) -> String = {
        "onMounted(() => {\n" +
                "  getData(\"http://localhost:8080/${String.unCapitalize(it)}/\" + id).then(response => {\n" +
                "    ${it}Details.value = response\n" +
                "  })\n" +
                "})"
    }

    val scriptsTagFoot = "\n</script>"

    val scriptTags = entities.entries.associate {
        it.key to (scriptsTagHead(it.key) + "\n" + scriptsTagFields(it.key) + "\n" + scriptsTagLifecycleHooks(it.key) + scriptsTagFoot + "\n" + "\n")
    }

    val templateTags = entities.keys.associateWith {
        "<template>\n" +
                "  <v-table>\n" +
                "    <tbody>\n" +
                "    <tr v-for=\"(value, key) in ${it}Details\" class=\"text-left\" v-bind:key=\"value\">\n" +
                "      <td>{{ key }}</td>\n" +
                "      <td>{{ value }}</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "  </v-table>\n" +
                "</template>"
    }

    return entities.keys.associateWith { scriptTags[it] + templateTags[it] }
}
