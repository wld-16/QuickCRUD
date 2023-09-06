package wld.accelerate.pipelinec

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.jetbrains.annotations.NotNull
import org.yaml.snakeyaml.Yaml


class EntityGenerator: AnAction() {
        override fun update(@NotNull event: AnActionEvent) {
            // Using the event, evaluate the context,
            // and enable or disable the action.
        }

        override fun getActionUpdateThread(): ActionUpdateThread {
                val yaml: Yaml = Yaml()

            return super.getActionUpdateThread()
        }

        override fun actionPerformed(@NotNull event: AnActionEvent) {
            // Using the event, implement an action.
            // For example, create and show a dialog.
        } // Override getActionUpdateThread() when you target 2022.3 or later!
}