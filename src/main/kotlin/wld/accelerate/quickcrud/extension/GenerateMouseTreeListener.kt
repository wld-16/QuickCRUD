package wld.accelerate.quickcrud.extension

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.treeStructure.Tree
import kotlinx.collections.immutable.toPersistentHashMap
import wld.accelerate.quickcrud.*
import wld.accelerate.quickcrud.dialog.CreateControllerDialog
import wld.accelerate.quickcrud.dialog.CreateEntitiyDialog
import wld.accelerate.quickcrud.dialog.CreateEnumDialog
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

fun generatingClassesMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
    return object : MouseAdapter() {
        override fun mouseClicked(mouseEvent: MouseEvent) {
            if (mouseEvent.clickCount == 2) { // Double-click
                val path: TreePath? = generateTree.getPathForLocation(50, mouseEvent.y)
                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject

                        if ("generate" == userObject) {

                        } else {

                            FileChooser.chooseFile(
                                FileChooserDescriptorFactory.createSingleFileDescriptor(),
                                toolWindow.project,
                                null
                            ) {

                                val file = File(it.path)
                                val yamlMap = parseYaml(file.absolutePath)
                                val packagePathString = yamlMap["packagePath"] as String
                                val entitiesMap = yamlMap["entities"] as Map<String, Map<String, Any>>
                                val enumsMap = yamlMap["enums"] as LinkedHashMap<String, String>
                                val generatingFunction =
                                    { generateSuffix: String, entityClassRepresentations: Map<String, String>, fileEnding: String ->
                                        val generatePath = toolWindow.project.basePath + generateSuffix
                                        Files.createDirectories(Path.of(generatePath))

                                        entityClassRepresentations.forEach { fileToContent ->
                                            File(generatePath + "/" + fileToContent.key + fileEnding).writeText(
                                                fileToContent.value
                                            )
                                        }
                                    }

                                when (userObject) {
                                    "sql" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/sql/"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/sql/"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/sql",
                                            writeDDL(entitiesMap),
                                            ".sql"
                                        )
                                    }

                                    "entities" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/entity"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString/entity"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/entity", writeEntityDataClassJava(
                                                entitiesMap,
                                                packagePathString
                                            ), ".java"
                                        )
                                    }

                                    "enums" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/controller", mapOf("EnumController" to writeJavaEnumControllerClass(
                                                enumsMap.keys.toList(),
                                                yamlMap["packagePath"] as String)),
                                                ".java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString", writeJavaEnums(
                                                enumsMap.entries.associate{ enumPair -> String.capitalize(enumPair.key) to enumPair.value },
                                                packagePathString
                                            ), ".java"
                                        )
                                    }

                                    "controllers" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/controller", writeJavaControllerClasses(
                                                entitiesMap.keys.toList(),
                                                packagePathString
                                            ), "Controller.java"
                                        )
                                    }

                                    "services" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/service"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/service", writeJavaServiceClass(
                                                entitiesMap,
                                                packagePathString
                                            ), "Service.java"
                                        )
                                    }
                                    "models" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/model"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString/model"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/model", writeJavaModelDataClass(
                                                entitiesMap,
                                                packagePathString
                                            ), "Model.java"
                                        )
                                    }

                                    "repositories" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/repository"))
                                        }
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/repository",
                                            writeJavaRepositoryDataClass(
                                                entitiesMap.keys.toList(),
                                                packagePathString
                                            ),
                                            "Repository.java"
                                        )
                                    }

                                    "vue-create" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/vue/components",
                                            writeVueCreateForm(entitiesMap),
                                            "Create.vue"
                                        )
                                    }

                                    "vue-details" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/vue/components", writeVueDetailsComponentTemplate(
                                                entitiesMap
                                            ), "Details.vue"
                                        )

                                    }

                                    "vue-list" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/vue/components", writeVueListComponentTemplate(
                                                entitiesMap
                                            ), "List.vue"
                                        )
                                    }

                                    "vue-landing-page" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/vue/components",
                                            mapOf("LandingPage" to writeVueLandingPageComponentTemplate((yamlMap?.get("entities") as Map<String, *>).keys.toList())),
                                            ".vue"
                                        )
                                    }

                                    "all" -> {
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/vue/components"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/entity"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString/entity"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/controller"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/service"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/service"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/repository"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/repository"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString/model"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString/model"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/resources/sql/"))) {
                                            Files.createDirectories(Path.of("${toolWindow.project.basePath}/src/main/resources/sql/"))
                                        }
                                        if (!Files.exists(Path.of("${toolWindow.project.basePath}/src/main/java/$packagePathString"))) {
                                            Files.createDirectories(Path.of("$toolWindow.project.basePath/src/main/java/$packagePathString"))
                                        }
                                        generatingFunction(
                                            "/src/main/resources/vue/components",
                                            mapOf("LandingPage" to writeVueLandingPageComponentTemplate((yamlMap?.get("entities") as Map<String, *>).keys.toList())),
                                            ".vue"
                                        )
                                        generatingFunction(
                                            "/src/main/resources/vue/components", writeVueListComponentTemplate(
                                                entitiesMap
                                            ), "List.vue"
                                        )
                                        generatingFunction(
                                            "/src/main/resources/vue/components", writeVueDetailsComponentTemplate(
                                                entitiesMap
                                            ), "Details.vue"
                                        )
                                        generatingFunction(
                                            "/src/main/resources/vue/components",
                                            writeVueCreateForm(entitiesMap),
                                            "Create.vue"
                                        )
                                        generatingFunction(
                                            "/src/main/resources/sql",
                                            writeDDL(entitiesMap),
                                            ".sql"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString", writeJavaEnums(
                                                enumsMap,
                                                packagePathString
                                            ), ".java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/entity", writeEntityDataClassJava(
                                                entitiesMap,
                                                packagePathString
                                            ), ".java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/model", writeJavaModelDataClass(
                                                entitiesMap,
                                                packagePathString
                                            ), "Model.java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/controller", mapOf("EnumController" to writeJavaEnumControllerClass(
                                                enumsMap.keys.toList(),
                                                yamlMap["packagePath"] as String + ".controller")),
                                            ".java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/controller", writeJavaControllerClasses(
                                                entitiesMap.keys.toList(),
                                                packagePathString
                                            ), "Controller.java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/service", writeJavaServiceClass(
                                                entitiesMap,
                                                packagePathString
                                            ), "Service.java"
                                        )
                                        generatingFunction(
                                            "/src/main/java/$packagePathString/repository",
                                            writeJavaRepositoryDataClass(
                                                entitiesMap.keys.toList(),
                                                packagePathString
                                            ),
                                            "Repository.java"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createConfigMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
    return object : MouseAdapter() {
        override fun mouseClicked(mouseEvent: MouseEvent) {
            if (mouseEvent.clickCount === 2) { // Double-click
                val path: TreePath = generateTree.getPathForLocation(50, mouseEvent.y)
                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject
                        val configFile = File("${toolWindow.project.basePath}/src/main/resources/config.yaml")

                        val createConfigFileIfNotExists = {
                            if (!Files.exists(Path.of(configFile.absolutePath))) {
                                val file =
                                    File("${toolWindow.project.basePath}/src/main/resources/config.yaml").createNewFile()
                            }
                        }

                        if ("entities" == userObject) {
                            val createEntitiyDialog = CreateEntitiyDialog()

                            if (createEntitiyDialog.showAndGet()) {
                                val entityName = createEntitiyDialog.fieldName?.text
                                val fields = mutableMapOf<String, Any>()

                                for (i in 0..(createEntitiyDialog.jbTable!!.model!!.rowCount - 1)) {
                                    fields[createEntitiyDialog.jbTable?.model?.getValueAt(i, 0).toString()] =
                                        createEntitiyDialog.jbTable!!.model!!.getValueAt(i, 1).toString()
                                }

                                createConfigFileIfNotExists()
                                val yamlMap = parseYaml(configFile.absolutePath)

                                configFile.writeText(
                                    getConfigFileString(
                                        mapOf(Pair(entityName, fields)),
                                        mapOf<String, Any>(),
                                        mapOf<String, Any>(),
                                        yamlMap as Map<*, *>
                                    )
                                )
                            }
                        } else if ("controllers" == userObject) {
                            val createControllerDialog = CreateControllerDialog()
                            if (createControllerDialog.showAndGet()) {
                                val entityName = createControllerDialog.fieldName?.text
                                val fields = mapOf<String, Boolean>(
                                    ENDPOINT.CREATE.name to createControllerDialog.jbTable!!.model!!.getValueAt(
                                        ENDPOINT.CREATE.ordinal,
                                        1
                                    ).toString().toBoolean(),
                                    ENDPOINT.READ.name to createControllerDialog.jbTable!!.model!!.getValueAt(
                                        ENDPOINT.READ.ordinal,
                                        1
                                    ).toString().toBoolean(),
                                    ENDPOINT.READ_ALL.name to createControllerDialog.jbTable!!.model!!.getValueAt(
                                        ENDPOINT.READ_ALL.ordinal,
                                        1
                                    ).toString().toBoolean(),
                                    ENDPOINT.UPDATE.name to createControllerDialog.jbTable!!.model!!.getValueAt(
                                        ENDPOINT.UPDATE.ordinal,
                                        1
                                    ).toString().toBoolean(),
                                    ENDPOINT.DELETE.name to createControllerDialog.jbTable!!.model!!.getValueAt(
                                        ENDPOINT.DELETE.ordinal,
                                        1
                                    ).toString().toBoolean()
                                )

                                createConfigFileIfNotExists()
                                val yamlMap = parseYaml(configFile.absolutePath)

                                configFile.writeText(
                                    getConfigFileString(
                                        mapOf<String, Any>(),
                                        mapOf(Pair<String, Map<String, Any>>(entityName!!, fields)),
                                        mapOf<String, Any>(),
                                        yamlMap as Map<*, *>
                                    )
                                )
                            }
                        } else if ("enums" == userObject) {
                            val createEnumDialog = CreateEnumDialog()
                            if (createEnumDialog.showAndGet()) {
                                val enumName = createEnumDialog.fieldName?.text
                                val fields = mutableMapOf<String, MutableList<String>>()

                                fields[enumName!!] = mutableListOf<String>()

                                for (i in 0..(createEnumDialog.jbTable!!.model!!.rowCount - 1)) {
                                    // WIP Test
                                    fields[enumName]!!.add(createEnumDialog.jbTable?.model?.getValueAt(i, 0).toString())
                                }
                                Messages.showInfoMessage(fields[enumName]!!.joinToString(), "join")

                                createConfigFileIfNotExists()
                                val yamlMap = parseYaml(configFile.absolutePath)

                                configFile.writeText(
                                    getConfigFileString(
                                        mapOf<String, Any>(),
                                        mapOf<String, Any>(),
                                        fields,
                                        yamlMap as Map<*, *>
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getConfigFileString(
    entityFields: Map<*, *>,
    controllerFields: Map<*, *>,
    enumFields: Map<*, *>,
    yamlMap: Map<*, *>
): String {
    val controllersMap: MutableMap<String, Map<String, Any>> = mutableMapOf()
    if (yamlMap.containsKey("controllers") && yamlMap["controllers"] != null) {
        controllersMap.putAll(yamlMap["controllers"] as Map<String, Map<String, Any>>)
    }
    controllersMap.putAll(controllerFields as Map<String, Map<String, Any>>)

    var entitiesMap: MutableMap<String, Map<String, Any>> = mutableMapOf()
    if (yamlMap.containsKey("entities") && yamlMap["entities"] != null) {
        entitiesMap.putAll(yamlMap["entities"] as Map<String, Map<String, Any>>)
    }
    entitiesMap.putAll(entityFields as Map<String, Map<String, Any>>)

    var enumsMap: MutableMap<String, List<String>> = mutableMapOf()
    if (yamlMap.containsKey("enums") && yamlMap["enums"] != null) {
        enumsMap = yamlMap["enums"] as MutableMap<String, List<String>>
    }
    enumsMap.putAll(enumFields as Map<String, List<String>>)

    val packagePath = yamlMap["packagePath"] ?: "<enterPackagePathHere>"

    return writePackagePath(packagePath.toString()) + "\n" +
            writeYamlEnums(enumsMap) + "\n" +
            writeYamlEntities(entitiesMap) + "\n" +
            writeYamlControllers(controllersMap)
}

enum class ENDPOINT {
    CREATE,
    READ,
    READ_ALL,
    UPDATE,
    DELETE
}
