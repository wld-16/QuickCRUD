package wld.accelerate.pipelinec

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.componentsList.layout.VerticalStackLayout
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NotNull
import wld.accelerate.pipelinec.extension.createMouseTreeListener
import wld.accelerate.pipelinec.extension.generatingMouseTreeListener
import java.awt.BorderLayout
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath


class BookInfo(s: String, s1: String)

class PipelineToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = ToolWindowContent(toolWindow)
        val content: Content =
            ContentFactory.getInstance().createContent(toolWindowContent.rootPanel, "Root Panel", false)
        toolWindow.contentManager.addContent(content)
    }


    private class ToolWindowContent(toolWindow: ToolWindow) {
        val rootPanel = SimpleToolWindowPanel(true)
        val generatingTreePanel = SimpleToolWindowPanel(true)
        val configTreePanel = SimpleToolWindowPanel(true)

        init {
            listOf(
                DDLGeneratorAction(),
                EntityGeneratorAction(),
                EnumGeneratorAction(),
                FullApplicationGeneratorAction(),
                VueComponentDetailsAction(),
                VueComponentFormAction(),
                VueComponentListAction()
            )

            rootPanel.layout = VerticalStackLayout()
            rootPanel.add(generatingTreePanel)
            rootPanel.add(configTreePanel)

            //val boxLayoutContainer = Container()
            //configTreePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0))

            val createConfigNode = DefaultMutableTreeNode("Create Config", true)

            createConfigNode.add(DefaultMutableTreeNode("enums"))
            createConfigNode.add(DefaultMutableTreeNode("controllers"))
            createConfigNode.add(DefaultMutableTreeNode("entities"))

            val generateNode = DefaultMutableTreeNode("generate", true)

            generateNode.add(DefaultMutableTreeNode("sql"))
            generateNode.add(DefaultMutableTreeNode("entities"))
            generateNode.add(DefaultMutableTreeNode("controllers"))
            generateNode.add(DefaultMutableTreeNode("vue-create"))
            generateNode.add(DefaultMutableTreeNode("vue-details"))
            generateNode.add(DefaultMutableTreeNode("vue-list"))
            generateNode.add(DefaultMutableTreeNode("vue-landing-page"))

            val creatingConfigTree = Tree(createConfigNode)
            val generatingTree = Tree(generateNode)

            creatingConfigTree.addMouseListener(object : MouseAdapter() {

            })

            generatingTree.addMouseListener(generatingMouseTreeListener(generatingTree, toolWindow))
            println("has added tree selection listener")

            generatingTreePanel.add(generatingTree)
            configTreePanel.add(creatingConfigTree)

            rootPanel.add(configTreePanel)
            rootPanel.add(generatingTreePanel)
        }

        private fun setIconLabel(label: JLabel, imagePath: String) {
            label.setIcon(ImageIcon(Objects.requireNonNull(javaClass.getResource(imagePath))))
        }

        @NotNull
        private fun createControlsPanel(toolWindow: ToolWindow): JPanel {
            val controlsPanel = JPanel()
            val treeActions = Tree()

            val refreshDateAndTimeButton = JButton("Refresh")
            //refreshDateAndTimeButton.addActionListener { e: ActionEvent? -> updateCurrentDateTime() }
            controlsPanel.add(refreshDateAndTimeButton)
            val hideToolWindowButton = JButton("Hide")
            hideToolWindowButton.addActionListener { e: ActionEvent? -> toolWindow.hide(null) }
            controlsPanel.add(hideToolWindowButton)
            return controlsPanel
        }

        private fun getFormattedValue(calendar: Calendar, calendarField: Int): String {
            val value: Int = calendar.get(calendarField)
            return StringUtils.leftPad(value.toString(), 2, "0")
        }

        companion object {
            private const val CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png"
            private const val TIME_ZONE_ICON_PATH = "/toolWindow/Time-zone-icon.png"
            private const val TIME_ICON_PATH = "/toolWindow/Time-icon.png"
        }
    }
}