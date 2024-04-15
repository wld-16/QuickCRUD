package wld.accelerate.quickcrud

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.componentsList.layout.VerticalStackLayout
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.quickcrud.extension.createConfigMouseTreeListener
import wld.accelerate.quickcrud.extension.generatingClassesMouseTreeListener
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.tree.DefaultMutableTreeNode

private const val CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png"

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

            val createConfigNode = DefaultMutableTreeNode(JLabel("Create Config"), true)
            //setIconForNode(createConfigNode, ImageIcon(AllIcons.Nodes.Package.printToString()))

            createConfigNode.add(DefaultMutableTreeNode("enums"))
            createConfigNode.add(DefaultMutableTreeNode("controllers"))
            createConfigNode.add(DefaultMutableTreeNode("entities"))

            val generateNode = DefaultMutableTreeNode("generate", true)
            setIconForNode(generateNode, ImageIcon(javaClass.getResource(CALENDAR_ICON_PATH) ))
            generateNode.add(DefaultMutableTreeNode("sql"))
            generateNode.add(DefaultMutableTreeNode("entities"))
            generateNode.add(DefaultMutableTreeNode("controllers"))
            generateNode.add(DefaultMutableTreeNode("vue-create"))
            generateNode.add(DefaultMutableTreeNode("vue-details"))
            generateNode.add(DefaultMutableTreeNode("vue-list"))
            generateNode.add(DefaultMutableTreeNode("vue-landing-page"))

            val creatingConfigTree = Tree(createConfigNode)
            val generatingClassesTree = Tree(generateNode)

            creatingConfigTree.addMouseListener(createConfigMouseTreeListener(creatingConfigTree, toolWindow))
            generatingClassesTree.addMouseListener(generatingClassesMouseTreeListener(generatingClassesTree, toolWindow))
            println("has added tree selection listener")

            generatingTreePanel.add(generatingClassesTree)
            configTreePanel.add(creatingConfigTree)

            rootPanel.add(configTreePanel)
            rootPanel.add(generatingTreePanel)
        }

        private fun setIconLabel(label: JLabel, imagePath: String) {
            label.setIcon(ImageIcon(Objects.requireNonNull(javaClass.getResource(imagePath))))
        }

        private companion object fun setIconForNode(node: DefaultMutableTreeNode, icon: ImageIcon) {
            val text = node.userObject.toString()
            node.userObject = icon

        }
    }
}