//Facade Pattern
import java.time.LocalDate;

public class ReservationFacade {
    private DataStore db = DataStore.getInstance();
    private PaymentProcessor paymentProcessor = new BkashAdapter();

    public boolean bookRoom(Customer c, Room r, LocalDate in, LocalDate out) {
        if (!out.isAfter(in)) return false;
        
        Reservation res = new Reservation("RES-" + System.currentTimeMillis(), c, r, in, out);
        
        // Process payment adapter
        if (paymentProcessor.processPayment(res.getTotalPrice())) {
            db.addPendingReservation(res);
            try { db.saveAll("hoteldata.dat"); } catch (Exception ignored) {}
            return true;
        }
        return false;
    }

    public void confirmBooking(String resId) {
        Reservation r = db.getPendingReservations().stream().filter(x -> x.getReservationId().equals(resId)).findFirst().orElse(null);
        if (r != null) {
            db.getPendingReservations().remove(r);
            db.addConfirmedReservation(r);
            r.getRoom().setStatus(Room.RoomStatus.BOOKED);
            db.notifyObservers();
        }
    }
}