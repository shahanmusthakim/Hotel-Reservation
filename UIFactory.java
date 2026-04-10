import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UIFactory {
    public static JButton createAnimatedButton(String text, Color baseColor, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBackground(baseColor); btn.setForeground(Color.WHITE); btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btn.setOpaque(true); btn.setPreferredSize(new Dimension(220, 30));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(baseColor.brighter()); btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); }
            public void mousePressed(MouseEvent e) { btn.setBackground(baseColor.darker()); }
            public void mouseReleased(MouseEvent e) { btn.setBackground(baseColor.brighter()); }
        });
        btn.addActionListener(e -> action.run()); return btn;
    }
}