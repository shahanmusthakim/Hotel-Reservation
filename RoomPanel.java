import java.awt.*;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class RoomPanel extends JPanel implements DataObserver {
    private JTextField txtId = new JTextField(), txtPrice = new JTextField();
    private JTextField txtSearch = new JTextField(20);
    private JComboBox<RoomType> cbType = new JComboBox<>(RoomType.values());
    private DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Type", "Price", "Status"}, 0);
    private JTable table = new JTable(model);
    private boolean editMode = false; private String editingRoomId = null; private int roomCounter = 0;

    public RoomPanel() {
        setLayout(new BorderLayout()); setOpaque(false); DataStore.getInstance().addObserver(this); updateCounter();
        txtId.setText(generateNextRoomId()); txtId.setEditable(false); 
        UIFactory.styleTextField(txtId); UIFactory.styleTextField(txtPrice); UIFactory.styleTextField(txtSearch);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Room ID:")); form.add(txtId); form.add(new JLabel("Type:")); form.add(cbType); form.add(new JLabel("Price:")); form.add(txtPrice);
        
        JPanel btns = new JPanel(); btns.setOpaque(false); Color gold = new Color(212, 175, 55);
        btns.add(UIFactory.createAnimatedButton("Add / Save Room", gold, this::addOrSaveRoom));
        btns.add(UIFactory.createAnimatedButton("Edit Room", gold, () -> {
            int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this, "Select a room."); return; }
            editingRoomId = model.getValueAt(r, 0).toString();
            Room room = DataStore.getInstance().getRooms().stream().filter(x -> x.getRoomId().equals(editingRoomId)).findFirst().orElse(null);
            if (room != null) { txtId.setText(room.getRoomId()); cbType.setSelectedItem(room.getType()); txtPrice.setText(String.valueOf(room.getPricePerNight())); editMode = true; }
        }));
        btns.add(UIFactory.createAnimatedButton("Remove Room", new Color(220, 53, 69), () -> {
            int r = table.getSelectedRow(); 
            if(r != -1) {
                String id = model.getValueAt(r, 0).toString();
                DataStore.getInstance().getRooms().removeIf(room -> room.getRoomId().equals(id));
                DataStore.getInstance().getPendingReservations().removeIf(res -> res.getRoom().getRoomId().equals(id));
                DataStore.getInstance().getConfirmedReservations().removeIf(res -> res.getRoom().getRoomId().equals(id));
                DataStore.getInstance().saveDataSilently(); DataStore.getInstance().notifyObservers(); updateCounter(); clearForm();
            }
        }));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); searchPanel.setOpaque(false);
        JLabel lblSearch = new JLabel("Search:"); lblSearch.setForeground(gold); searchPanel.add(lblSearch); searchPanel.add(txtSearch);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { onDataChanged(); } public void removeUpdate(DocumentEvent e) { onDataChanged(); } public void changedUpdate(DocumentEvent e) { onDataChanged(); }
        });

        JPanel centerPanel = new JPanel(new BorderLayout()); centerPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(table); UIFactory.styleTable(table, scrollPane);
        centerPanel.add(searchPanel, BorderLayout.NORTH); centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(form, BorderLayout.NORTH); add(centerPanel, BorderLayout.CENTER); add(btns, BorderLayout.SOUTH); onDataChanged();
    }

    private void updateCounter() {
        int max = 0; for (Room r : DataStore.getInstance().getRooms()) { try { int num = Integer.parseInt(r.getRoomId()); if (num > max) max = num; } catch (Exception ignored) {} } this.roomCounter = max;
    }
    private String generateNextRoomId() { return String.format("%03d", roomCounter + 1); }
    private void clearForm() { txtId.setText(generateNextRoomId()); txtPrice.setText(""); cbType.setSelectedIndex(0); editMode = false; editingRoomId = null; }
    
    private void addOrSaveRoom() {
        try {
            double price = Double.parseDouble(txtPrice.getText());
            if (editMode) { DataStore.getInstance().getRooms().removeIf(r -> r.getRoomId().equals(editingRoomId)); DataStore.getInstance().addRoom(RoomFactory.createRoom(editingRoomId, (RoomType) cbType.getSelectedItem(), price)); } 
            else { DataStore.getInstance().addRoom(RoomFactory.createRoom(txtId.getText(), (RoomType) cbType.getSelectedItem(), price)); updateCounter(); }
            DataStore.getInstance().saveDataSilently(); clearForm();
        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Valid price needed."); }
    }

    @Override public void onDataChanged() {
        model.setRowCount(0); String query = txtSearch.getText().toLowerCase();
        DataStore.getInstance().getRooms().stream().sorted(Comparator.comparing(Room::getRoomId)).filter(r -> r.getRoomId().toLowerCase().contains(query) || r.getType().toString().toLowerCase().contains(query))
            .forEach(r -> model.addRow(new Object[]{r.getRoomId(), r.getType(), r.getPricePerNight(), r.getStatus()}));
    }
}