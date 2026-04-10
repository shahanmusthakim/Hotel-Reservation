import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RoomPanel extends JPanel implements DataObserver {
    private JTextField txtId = new JTextField(), txtPrice = new JTextField();
    private JComboBox<RoomType> cbType = new JComboBox<>(RoomType.values());
    private DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Type", "Price", "Status"}, 0);

    public RoomPanel() {
        setLayout(new BorderLayout()); setOpaque(false);
        DataStore.getInstance().addObserver(this);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("Room ID:")); form.add(txtId); form.add(new JLabel("Type:")); form.add(cbType); form.add(new JLabel("Price:")); form.add(txtPrice);
        
        JPanel btns = new JPanel(); btns.setOpaque(false);
        btns.add(UIFactory.createAnimatedButton("Add Room", new Color(212, 175, 55), () -> {
            // Using Factory Pattern
            Room newRoom = RoomFactory.createRoom(txtId.getText(), (RoomType) cbType.getSelectedItem(), Double.parseDouble(txtPrice.getText()));
            DataStore.getInstance().addRoom(newRoom);
        }));

        add(form, BorderLayout.NORTH); add(new JScrollPane(new JTable(model)), BorderLayout.CENTER); add(btns, BorderLayout.SOUTH);
        onDataChanged();
    }

    @Override public void onDataChanged() {
        model.setRowCount(0);
        for(Room r : DataStore.getInstance().getRooms()) model.addRow(new Object[]{r.getRoomId(), r.getType(), r.getPricePerNight(), r.getStatus()});
    }
}