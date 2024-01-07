package wld.accelerate.pipelinec

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.componentsList.layout.VerticalStackLayout
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.pipelinec.extension.createConfigMouseTreeListener
import wld.accelerate.pipelinec.extension.generatingClassesMouseTreeListener
import java.util.*
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode


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
    }
}