//Composite Pattern Base
import java.io.Serializable;

public interface BookingComponent extends Serializable {
    double getTotalPrice();
    void displayBookingDetails();
}