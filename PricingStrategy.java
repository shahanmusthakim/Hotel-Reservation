//Strategy Patter
import java.io.Serializable;

public interface PricingStrategy extends Serializable {
    double calculatePrice(double basePrice, long nights);
}

class StandardPricing implements PricingStrategy {
    public double calculatePrice(double basePrice, long nights) { return basePrice * nights; }
}

class OffSeasonDiscount implements PricingStrategy {
    public double calculatePrice(double basePrice, long nights) { return (basePrice * nights) * 0.85; } // 15% discount
}