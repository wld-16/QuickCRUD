package wld.accelerate.pipelinec.extension

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.pipelinec.*
import wld.accelerate.pipelinec.dialog.CreateControllerDialog
import wld.accelerate.pipelinec.dialog.CreateEntitiyDialog
import wld.accelerate.pipelinec.dialog.CreateEnumDialog
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
            if (mouseEvent.clickCount === 2) { // Double-click
                val path: TreePath = generateTree.getPathForLocation(mouseEvent.x, mouseEvent.y)
                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject

                        //val file = File("src/main/resources/" + classLoader.getResource(resourceName)!!.file)
                        if ("generate" == userObject) {

                        } else {

                            FileChooser.chooseFile(
                                FileChooserDescriptorFactory.createSingleFileDescriptor(),
                                toolWindow.project,
                                null
                            ) {

                                val file = File(it.path)
                                val yamlMap = parseYaml(file.absolutePath)
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

                                if ("sql" == userObject) {
                                    generatingFunction(
                                        "/src/res/sql",
                                        writeDDL(yamlMap?.get("entities") as Map<String, Map<String, Any>>),
                                        ".sql"
                                    )
                                } else if ("entities" == userObject) {
                                    generatingFunction(
                                        "/src/res/entities", writeEntityDataClassJava(
                                            yamlMap?.get("entities") as Map<String, Map<String, Any>>,
                                            (yamlMap.get("packagePath") as String) + ".entities"
                                        ), ".java"
                                    )
                                } else if ("controllers" == userObject) {
                                    generatingFunction(
                                        "/src/res/controllers", writeJavaControllerClasses(
                                            (yamlMap?.get("entities") as Map<String, *>).keys.toList(),
                                            (yamlMap["packagePath"] as String) + ".java.controller"
                                        ), ".java"
                                    )

                                } else if ("vue-create" == userObject) {
                                    generatingFunction(
                                        "/src/res/vue/components",
                                        writeVueCreateForm(yamlMap?.get("entities") as Map<String, Map<String, Any>>),
                                        "Create.vue"
                                    )

                                } else if ("vue-details" == userObject) {
                                    generatingFunction(
                                        "/src/res/vue/components", writeVueDetailsComponentTemplate(
                                            yamlMap?.get("entities") as Map<String, Map<String, Any>>
                                        ), "Details.vue"
                                    )

                                } else if ("vue-list" == userObject) {
                                    generatingFunction(
                                        "/src/res/vue/components", writeVueListComponentTemplate(
                                            yamlMap?.get("entities") as Map<String, Map<String, Any>>
                                        ), "List.vue"
                                    )

                                } else if ("vue-landing-page" == userObject) {
                                    generatingFunction(
                                        "/src/res/vue/components",
                                        mapOf("LandingPage" to writeVueLandingPageComponentTemplate((yamlMap?.get("entities") as Map<String, *>).keys.toList())),
                                        ".vue"
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

fun createConfigMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
    return object : MouseAdapter() {
        override fun mouseClicked(mouseEvent: MouseEvent) {
            if (mouseEvent.clickCount === 2) { // Double-click
                val path: TreePath = generateTree.getPathForLocation(mouseEvent.x, mouseEvent.y)
                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject

                        if ("entities" == userObject) {
                            val createEntitiyDialog = CreateEntitiyDialog()

                            if (createEntitiyDialog.showAndGet()) {
                                val entityName = createEntitiyDialog.fieldName?.text
                                val fields = mutableMapOf<String,Any>()

                                for(i in 0..(createEntitiyDialog.jbTable!!.model!!.rowCount - 1)){
                                    fields[createEntitiyDialog.jbTable?.model?.getValueAt(i,0).toString()] = createEntitiyDialog.jbTable!!.model!!.getValueAt(i,1).toString()
                                }

                                val file = File(toolWindow.project.basePath + "/src/res/config.yaml")

                                try { parseYaml(file.absolutePath) }
                                catch (e: FileNotFoundException) {
                                    File(toolWindow.project.basePath + "/src/res/config.yaml").writeText("")
                                }
                                val yamlMap = parseYaml(file.absolutePath)

                                val entitiesMap: MutableMap<String, Map<String, Any>> =
                                    if(yamlMap?.containsKey("entities") == true) {
                                        yamlMap["entities"] as MutableMap<String, Map<String, Any>>
                                    }
                                    else {
                                        mutableMapOf<String, Map<String, Any>>()
                                    }
                                entitiesMap[entityName!!] = fields as Map<String, Any>

                                var controllersMap: MutableMap<String, Map<String, Boolean>> = mutableMapOf()
                                if(yamlMap?.containsKey("controllers") == true) {
                                    Messages.showInfoMessage("Found another controller", "info")
                                    (yamlMap["controllers"] as MutableMap<String, Map<String, Boolean>>).also { controllersMap = it }
                                }

                                file.writeText(
                                    writePackagePath("wld.accelerate.pipelinec") + "\n" +
                                        writeYamlEntities(entitiesMap) + "\n" +
                                        writeYamlControllers(controllersMap)
                                )
                            }
                        } else if("controllers" == userObject) {
                            val createControllerDialog = CreateControllerDialog()
                            if (createControllerDialog.showAndGet()) {
                                val entityName = createControllerDialog.fieldName?.text
                                val fields = mapOf<String, Boolean>(
                                    ENDPOINT.CREATE.name to createControllerDialog.jbTable!!.model!!.getValueAt(ENDPOINT.CREATE.ordinal,1).toString().toBoolean(),
                                    ENDPOINT.READ.name to createControllerDialog.jbTable!!.model!!.getValueAt(ENDPOINT.READ.ordinal,1).toString().toBoolean(),
                                    ENDPOINT.READ_ALL.name to createControllerDialog.jbTable!!.model!!.getValueAt(ENDPOINT.READ_ALL.ordinal,1).toString().toBoolean(),
                                    ENDPOINT.UPDATE.name to createControllerDialog.jbTable!!.model!!.getValueAt(ENDPOINT.UPDATE.ordinal,1).toString().toBoolean(),
                                    ENDPOINT.DELETE.name to createControllerDialog.jbTable!!.model!!.getValueAt(ENDPOINT.DELETE.ordinal,1).toString().toBoolean()
                                )

                                val file = File(toolWindow.project.basePath + "/src/res/config.yaml")

                                try { parseYaml(file.absolutePath) }
                                catch (e: FileNotFoundException) {
                                    File(toolWindow.project.basePath + "/src/res/config.yaml").writeText("")
                                }
                                val yamlMap = parseYaml(file.absolutePath)

                                var controllersMap: MutableMap<String, Map<String, Boolean>> = mutableMapOf()
                                    if(yamlMap?.containsKey("controllers") == true) {
                                        Messages.showInfoMessage("Found another controller", "info")
                                        (yamlMap["controllers"] as MutableMap<String, Map<String, Boolean>>).also { controllersMap = it }
                                    }

                                val entitiesMap: MutableMap<String, Map<String, Any>> =
                                    if(yamlMap?.containsKey("entities") == true) {
                                        yamlMap["entities"] as MutableMap<String, Map<String, Any>>
                                    }
                                    else {
                                        mutableMapOf<String, Map<String, Any>>()
                                    }
                                controllersMap[entityName!!] = fields

                                file.writeText(
                                    writePackagePath("wld.accelerate.pipelinec") + "\n" +
                                            writeYamlEntities(entitiesMap) + "\n" +
                                            writeYamlControllers(controllersMap)
                                )
                            }
                        } else if("enums" == userObject) {
                            val createEnumDialog = CreateEnumDialog()
                            if (createEnumDialog.showAndGet()) {
                                val enumName = createEnumDialog.fieldName?.text
                                val fields = mutableMapOf<String, MutableList<String>>()

                                fields[enumName!!] = mutableListOf<String>()

                                for(i in 0..(createEnumDialog.jbTable!!.model!!.rowCount - 1)){
                                    // WIP Test
                                    fields[enumName]!!.add(createEnumDialog.jbTable?.model?.getValueAt(i,0).toString())
                                }
                                Messages.showInfoMessage(fields.get(enumName)!!.joinToString(), "join")
                                val file = File(toolWindow.project.basePath + "/src/res/config.yaml")

                                try { parseYaml(file.absolutePath) }
                                catch (e: FileNotFoundException) {
                                    File(toolWindow.project.basePath + "/src/res/config.yaml").writeText("")
                                }
                                val yamlMap = parseYaml(file.absolutePath)

                                var controllersMap: MutableMap<String, Map<String, Boolean>> =
                                if(yamlMap?.containsKey("controllers") == true) {
                                    yamlMap["controllers"] as MutableMap<String, Map<String, Boolean>>
                                } else mutableMapOf()

                                val entitiesMap: MutableMap<String, Map<String, Any>> =
                                    if(yamlMap?.containsKey("entities") == true) {
                                        yamlMap["entities"] as MutableMap<String, Map<String, Any>>
                                    } else mutableMapOf()

                                var enumsMap: MutableMap<String, List<String>> =
                                    if(yamlMap?.containsKey("enums") == true) {
                                        yamlMap["enums"] as MutableMap<String, List<String>>
                                    } else mutableMapOf()

                                enumsMap[enumName] = fields[enumName]!!

                                file.writeText(
                                    writePackagePath("wld.accelerate.pipelinec") + "\n" +
                                            writeYamlEnums(enumsMap) + "\n" +
                                            writeYamlEntities(entitiesMap) + "\n" +
                                            writeYamlControllers(controllersMap)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class ENDPOINT {
    CREATE,
    READ,
    READ_ALL,
    UPDATE,
    DELETE
}
