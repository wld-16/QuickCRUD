package wld.accelerate.quickcrud

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import org.jetbrains.annotations.NotNull
import org.yaml.snakeyaml.Yaml
import java.io.File


const val ENTITY_PATH = "entity/"
private const val CONTROLLER_PATH = "controller/"
private const val SERVICE_PATH = "service"
private const val REPOSITORY_PATH = "repository/"

class FullApplicationGeneratorAction : AnAction() {
    override fun update(@NotNull event: AnActionEvent) {

        // Using the event, evaluate the context,
        // and enable or disable the action.
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        val yaml: Yaml = Yaml()

        return super.getActionUpdateThread()
    }

    override fun actionPerformed(@NotNull event: AnActionEvent) {
        FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), event.project, null) {
            val file = it.canonicalFile

            val yamlMap = parseYaml(file!!.path)

            val linkedHashMap = yamlMap?.get("enums") as LinkedHashMap<String, String>

            val enumClassRepresentations = writeJavaEnums(linkedHashMap, yamlMap["packagePath"] as String)
            val entityClassRepresentations = writeEntityDataClassJava(yamlMap?.get("entities") as Map<String, Map<String, Any>>, yamlMap["packagePath"] as String)
            val repositoryClassRepresentations = writeJavaRepositoryDataClass((yamlMap?.get("entities") as Map<String, Map<String, Any>>).keys.toList(), (yamlMap["packagePath"] as String) + ".java.repository")
            val controllerClassRepresentations = writeJavaControllerClasses((yamlMap["entities"] as Map<String, *>).keys.toList(), (yamlMap["packagePath"] as String) + ".java.controller")
            val serviceClassRepresentations = writeJavaServiceClass(yamlMap?.get("entities") as Map<String, Map<String, Any>>, yamlMap["packagePath"] as String)

           val basePath = event.project!!.basePath + "/src/"
            enumClassRepresentations.forEach { fileEntry ->
                File(basePath  + fileEntry.key + ".java").writeText(fileEntry.value)
            }

            entityClassRepresentations.forEach{ fileEntry ->
                File(basePath + ENTITY_PATH + fileEntry.key +".java").writeText(fileEntry.value)
            }

            repositoryClassRepresentations.forEach{ fileEntry ->
                File(basePath + REPOSITORY_PATH + fileEntry.key + "Repository.java").writeText(fileEntry.value)
            }

            controllerClassRepresentations.forEach{ fileEntry ->
                File(basePath + CONTROLLER_PATH + fileEntry.key + "Controller.java").writeText(fileEntry.value)
            }

            serviceClassRepresentations.forEach { fileEntry ->
                File(basePath + SERVICE_PATH + fileEntry.key + "Service.java").writeText(fileEntry.value)
            }
        }

        // Using the event, implement an action.
        // For example, create and show a dialog.
    } // Override getActionUpdateThread() when you target 2022.3 or later!
}