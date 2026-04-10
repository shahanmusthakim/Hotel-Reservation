//Singleton Pattern + Observer Subject
import java.io.*;
import java.util.*;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DataStore instance; // Singleton

    private List<Room> rooms = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Reservation> pendingReservations = new ArrayList<>();
    private List<Reservation> confirmedReservations = new ArrayList<>();
    private transient List<DataObserver> observers = new ArrayList<>();
    private transient Stack<DataStoreMemento> undoStack = new Stack<>(); // Memento history

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) { instance = new DataStore(); }
        return instance;
    }

    public void addObserver(DataObserver o) { 
        if (observers == null) observers = new ArrayList<>(); 
        observers.add(o); 
    }
    
    public void notifyObservers() {
        if (observers != null) { for (DataObserver o : observers) o.onDataChanged(); }
    }

    public void saveCustomerState() { undoStack.push(new DataStoreMemento(this.customers)); }
    public void undoCustomerChange() {
        if (!undoStack.isEmpty()) { this.customers = undoStack.pop().getSavedCustomers(); notifyObservers(); }
    }

    public List<Room> getRooms() { return rooms; }
    public List<Customer> getCustomers() { return customers; }
    public List<Reservation> getPendingReservations() { return pendingReservations; }
    public List<Reservation> getConfirmedReservations() { return confirmedReservations; }

    public void addRoom(Room r) { rooms.add(r); notifyObservers(); }
    public void addCustomer(Customer c) { saveCustomerState(); customers.add(c); notifyObservers(); }
    public void removeCustomer(Customer c) { saveCustomerState(); customers.remove(c); notifyObservers(); }
    
    public void addPendingReservation(Reservation r) { pendingReservations.add(r); notifyObservers(); }
    public void addConfirmedReservation(Reservation r) { confirmedReservations.add(r); notifyObservers(); }

    public void saveAll(String file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) { oos.writeObject(this); }
    }

    public static void loadAll(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DataStore loaded = (DataStore) ois.readObject();
            instance.rooms = loaded.rooms; instance.customers = loaded.customers;
            instance.pendingReservations = loaded.pendingReservations; instance.confirmedReservations = loaded.confirmedReservations;
        } catch (Exception ignored) {}
    }
}