import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UIFactory {
    public static JButton createAnimatedButton(String text, Color accentColor, Runnable action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(accentColor.darker());
                else if (getModel().isRollover()) g2.setColor(accentColor.brighter());
                else g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g); g2.dispose();
            }
        };
        btn.setForeground(Color.WHITE); btn.setFont(new Font("Segoe UI", Font.BOLD, 15)); btn.setFocusPainted(false);
        btn.setContentAreaFilled(false); btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setPreferredSize(new Dimension(220, 40));
        btn.addActionListener(e -> action.run()); return btn;
    }

    public static void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15)); field.setBackground(new Color(40, 40, 40));
        field.setForeground(Color.WHITE); field.setCaretColor(new Color(212, 175, 55));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 1), new EmptyBorder(5, 10, 5, 10)));
    }

    public static void styleTable(JTable table, JScrollPane scrollPane) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); table.setRowHeight(35); table.setBackground(new Color(30, 30, 30));
        table.setForeground(Color.WHITE); table.setGridColor(new Color(60, 60, 60)); table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(212, 175, 55, 150)); table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader(); header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(20, 20, 20)); header.setForeground(new Color(212, 175, 55)); header.setBorder(new EmptyBorder(0, 0, 0, 0));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        scrollPane.getViewport().setBackground(new Color(30, 30, 30)); scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
    }
}