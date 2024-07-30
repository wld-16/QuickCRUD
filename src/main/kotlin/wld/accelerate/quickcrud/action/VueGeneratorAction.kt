package wld.accelerate.quickcrud.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import org.jetbrains.annotations.NotNull
import org.yaml.snakeyaml.Yaml
import wld.accelerate.quickcrud.parseYaml
import wld.accelerate.quickcrud.writeVueCreateForm
import wld.accelerate.quickcrud.writeVueDetailsComponentTemplate
import wld.accelerate.quickcrud.writeVueListComponentTemplate
import java.io.File


class VueGeneratorAction : AnAction() {
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

            val vueDetailsComponentRepresentation = writeVueDetailsComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)
            val vueFormComponentRepresentation = writeVueCreateForm(yamlMap?.get("entities") as Map<String, Map<String, Any>>)
            val vueListComponentRepresentation = writeVueListComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

            val basePath = event.project!!.basePath + "/src/main/resources/vue/"


            vueDetailsComponentRepresentation.forEach { fileToContent ->
                File(basePath + fileToContent.key + "Details" +".vue").writeText(fileToContent.value)
            }

            vueFormComponentRepresentation.forEach { fileToContent ->
                File(basePath + fileToContent.key + "Form" +".vue").writeText(fileToContent.value)
            }

            vueListComponentRepresentation.forEach { fileToContent ->
                File(basePath + fileToContent.key + "List" +".vue").writeText(fileToContent.value)
            }
        }

        // Using the event, implement an action.
        // For example, create and show a dialog.
    } // Override getActionUpdateThread() when you target 2022.3 or later!
}