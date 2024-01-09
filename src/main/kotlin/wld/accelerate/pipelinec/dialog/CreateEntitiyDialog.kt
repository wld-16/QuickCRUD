package wld.accelerate.pipelinec.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.annotation.Nullable
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.table.DefaultTableModel


class CreateEntitiyDialog : DialogWrapper(true) {

    private var defaultTableModel = DefaultTableModel(arrayOf("name", "type"), 2)
    var jbTable: JBTable? = JBTable(defaultTableModel)
    var fieldName: JTextField? = JTextField("entity name")
    init {
        title = "Create Configuration"
        defaultTableModel = DefaultTableModel(arrayOf("name", "type"), 2)
        jbTable = JBTable(defaultTableModel)
        fieldName = JTextField("entity name")
        init()
    }

    @Nullable
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        val verticalFlowLayout = VerticalFlowLayout()
        val headerPanel = JPanel(verticalFlowLayout)

        val label = JLabel("Create Entity")


        headerPanel.add(label)
        headerPanel.add(fieldName)

        dialogPanel.add(headerPanel, BorderLayout.NORTH)

        defaultTableModel.setColumnIdentifiers(arrayOf("name","type"))

        jbTable!!.model = defaultTableModel

        jbTable!!.setShowColumns(true)
        jbTable!!.setShowGrid(true)

        jbTable!!.size = Dimension(this.size.width, 100)

        val removeButton = JButton("Remove Row")
        removeButton.addActionListener { defaultTableModel.removeRow(defaultTableModel.rowCount-1) }

        val addButton = JButton("Add Row")
        addButton.addActionListener { defaultTableModel.addRow(arrayOf("name","field")) }

        headerPanel.add(removeButton)
        dialogPanel.add(jbTable, BorderLayout.CENTER)
        dialogPanel.add(addButton, BorderLayout.SOUTH)

        return dialogPanel
    }
}