import java.time.LocalDate;

public class ReservationFacade {
    private DataStore db = DataStore.getInstance();

    public boolean bookRoom(Customer c, Room r, LocalDate in, LocalDate out, PaymentProcessor paymentProcessor) {
        if (!out.isAfter(in)) return false;
        
        Reservation res = new Reservation("RES-" + System.currentTimeMillis(), c, r, in, out);
        
        if (paymentProcessor.processPayment(res.getTotalPrice())) {
            r.setStatus(Room.RoomStatus.BOOKED); 
            db.addPendingReservation(res);
            db.saveDataSilently();
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

    public void checkOutRoom(String resId) {
        Reservation r = db.getConfirmedReservations().stream().filter(x -> x.getReservationId().equals(resId)).findFirst().orElse(null);
        if (r == null) r = db.getPendingReservations().stream().filter(x -> x.getReservationId().equals(resId)).findFirst().orElse(null);
        
        if (r != null) {
            db.getConfirmedReservations().remove(r);
            db.getPendingReservations().remove(r);
            r.getRoom().setStatus(Room.RoomStatus.AVAILABLE); 
            db.getHistoryReservations().add(r); 
            db.saveDataSilently();
            db.notifyObservers();
        }
    }
}