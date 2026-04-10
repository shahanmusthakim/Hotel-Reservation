import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel extends JPanel implements DataObserver {
    private JTextField txtId = new JTextField("242-"), txtName = new JTextField(), txtPhone = new JTextField(), txtEmail = new JTextField();
    private DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Phone", "Email"}, 0);
    private JTable table = new JTable(model);

    public CustomerPanel() {
        setLayout(new BorderLayout()); setOpaque(false);
        DataStore.getInstance().addObserver(this); // Subscribe to Observer

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("ID:")); form.add(txtId); form.add(new JLabel("Name:")); form.add(txtName);
        form.add(new JLabel("Phone:")); form.add(txtPhone); form.add(new JLabel("Email:")); form.add(txtEmail);
        
        JPanel btns = new JPanel(); btns.setOpaque(false); Color gold = new Color(212, 175, 55);
        btns.add(UIFactory.createAnimatedButton("Add", gold, () -> DataStore.getInstance().addCustomer(new Customer(txtId.getText(), txtName.getText(), txtPhone.getText(), txtEmail.getText()))));
        btns.add(UIFactory.createAnimatedButton("Remove", gold, () -> {
            int r = table.getSelectedRow(); if(r != -1) DataStore.getInstance().removeCustomer(DataStore.getInstance().getCustomers().get(r));
        }));
        btns.add(UIFactory.createAnimatedButton("Undo Trash", gold, () -> DataStore.getInstance().undoCustomerChange())); // Trigger Memento
        
        add(form, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(btns, BorderLayout.SOUTH);
        onDataChanged();
    }

    @Override public void onDataChanged() {
        model.setRowCount(0);
        for(Customer c : DataStore.getInstance().getCustomers()) model.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getPhone(), c.getEmail()});
    }
}