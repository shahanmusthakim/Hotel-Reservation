import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Hotel Reservation System"); setSize(900, 600); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DataStore.getInstance(); 
        DataStore.loadAll("hoteldata.dat"); // Load via Singleton

        JPanel bgPanel = new JPanel() {
            Image bg = new ImageIcon("hotel_sketch.png").getImage();
            protected void paintComponent(Graphics g) { super.paintComponent(g); g.drawImage(bg, 0, 0, getWidth(), getHeight(), this); }
        };
        bgPanel.setLayout(null); setContentPane(bgPanel);

        JLabel title = new JLabel("Hotel Reservation System"); title.setFont(new Font("Segoe UI", Font.BOLD, 36)); 
        title.setForeground(new Color(212, 175, 55)); title.setBounds(250, 20, 500, 50); bgPanel.add(title);

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 30, 0)); btnPanel.setOpaque(false); btnPanel.setBounds(50, 120, 800, 150);
        Color gold = new Color(212, 175, 55);

        btnPanel.add(UIFactory.createAnimatedButton("Customers", gold, () -> new BaseFrame("Customers", new CustomerPanel()).setVisible(true)));
        btnPanel.add(UIFactory.createAnimatedButton("Rooms", gold, () -> new BaseFrame("Rooms", new RoomPanel()).setVisible(true)));
        btnPanel.add(UIFactory.createAnimatedButton("Reservations", gold, () -> new BaseFrame("Reservations", new ReservationPanel()).setVisible(true)));
        bgPanel.add(btnPanel); setVisible(true);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(MainFrame::new); }
}

class BaseFrame extends JFrame {
    public BaseFrame(String title, JPanel panel) {
        super(title); setSize(900, 600); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel bg = new JPanel(new BorderLayout()) {
            Image img = new ImageIcon("golden_background.png").getImage();
            protected void paintComponent(Graphics g) { super.paintComponent(g); g.drawImage(img, 0, 0, getWidth(), getHeight(), this); }
        };
        setContentPane(bg); add(panel, BorderLayout.CENTER);
    }
}