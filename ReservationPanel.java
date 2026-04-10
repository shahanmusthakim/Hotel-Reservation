import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ReservationPanel extends JPanel implements DataObserver {
    private JComboBox<Customer> cbCustomer = new JComboBox<>();
    private JComboBox<Room> cbRoom = new JComboBox<>();
    private DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Customer", "Room"}, 0);
    private JTable table = new JTable(model);
    private ReservationFacade facade = new ReservationFacade(); // Facade Instance

    public ReservationPanel() {
        setLayout(new BorderLayout()); setOpaque(false); DataStore.getInstance().addObserver(this);
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10)); form.setOpaque(false);
        form.add(new JLabel("Customer:")); form.add(cbCustomer); form.add(new JLabel("Room:")); form.add(cbRoom);
        
        JPanel btns = new JPanel(); btns.setOpaque(false); Color gold = new Color(212, 175, 55);
        btns.add(UIFactory.createAnimatedButton("Book via bKash", gold, () -> {
            boolean success = facade.bookRoom((Customer)cbCustomer.getSelectedItem(), (Room)cbRoom.getSelectedItem(), LocalDate.now(), LocalDate.now().plusDays(1));
            if(success) JOptionPane.showMessageDialog(this, "Booked and Paid!"); else JOptionPane.showMessageDialog(this, "Booking Failed.");
        }));
        btns.add(UIFactory.createAnimatedButton("Confirm Check-In", gold, () -> {
            int r = table.getSelectedRow(); if(r != -1) facade.confirmBooking(model.getValueAt(r, 0).toString());
        }));

        add(form, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(btns, BorderLayout.SOUTH); onDataChanged();
    }

    @Override public void onDataChanged() {
        cbCustomer.removeAllItems(); cbRoom.removeAllItems(); model.setRowCount(0);
        for(Customer c : DataStore.getInstance().getCustomers()) cbCustomer.addItem(c);
        for(Room r : DataStore.getInstance().getRooms()) if(r.getStatus() == Room.RoomStatus.AVAILABLE) cbRoom.addItem(r);
        for(Reservation res : DataStore.getInstance().getPendingReservations()) model.addRow(new Object[]{res.getReservationId(), res.getCustomer().getName(), res.getRoom().getRoomId()});
    }
}