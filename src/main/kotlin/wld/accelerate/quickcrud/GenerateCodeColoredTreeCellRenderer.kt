package wld.accelerate.quickcrud

import com.intellij.ui.ColoredTreeCellRenderer
import javax.swing.JTree

class GenerateCodeColoredTreeCellRenderer : ColoredTreeCellRenderer() {
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
                append("Generate Code\n")
            }
            1 -> {
                this.icon = Icons.AddFileIcon
                append("\tSQL\n")
            }
            2 -> {
                this.icon = Icons.AddFileIcon
                append("\tEnums\n")
            }
            3 -> {
                this.icon = Icons.AddFileIcon
                append("\tEntities\n")
            }
            4 -> {
                this.icon = Icons.AddFileIcon
                append("\tModels\n")
            }
            5 -> {
                this.icon = Icons.AddFileIcon
                append("\tControllers\n")
            }
            6 -> {
                this.icon = Icons.AddFileIcon
                append("\tServices")
            }
            7 -> {
                this.icon = Icons.AddFileIcon
                append("\tRepositories")
            }
            8 -> {
                this.icon = Icons.AddFileIcon
                append("\tVUE-Create\n")
            }
            9 -> {
                this.icon = Icons.AddFileIcon
                append("\tVUE-Details\n")
            }
            10 -> {
                this.icon = Icons.AddFileIcon
                append("\tVUE-List\n")
            }
            11 -> {
                this.icon = Icons.AddFileIcon
                append("\tVUE-Landing-Page\n")
            }
            12 -> {
                this.icon = Icons.AddFileIcon
                append("\tAll\n")
            }
        }
    }
}