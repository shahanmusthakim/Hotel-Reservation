//Memento Pattern
import java.util.*;

public class DataStoreMemento {
    private final List<Customer> backupCustomers;
    public DataStoreMemento(List<Customer> customers) { this.backupCustomers = new ArrayList<>(customers); }
    public List<Customer> getSavedCustomers() { return backupCustomers; }
}