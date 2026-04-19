import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class ReservationPanel extends JPanel implements DataObserver {
    private JComboBox<Customer> cbCustomer = new JComboBox<>();
    private JComboBox<Room> cbRoom = new JComboBox<>();
    private JComboBox<String> cbPayment = new JComboBox<>(new String[]{"Cash", "Card"}); 
    private JTextField txtSearch = new JTextField(20);
    
    private DefaultTableModel modelActive = new DefaultTableModel(new Object[]{"ID", "Customer", "Room", "Status"}, 0);
    private JTable tableActive = new JTable(modelActive);
    private DefaultTableModel modelHistory = new DefaultTableModel(new Object[]{"ID", "Customer", "Room", "Check-Out Date"}, 0);
    private JTable tableHistory = new JTable(modelHistory);
    private ReservationFacade facade = new ReservationFacade();

    private CardLayout cardLayout = new CardLayout();
    private JPanel tableContainer = new JPanel(cardLayout);
    private boolean showingActive = true;

    public ReservationPanel() {
        setLayout(new BorderLayout()); setOpaque(false); DataStore.getInstance().addObserver(this); UIFactory.styleTextField(txtSearch);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        form.add(new JLabel("Customer:")); form.add(cbCustomer); form.add(new JLabel("Room:")); form.add(cbRoom); form.add(new JLabel("Payment Method:")); form.add(cbPayment);
        
        JPanel btns = new JPanel(); btns.setOpaque(false); Color gold = new Color(212, 175, 55);
        
        btns.add(UIFactory.createAnimatedButton("Book Room", gold, () -> {
            Customer c = (Customer)cbCustomer.getSelectedItem(); Room r = (Room)cbRoom.getSelectedItem(); if (c == null || r == null) return;
            PaymentProcessor processor = cbPayment.getSelectedItem().equals("Cash") ? new CashPayment() : new CardPaymentAdapter();
            boolean success = facade.bookRoom(c, r, LocalDate.now(), LocalDate.now().plusDays(1), processor);
            if(success) { JOptionPane.showMessageDialog(this, "Booked Successfully via " + cbPayment.getSelectedItem() + "!"); cbPayment.setSelectedIndex(0); }
            else JOptionPane.showMessageDialog(this, "Booking Failed.");
        }));
        
        btns.add(UIFactory.createAnimatedButton("Confirm Check-In", gold, () -> { int r = tableActive.getSelectedRow(); if(r != -1) facade.confirmBooking(modelActive.getValueAt(r, 0).toString()); }));
        btns.add(UIFactory.createAnimatedButton("Check-Out", new Color(220, 53, 69), () -> { 
            int r = tableActive.getSelectedRow(); 
            if(r != -1) { facade.checkOutRoom(modelActive.getValueAt(r, 0).toString()); JOptionPane.showMessageDialog(this, "Customer Checked Out."); }
        }));

        tableContainer.setOpaque(false);
        JScrollPane scrollActive = new JScrollPane(tableActive); UIFactory.styleTable(tableActive, scrollActive); 
        JScrollPane scrollHistory = new JScrollPane(tableHistory); UIFactory.styleTable(tableHistory, scrollHistory); 
        tableContainer.add(scrollActive, "ACTIVE"); tableContainer.add(scrollHistory, "HISTORY");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); searchPanel.setOpaque(false);
        
        // FIX: Reverted to a standard JButton and set the text color to BLACK so it is visible!
        JButton btnToggleView = new JButton("View History ➔");
        btnToggleView.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnToggleView.setForeground(Color.BLACK); // Text color explicitly set to black
        btnToggleView.setFocusPainted(false);
        btnToggleView.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnToggleView.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnToggleView.addActionListener(e -> {
            showingActive = !showingActive;
            if (showingActive) {
                cardLayout.show(tableContainer, "ACTIVE");
                btnToggleView.setText("View History ➔");
            } else {
                cardLayout.show(tableContainer, "HISTORY");
                btnToggleView.setText("⬅ View Active");
            }
        });

        JLabel lblSearch = new JLabel("Search:"); lblSearch.setForeground(gold); 
        searchPanel.add(btnToggleView); searchPanel.add(Box.createHorizontalStrut(20)); searchPanel.add(lblSearch); searchPanel.add(txtSearch);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onDataChanged(); } public void removeUpdate(DocumentEvent e) { onDataChanged(); } public void changedUpdate(DocumentEvent e) { onDataChanged(); }
        });

        JPanel centerPanel = new JPanel(new BorderLayout()); centerPanel.setOpaque(false);
        centerPanel.add(searchPanel, BorderLayout.NORTH); centerPanel.add(tableContainer, BorderLayout.CENTER);

        add(form, BorderLayout.NORTH); add(centerPanel, BorderLayout.CENTER); add(btns, BorderLayout.SOUTH); onDataChanged();
    }

    @Override public void onDataChanged() {
        cbCustomer.removeAllItems(); cbRoom.removeAllItems(); modelActive.setRowCount(0); modelHistory.setRowCount(0); String query = txtSearch.getText().toLowerCase();
        for(Customer c : DataStore.getInstance().getCustomers()) cbCustomer.addItem(c);
        DataStore.getInstance().getRooms().stream().sorted(Comparator.comparing(Room::getRoomId)).filter(r -> r.getStatus() == Room.RoomStatus.AVAILABLE).forEach(cbRoom::addItem);
        
        cbCustomer.setSelectedIndex(-1); cbRoom.setSelectedIndex(-1);
        
        DataStore.getInstance().getPendingReservations().stream().filter(r -> r.getReservationId().toLowerCase().contains(query) || r.getCustomer().getName().toLowerCase().contains(query)).forEach(res -> modelActive.addRow(new Object[]{res.getReservationId(), res.getCustomer().getName(), res.getRoom().getRoomId(), "Pending"}));
        DataStore.getInstance().getConfirmedReservations().stream().filter(r -> r.getReservationId().toLowerCase().contains(query) || r.getCustomer().getName().toLowerCase().contains(query)).forEach(res -> modelActive.addRow(new Object[]{res.getReservationId(), res.getCustomer().getName(), res.getRoom().getRoomId(), "Checked-In"}));
        DataStore.getInstance().getHistoryReservations().stream().filter(r -> r.getReservationId().toLowerCase().contains(query) || r.getCustomer().getName().toLowerCase().contains(query)).forEach(res -> modelHistory.addRow(new Object[]{res.getReservationId(), res.getCustomer().getName(), res.getRoom().getRoomId(), LocalDate.now().toString()}));
    }
}