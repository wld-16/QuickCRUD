package wld.accelerate.pipelinec.extension

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.pipelinec.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

fun generatingMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
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

fun createMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
    return object : MouseAdapter() {
        override fun mouseClicked(mouseEvent: MouseEvent) {
            if (mouseEvent.clickCount === 2) { // Double-click
                val path: TreePath = generateTree.getPathForLocation(mouseEvent.x, mouseEvent.y)
                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject

                        Messages.showInfoMessage("" + mouseEvent.x + " - " + mouseEvent.y, "Tree Click")

                        val createConfigDialog = CreateConfigDialog()

                        if (createConfigDialog.showAndGet()) {
                            // user pressed OK
                        }
                    }
                }
            }
        }
    }
}
