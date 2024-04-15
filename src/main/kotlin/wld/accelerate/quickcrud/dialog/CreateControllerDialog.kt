package wld.accelerate.quickcrud.dialog

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


class CreateControllerDialog : DialogWrapper(true) {

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

        defaultTableModel.setColumnIdentifiers(arrayOf("endpoint","isActive"))

        defaultTableModel.addRow(arrayOf("CREATE", true))
        defaultTableModel.addRow(arrayOf("READ", true))
        defaultTableModel.addRow(arrayOf("READ_ALL", true))
        defaultTableModel.addRow(arrayOf("UPDATE", true))
        defaultTableModel.addRow(arrayOf("DELETE", true))

        jbTable!!.model = defaultTableModel

        jbTable!!.setShowColumns(true)
        jbTable!!.setShowGrid(true)

        jbTable!!.size = Dimension(this.size.width, 100)

        dialogPanel.add(jbTable, BorderLayout.CENTER)

        return dialogPanel
    }
}