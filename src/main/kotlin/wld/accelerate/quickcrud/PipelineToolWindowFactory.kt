package wld.accelerate.quickcrud

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.componentsList.layout.VerticalStackLayout
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.quickcrud.action.*
import wld.accelerate.quickcrud.extension.createConfigMouseTreeListener
import wld.accelerate.quickcrud.extension.generatingClassesMouseTreeListener
import javax.swing.tree.DefaultMutableTreeNode

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

            val createConfigNode = DefaultMutableTreeNode("Config", true)

            createConfigNode.add(DefaultMutableTreeNode("enums"))
            createConfigNode.add(DefaultMutableTreeNode("controllers"))
            createConfigNode.add(DefaultMutableTreeNode("entities"))

            val coloredTreeCellRenderer: ColoredTreeCellRenderer = CreateConfigColoredTreeCellRenderer()


            val creatingConfigTree = Tree(createConfigNode)
            creatingConfigTree.cellRenderer = coloredTreeCellRenderer


            val generateNode = DefaultMutableTreeNode("generate", true)
            generateNode.add(DefaultMutableTreeNode("sql"))
            generateNode.add(DefaultMutableTreeNode("enums"))
            generateNode.add(DefaultMutableTreeNode("entities"))
            generateNode.add(DefaultMutableTreeNode("models"))
            generateNode.add(DefaultMutableTreeNode("controllers"))
            generateNode.add(DefaultMutableTreeNode("services"))
            generateNode.add(DefaultMutableTreeNode("repositories"))
            generateNode.add(DefaultMutableTreeNode("vue-create"))
            generateNode.add(DefaultMutableTreeNode("vue-details"))
            generateNode.add(DefaultMutableTreeNode("vue-list"))
            generateNode.add(DefaultMutableTreeNode("vue-landing-page"))
            generateNode.add(DefaultMutableTreeNode("all"))


            val generateCodeColoredTreeCellRenderer: ColoredTreeCellRenderer = GenerateCodeColoredTreeCellRenderer()

            val generatingClassesTree = Tree(generateNode)
            generatingClassesTree.cellRenderer = generateCodeColoredTreeCellRenderer




            creatingConfigTree.addMouseListener(createConfigMouseTreeListener(creatingConfigTree, toolWindow))
            generatingClassesTree.addMouseListener(generatingClassesMouseTreeListener(generatingClassesTree, toolWindow))
            println("has added tree selection listener")

            generatingTreePanel.add(generatingClassesTree)
            configTreePanel.add(creatingConfigTree)

            rootPanel.add(configTreePanel)
            rootPanel.add(generatingTreePanel)
        }

    }
}