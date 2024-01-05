package wld.accelerate.pipelinec.extension

import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.treeStructure.Tree
import wld.accelerate.pipelinec.*
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath


fun CreateConfigMouseTreeListener(generateTree: Tree, toolWindow: ToolWindow): MouseAdapter {
    return object : MouseAdapter() {
        override fun mouseClicked(mouseEvent: MouseEvent) {
            if (mouseEvent.clickCount === 2) { // Double-click
                val path: TreePath = generateTree.getPathForLocation(mouseEvent.x, mouseEvent.y)
                Messages.showInfoMessage("hello world", "hello")
                val testButton = JButton()

                testButton.addActionListener { actionEvent: ActionEvent? ->
                    if (CreateEntitiyDialog().showAndGet()) {
                        // user pressed OK
                    }
                }

                if (path != null) {
                    val lastPathComponent: Any = path.lastPathComponent
                    if (lastPathComponent is DefaultMutableTreeNode) {
                        val userObject = lastPathComponent.userObject

                        //val file = File("src/main/resources/" + classLoader.getResource(resourceName)!!.file)
                        Messages.showInfoMessage("hello world", "hello")
                        val testButton = JButton()
                        testButton.addActionListener { actionEvent: ActionEvent? ->
                            if (CreateEntitiyDialog().showAndGet()) {
                                // user pressed OK
                            }
                        }
                    }
                }
            }
        }
    }
}