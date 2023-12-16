package wld.accelerate.pipelinec

import com.intellij.execution.runToolbar.components.MouseListenerHelper
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.currentOrDefaultProject
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.ClickListener
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.treeStructure.Tree
import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NotNull
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.beans.PropertyChangeListener
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.swing.*
import javax.swing.event.MouseInputAdapter
import javax.swing.event.MouseInputListener
import javax.swing.event.TreeSelectionListener
import javax.swing.event.TreeWillExpandListener
import javax.swing.plaf.basic.BasicListUI.MouseInputHandler
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath
import kotlin.collections.HashMap


class BookInfo(s: String, s1: String) {

}

class PipelineToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = CalendarToolWindowContent(toolWindow)
        val content: Content =
            ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }


    private class CalendarToolWindowContent(toolWindow: ToolWindow) {
        val contentPanel = JPanel()
        private val currentDate = JLabel()
        private val timeZone = JLabel()
        private val currentTime = JLabel()

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

            contentPanel.setLayout(BorderLayout(0, 20))
            contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0))
            //contentPanel.add(createCalendarPanel(), BorderLayout.PAGE_START)

            val defaultMutableTreeNode = DefaultMutableTreeNode("Generate", true)

            defaultMutableTreeNode.add(DefaultMutableTreeNode("SQL"))
            defaultMutableTreeNode.add(DefaultMutableTreeNode("Entities"))
            defaultMutableTreeNode.add(DefaultMutableTreeNode("Controllers"))
            defaultMutableTreeNode.add(DefaultMutableTreeNode("Vue"))

            val tree = Tree(defaultMutableTreeNode)

            println("added property change listener")


            tree.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(mouseEvent: MouseEvent) {
                    if (mouseEvent.getClickCount() === 2) { // Double-click

                        Messages.showInfoMessage("" + mouseEvent.getX() + " - " + mouseEvent.getY(), "Tree Click")


                        val path: TreePath = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY())
                        if (path != null) {
                            val lastPathComponent: Any = path.getLastPathComponent()
                            if (lastPathComponent is DefaultMutableTreeNode) {
                                val userObject = lastPathComponent.userObject

                                //val file = File("src/main/resources/" + classLoader.getResource(resourceName)!!.file)

                                Messages.showInfoMessage(userObject.toString(), "Tree Click")

                                FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), toolWindow.project, null) {

                                    val file = File(it.path)
                                    val yamlMap = parseYaml(file.absolutePath)

                                    if ("SQL" == userObject) {
                                        val sqlGeneratePath = toolWindow.project.basePath + "/src/res/sql"
                                        Files.createDirectories(Path.of(sqlGeneratePath))

                                        val entityClassRepresentations =
                                            writeDDL(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

                                        entityClassRepresentations.forEach { fileToContent ->
                                            File(sqlGeneratePath + "/" + fileToContent.key + ".sql").writeText(
                                                fileToContent.value
                                            )
                                        }
                                    } else if ("Entities" == userObject) {
                                        val entitiesGeneratePath = toolWindow.project.basePath + "/src/res/entities"
                                        Files.createDirectories(Path.of(entitiesGeneratePath))

                                        val entityClassRepresentations =
                                            writeEntityDataClassJava(yamlMap?.get("entities") as Map<String, Map<String, Any>>,
                                                (yamlMap?.get("packagePath") as String) + ".entities"
                                            )

                                        entityClassRepresentations.forEach { fileToContent ->
                                            File(entitiesGeneratePath + "/" + fileToContent.key + ".java").writeText(
                                                fileToContent.value
                                            )
                                        }
                                    } else if ("Controllers" == userObject) {
                                        val entitiesGeneratePath = toolWindow.project.basePath + "/src/res/controllers"
                                        Files.createDirectories(Path.of(entitiesGeneratePath))

                                        val entityClassRepresentations =
                                            writeJavaControllerClasses((yamlMap?.get("entities") as Map<String, *>).keys.toList(), (yamlMap["packagePath"] as String) + ".java.controller")

                                        entityClassRepresentations.forEach { fileToContent ->
                                            File(entitiesGeneratePath + "/" + fileToContent.key + ".java").writeText(
                                                fileToContent.value
                                            )
                                        }

                                    } else if ("Vue" == userObject) {
                                        val entitiesGeneratePath = toolWindow.project.basePath + "/src/res/vue/components"
                                        Files.createDirectories(Path.of(entitiesGeneratePath))

                                        val vueComponentCreateForm =
                                            writeVueCreateForm(yamlMap?.get("entities") as Map<String, Map<String, Any>>)

                                        val vueComponentDetailsRepresentations = writeVueDetailsComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)
                                        val vueListComponentDetailsRepresentations = writeVueListComponentTemplate(yamlMap?.get("entities") as Map<String, Map<String, Any>>)
                                        val vueLandingPageComponentRepresentations = writeVueLandingPageComponentTemplate((yamlMap?.get("entities") as Map<String, *>).keys.toList())

                                        vueComponentCreateForm.forEach{
                                            File(entitiesGeneratePath + "/" + it.key + "CreateForm" +".vue").writeText(it.value)
                                        }

                                        vueComponentDetailsRepresentations.forEach {
                                            File(entitiesGeneratePath + "/" + it.key + "Details" +".vue").writeText(it.value)
                                        }

                                        vueListComponentDetailsRepresentations.forEach {
                                            File(entitiesGeneratePath + "/" + it.key + "List" +".vue").writeText(it.value)
                                        }

                                        File("$entitiesGeneratePath/LandingPage.vue").writeText(vueLandingPageComponentRepresentations)
                                    }
                                }
                            }
                        }
                    }
                }
            })


            println("has added tree selection listener")

            contentPanel.add(tree, BorderLayout.CENTER)

            //contentPanel.add(createControlsPanel(toolWindow), BorderLayout.CENTER)
            updateCurrentDateTime()
        }

        private fun createNodes(top: DefaultMutableTreeNode) {
            var category: DefaultMutableTreeNode? = null
            var book: DefaultMutableTreeNode? = null
            category = DefaultMutableTreeNode("Books for Java Programmers")
            top.add(category)

            //original Tutorial
            book = DefaultMutableTreeNode(
                BookInfo(
                    "The Java Tutorial: A Short Course on the Basics",
                    "tutorial.html"
                )
            )
            category.add(book)

            //Tutorial Continued
            book = DefaultMutableTreeNode(
                BookInfo(
                    "The Java Tutorial Continued: The Rest of the JDK",
                    "tutorialcont.html"
                )
            )
            category.add(book)

            //Swing Tutorial
            book = DefaultMutableTreeNode(
                BookInfo(
                    "The Swing Tutorial: A Guide to Constructing GUIs",
                    "swingtutorial.html"
                )
            )
            category.add(book)

            //...add more books for programmers...
            category = DefaultMutableTreeNode("Books for Java Implementers")
            top.add(category)

            //VM
            book = DefaultMutableTreeNode(
                BookInfo(
                    "The Java Virtual Machine Specification",
                    "vm.html"
                )
            )
            category.add(book)

            //Language Spec
            book = DefaultMutableTreeNode(
                BookInfo(
                    "The Java Language Specification",
                    "jls.html"
                )
            )
            category.add(book)
        }

        @NotNull
        private fun createActionsTree(): Tree {
            val actionsTree = Tree();
            val calendarPanel = JPanel()
            setIconLabel(timeZone, TIME_ZONE_ICON_PATH)
            calendarPanel.add(timeZone);
            actionsTree.add(calendarPanel);
            //actionsTree.add(timeZone);
            //actionsTree.add(timeZone);
            return actionsTree;
        }

        @NotNull
        private fun createCalendarPanel(): JPanel {
            val calendarPanel = JPanel()
            setIconLabel(currentDate, CALENDAR_ICON_PATH)
            setIconLabel(timeZone, TIME_ZONE_ICON_PATH)
            setIconLabel(currentTime, TIME_ICON_PATH)
            calendarPanel.add(currentDate)
            calendarPanel.add(timeZone)
            calendarPanel.add(currentTime)
            return calendarPanel
        }

        private fun setIconLabel(label: JLabel, imagePath: String) {
            label.setIcon(ImageIcon(Objects.requireNonNull(javaClass.getResource(imagePath))))
        }

        @NotNull
        private fun createControlsPanel(toolWindow: ToolWindow): JPanel {
            val controlsPanel = JPanel()

            val refreshDateAndTimeButton = JButton("Refresh")
            refreshDateAndTimeButton.addActionListener { e: ActionEvent? -> updateCurrentDateTime() }
            controlsPanel.add(refreshDateAndTimeButton)
            val hideToolWindowButton = JButton("Hide")
            hideToolWindowButton.addActionListener { e: ActionEvent? -> toolWindow.hide(null) }
            controlsPanel.add(hideToolWindowButton)
            return controlsPanel
        }

        private fun updateCurrentDateTime() {
            val calendar: Calendar = Calendar.getInstance()
            currentDate.setText(getCurrentDate(calendar))
            timeZone.setText(getTimeZone(calendar))
            currentTime.setText(getCurrentTime(calendar))
        }

        private fun getCurrentDate(calendar: Calendar): String {
            return ((calendar.get(Calendar.DAY_OF_MONTH).toString() + "/"
                    + (calendar.get(Calendar.MONTH) + 1)) + "/"
                    + calendar.get(Calendar.YEAR))
        }

        private fun getTimeZone(calendar: Calendar): String {
            val gmtOffset: Int = calendar.get(Calendar.ZONE_OFFSET) // offset from GMT in milliseconds
            val gmtOffsetString = (gmtOffset / 3600000).toString()
            return if (gmtOffset > 0) "GMT + $gmtOffsetString" else "GMT - $gmtOffsetString"
        }

        private fun getCurrentTime(calendar: Calendar): String {
            return getFormattedValue(calendar, Calendar.HOUR_OF_DAY) + ":" + getFormattedValue(
                calendar,
                Calendar.MINUTE
            )
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