package wld.accelerate.quickcrud

import com.intellij.ui.ColoredTreeCellRenderer
import javax.swing.JTree

class CreateConfigColoredTreeCellRenderer : ColoredTreeCellRenderer() {
    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        when (row) {
            0 -> {
                this.icon = Icons.PipelineIcon
                append("Config\n")
            }
            1 -> {
                this.icon = Icons.AddFileIcon
                append("\tenums\n")
            }
            2 -> {
                this.icon = Icons.AddFileIcon
                append("\tcontrollers\n")
            }
            3 -> {
                this.icon = Icons.AddFileIcon
                append("\tentities\n")
            }
        }
    }
}