import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    // NEW: We use CardLayout to swap screens inside the same window
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    public MainFrame() {
        super("Hotel Reservation System"); 
        setSize(1000, 700); // Made it slightly larger for the single-window feel
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DataStore.getInstance(); 
        DataStore.loadAll("hoteldata.dat");

        // --- NEW: TOP NAVIGATION BAR ---
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navBar.setBackground(new Color(20, 20, 20)); // Very dark grey
        Color gold = new Color(212, 175, 55);

        // Buttons trigger the CardLayout to "show" the specific panel
        JButton btnHome = UIFactory.createAnimatedButton("Home", gold, () -> cardLayout.show(mainContainer, "HOME"));
        JButton btnCust = UIFactory.createAnimatedButton("Customers", gold, () -> cardLayout.show(mainContainer, "CUSTOMERS"));
        JButton btnRoom = UIFactory.createAnimatedButton("Rooms", gold, () -> cardLayout.show(mainContainer, "ROOMS"));
        JButton btnRes = UIFactory.createAnimatedButton("Reservations", gold, () -> cardLayout.show(mainContainer, "RESERVATIONS"));

        navBar.add(btnHome); navBar.add(btnCust); navBar.add(btnRoom); navBar.add(btnRes);

        // --- THE HOME SCREEN ---
        JPanel homePanel = new JPanel() {
            Image bg = new ImageIcon("hotel_sketch.png").getImage();
            protected void paintComponent(Graphics g) { 
                super.paintComponent(g); 
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this); 
            }
        };
        homePanel.setLayout(null);
        JLabel title = new JLabel("Hotel Reservation System", SwingConstants.CENTER); 
        title.setFont(new Font("Segoe UI", Font.BOLD, 48)); 
        title.setForeground(gold); 
        title.setBounds(0, 250, 1000, 60); 
        homePanel.add(title);

        // --- ADDING PANELS TO THE "DECK OF CARDS" ---
        mainContainer.add(homePanel, "HOME");
        // We wrap your panels in a method to keep your golden_background.png active
        mainContainer.add(createPanelWithBackground(new CustomerPanel()), "CUSTOMERS");
        mainContainer.add(createPanelWithBackground(new RoomPanel()), "ROOMS");
        mainContainer.add(createPanelWithBackground(new ReservationPanel()), "RESERVATIONS");

        // Assemble the final layout
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH); // Nav bar stays stuck to the top
        add(mainContainer, BorderLayout.CENTER); // Main content changes in the center

        setVisible(true);
    }

    // Helper method to keep your background image on all the child panels
    private JPanel createPanelWithBackground(JPanel contentPanel) {
        JPanel bgPanel = new JPanel(new BorderLayout()) {
            Image img = new ImageIcon("golden_background.png").getImage();
            protected void paintComponent(Graphics g) { 
                super.paintComponent(g); 
                if (img != null) g.drawImage(img, 0, 0, getWidth(), getHeight(), this); 
            }
        };
        bgPanel.add(contentPanel, BorderLayout.CENTER);
        return bgPanel;
    }
    
    public static void main(String[] args) { 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font modernFont = new Font("Segoe UI", Font.PLAIN, 15);
            UIManager.put("Label.font", modernFont); UIManager.put("Button.font", modernFont);
            UIManager.put("TextField.font", modernFont); UIManager.put("ComboBox.font", modernFont);
            UIManager.put("OptionPane.messageFont", modernFont); UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
            
            UIManager.put("Label.foreground", new Color(212, 175, 55)); 
            UIManager.put("OptionPane.background", new Color(40, 40, 40)); UIManager.put("Panel.background", new Color(40, 40, 40));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
        } catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(MainFrame::new); 
    }
}