import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel extends JPanel implements DataObserver {
    private JTextField txtId = new JTextField("242-"), txtName = new JTextField(), txtPhone = new JTextField(), txtEmail = new JTextField();
    private JTextField txtSearch = new JTextField(20); 
    private DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Phone", "Email"}, 0);
    private JTable table = new JTable(model);

    public CustomerPanel() {
        setLayout(new BorderLayout()); setOpaque(false); DataStore.getInstance().addObserver(this);
        UIFactory.styleTextField(txtId); UIFactory.styleTextField(txtName); UIFactory.styleTextField(txtPhone); UIFactory.styleTextField(txtEmail); UIFactory.styleTextField(txtSearch);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10)); form.setOpaque(false); form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.add(new JLabel("ID:")); form.add(txtId); form.add(new JLabel("Name:")); form.add(txtName);
        form.add(new JLabel("Phone:")); form.add(txtPhone); form.add(new JLabel("Email:")); form.add(txtEmail);
        
        JPanel btns = new JPanel(); btns.setOpaque(false); Color gold = new Color(212, 175, 55);
        
        // --- ADD BUTTON WITH CLEAR FORM FIX ---
        btns.add(UIFactory.createAnimatedButton("Add", gold, () -> {
            DataStore.getInstance().addCustomer(new Customer(txtId.getText(), txtName.getText(), txtPhone.getText(), txtEmail.getText()));
            // Clear the form after adding
            txtId.setText("242-");
            txtName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
        }));
        
        btns.add(UIFactory.createAnimatedButton("Remove", gold, () -> { int r = table.getSelectedRow(); if(r != -1) DataStore.getInstance().removeCustomer(DataStore.getInstance().getCustomers().get(r)); }));
        btns.add(UIFactory.createAnimatedButton("Undo Trash", gold, () -> DataStore.getInstance().undoCustomerChange()));

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

    @Override public void onDataChanged() {
        model.setRowCount(0); String query = txtSearch.getText().toLowerCase();
        for(Customer c : DataStore.getInstance().getCustomers()) {
            if (c.getName().toLowerCase().contains(query) || c.getCustomerId().toLowerCase().contains(query) || c.getPhone().toLowerCase().contains(query)) model.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getPhone(), c.getEmail()});
        }
    }
}