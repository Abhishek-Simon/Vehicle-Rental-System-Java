import java.io.Serializable;
import java.util.UUID;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vehicleId;
    private String brand;
    private String model;
    private Category category;
    private double rentPerDay;
    private boolean isAvailable;
    private String extraInfo; // e.g., seats or engine capacity stored as text

    public Vehicle(String brand, String model, Category category, double rentPerDay, String extraInfo) {
        this.vehicleId = "V-" + UUID.randomUUID().toString().substring(0, 8);
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.rentPerDay = rentPerDay;
        this.isAvailable = true;
        this.extraInfo = extraInfo;
    }

    // getters and setters
    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Category getCategory() { return category; }
    public double getRentPerDay() { return rentPerDay; }
    public boolean isAvailable() { return isAvailable; }
    public String getExtraInfo() { return extraInfo; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setCategory(Category category) { this.category = category; }
    public void setRentPerDay(double rentPerDay) { this.rentPerDay = rentPerDay; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }

    public void displayShort() {
        System.out.printf("%-10s %-10s %-10s %-12s %-8.2f %-10s%n", vehicleId, brand, model, category, rentPerDay, (isAvailable ? "AVAILABLE" : "RENTED"));
    }

    public void displayFull() {
        System.out.println("Vehicle ID : " + vehicleId);
        System.out.println("Brand      : " + brand);
        System.out.println("Model      : " + model);
        System.out.println("Category   : " + category);
        System.out.println("Rent/day   : " + rentPerDay);
        System.out.println("Available  : " + (isAvailable ? "Yes" : "No"));
        System.out.println("Extra info : " + extraInfo);
    }

    @Override
    public String toString() {
        return vehicleId + " | " + brand + " " + model + " (" + category + ") - " + (isAvailable ? "AVAILABLE" : "RENTED") + " - ₹" + rentPerDay + "/day";
    }
}
