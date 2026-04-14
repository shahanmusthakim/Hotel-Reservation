//Composite Leaf
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation implements BookingComponent {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private Customer customer;
    private Room room;
    private LocalDate checkIn, checkOut;
    private PricingStrategy pricingStrategy;

    public Reservation(String reservationId, Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId; this.customer = customer; this.room = room; 
        this.checkIn = checkIn; this.checkOut = checkOut;
        this.pricingStrategy = new StandardPricing(); // Default strategy
    }

    public void setPricingStrategy(PricingStrategy strategy) { this.pricingStrategy = strategy; }
    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }

    @Override public double getTotalPrice() {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricingStrategy.calculatePrice(room.getPricePerNight(), nights > 0 ? nights : 1);
    }

    @Override public void displayBookingDetails() { System.out.println(this.toString()); }
    @Override public String toString() { return reservationId + " : " + customer.getName() + " -> Room " + room.getRoomId(); }
}