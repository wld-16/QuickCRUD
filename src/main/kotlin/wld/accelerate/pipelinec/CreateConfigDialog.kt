package wld.accelerate.pipelinec

import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Dimension
import javax.annotation.Nullable
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class CreateConfigDialog : DialogWrapper(true) {
    init {
        title = "Create Configuration"
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        val label = JLabel("Testing")
        label.preferredSize = Dimension(100, 100)
        dialogPanel.add(label, BorderLayout.CENTER)
        return dialogPanel
    }
}
