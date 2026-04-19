import java.io.Serializable;

public interface PaymentProcessor extends Serializable {
    boolean processPayment(double amount);
}

class CashPayment implements PaymentProcessor {
    @Override public boolean processPayment(double amount) {
        return true; 
    }
}

class ExternalCardAPI implements Serializable {
    public boolean chargeCard(double amount) {
        return true; 
    }
}

class CardPaymentAdapter implements PaymentProcessor {
    private ExternalCardAPI api = new ExternalCardAPI();
    @Override public boolean processPayment(double amount) { return api.chargeCard(amount); }
}