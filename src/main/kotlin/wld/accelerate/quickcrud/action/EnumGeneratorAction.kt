package wld.accelerate.quickcrud.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import org.jetbrains.annotations.NotNull
import org.yaml.snakeyaml.Yaml
import wld.accelerate.quickcrud.parseYaml
import wld.accelerate.quickcrud.writeJavaEnums
import java.io.File


class EnumGeneratorAction : AnAction() {
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

            val linkedHashMap = yamlMap["enums"] as LinkedHashMap<String, List<String>>

            val enumClassRepresentations = writeJavaEnums(linkedHashMap, yamlMap["packagePath"] as String)

            enumClassRepresentations.forEach { fileEntry ->
               File(event.project!!.basePath + "/src/main/" + fileEntry.key + ".java").writeText(fileEntry.value)
            }
        }

        // Using the event, implement an action.
        // For example, create and show a dialog.
    } // Override getActionUpdateThread() when you target 2022.3 or later!
}