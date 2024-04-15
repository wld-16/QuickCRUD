package wld.accelerate.quickcrud.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.annotation.Nullable
import javax.swing.*
import javax.swing.table.DefaultTableModel


class CreateEnumDialog : DialogWrapper(true) {

    private var defaultTableModel = DefaultTableModel(arrayOf(), 0)
    var jbTable: JBTable? = JBTable(defaultTableModel)
    var fieldName: JTextField? = JTextField("entity name")
    init {
        title = "Create Configuration"
        defaultTableModel = DefaultTableModel(arrayOf(), 0)
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

        defaultTableModel.setColumnIdentifiers(arrayOf("name"))

        jbTable!!.model = defaultTableModel

        jbTable!!.setShowColumns(true)
        jbTable!!.setShowGrid(true)

        jbTable!!.size = Dimension(this.size.width, 100)

        val removeButton = JButton("Remove Row")
        removeButton.addActionListener { defaultTableModel.removeRow(defaultTableModel.rowCount-1) }

        val addButton = JButton("Add Row")
        addButton.addActionListener { defaultTableModel.addRow(arrayOf("name")) }

        headerPanel.add(removeButton)
        dialogPanel.add(jbTable, BorderLayout.CENTER)
        dialogPanel.add(addButton, BorderLayout.SOUTH)

        return dialogPanel
    }
}