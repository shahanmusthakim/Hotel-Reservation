//Adapter Pattern
// Save this entire block in PaymentProcessor.java
interface PaymentProcessor {
    boolean processPayment(double amount);
}

class ExternalBkashAPI {
    public boolean makeTransfer(double bdtAmount) {
        System.out.println("Processing BDT " + bdtAmount + " via bKash...");
        return true; // Assume success
    }
}

class BkashAdapter implements PaymentProcessor {
    private ExternalBkashAPI api = new ExternalBkashAPI();
    @Override public boolean processPayment(double amount) {
        return api.makeTransfer(amount);
    }
}