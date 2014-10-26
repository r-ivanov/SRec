package utilidades;

import javax.swing.JTable;
import java.awt.Component;
import java.awt.Dimension;
 
public class TableColumnSizer {
	
   public static int getColumnPreferredWidthToFit(final JTable table, final int columnIndex, final boolean includeColumnHeader,
                                           final boolean includeCellEditor) {
      int columnWidth = (includeColumnHeader ? getColumnHeaderPreferredWidth(table, columnIndex) : 0);
      for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
         columnWidth =
         Math.max(getColumnPreferredWidthAtRow(table, rowIndex, columnIndex, includeCellEditor), columnWidth);
      }
      return columnWidth;
   }
 
   private static int getColumnHeaderPreferredWidth(final JTable table, final int columnIndex) {
      return table.getTableHeader().getDefaultRenderer().
              getTableCellRendererComponent(table,
                                            table.getModel().getColumnName(columnIndex),
                                            false, false, 0, columnIndex).getPreferredSize().width + 2;
   }
 
   private static int getColumnPreferredWidthAtRow(final JTable table, final int rowIndex, final int columnIndex, final boolean includeCellEditor) {
      int width = table.getCellRenderer(rowIndex, columnIndex).
              getTableCellRendererComponent(table,
                                            table.getModel().getValueAt(rowIndex,
                                                                        getModelColumnIndex(table, columnIndex)),
                                            false,
                                            false,
                                            rowIndex,
                                            getModelColumnIndex(table, columnIndex)).getPreferredSize().width + 4;
      if (includeCellEditor && table.isCellEditable(rowIndex, columnIndex)) {
         final Component editor = table.getCellEditor(rowIndex, columnIndex).
                 getTableCellEditorComponent(table,
                                             table.getModel().getValueAt(rowIndex, getModelColumnIndex(table, columnIndex)),
                                             false, rowIndex, getModelColumnIndex(table, columnIndex));
         if (editor != null) {
            width = Math.max(width, editor.getPreferredSize().width);
         }
      }
      return width;
   }
 
   private static int getModelColumnIndex(final JTable table, final int columnIndex) {
      return table.getColumnModel().getColumn(columnIndex).getModelIndex();
   }
 
   public static Dimension getPreferredScrollableViewportSize(final JTable table, final int visibleRowCount) {
      final Dimension result = new Dimension(0, 0);
      for (int columnIndex = 0, columnCount = getMaxColumnCountForSizingColumns(table);
           columnIndex < columnCount; columnIndex++) {
         result.width +=
         getColumnPreferredWidthToFit(table, columnIndex, true,
                                      table.getRowCount() > 0 ?
                                      table.getModel().isCellEditable(0, columnIndex) : false);
      }
      result.height = visibleRowCount * table.getRowHeight();
      return result;
   }
 
   public static void setColumnsWidthToFit(final JTable table, final boolean includeColumnHeader, final boolean includeCellEditor) {
      for (int columnIndex = 0, columnCount = getMaxColumnCountForSizingColumns(table);
           columnIndex < columnCount; columnIndex++) {
         if (preferredWidthIsSettable(table, columnIndex)) {
            setColumnPreferredWidth(table, columnIndex,
                                    getColumnPreferredWidthToFit(table, columnIndex, includeColumnHeader,
                                                                 includeCellEditor));
         }
      }
   }
 
   private static int getMaxColumnCountForSizingColumns(final JTable table) {
      return Math.min(table.getColumnModel().getColumnCount(), table.getModel().getColumnCount());
   }
 
   private static boolean preferredWidthIsSettable(final JTable table, final int columnIndex) {
      return table.getColumnModel().getColumn(columnIndex).getMaxWidth() != 0;
   }
 
   private static void setColumnPreferredWidth(final JTable table, final int column, final int columnWidth) {
      table.getColumnModel().getColumn(column).setPreferredWidth(columnWidth);
   }
}
 
 