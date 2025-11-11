import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;

    private String rentalId;
    private String vehicleId;
    private String customerId;
    private LocalDate rentDate;
    private LocalDate returnDateExpected;
    private LocalDate actualReturnDate; // null until returned
    private double totalAmount; // calculated at rent time or on return
    private boolean isReturned;

    public Rental(String vehicleId, String customerId, LocalDate rentDate, LocalDate returnDateExpected, double totalAmount) {
        this.rentalId = "R-" + UUID.randomUUID().toString().substring(0, 8);
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.rentDate = rentDate;
        this.returnDateExpected = returnDateExpected;
        this.totalAmount = totalAmount;
        this.isReturned = false;
        this.actualReturnDate = null;
    }

    // getters & setters
    public String getRentalId() { return rentalId; }
    public String getVehicleId() { return vehicleId; }
    public String getCustomerId() { return customerId; }
    public LocalDate getRentDate() { return rentDate; }
    public LocalDate getReturnDateExpected() { return returnDateExpected; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isReturned() { return isReturned; }

    public void returnVehicle(LocalDate actualReturnDate, double additionalAmount) {
        this.actualReturnDate = actualReturnDate;
        this.totalAmount += additionalAmount;
        this.isReturned = true;
    }

    public long daysRented() {
        LocalDate to = isReturned && actualReturnDate != null ? actualReturnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(rentDate, to) + 1; // inclusive
    }

    public void displayShort() {
        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-8.2f %-8s%n",
                rentalId, vehicleId, customerId,
                rentDate.toString(), (isReturned && actualReturnDate != null) ? actualReturnDate.toString() : returnDateExpected.toString(),
                totalAmount, (isReturned ? "RETURNED" : "ONGOING"));
    }

    public String toBillString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rental ID: ").append(rentalId).append("\n");
        sb.append("Vehicle ID: ").append(vehicleId).append("\n");
        sb.append("Customer ID: ").append(customerId).append("\n");
        sb.append("Rent Date: ").append(rentDate).append("\n");
        sb.append("Expected Return Date: ").append(returnDateExpected).append("\n");
        if (isReturned && actualReturnDate != null) {
            sb.append("Actual Return Date: ").append(actualReturnDate).append("\n");
        }
        sb.append("Total Amount: ₹").append(String.format("%.2f", totalAmount)).append("\n");
        sb.append("Status: ").append(isReturned ? "RETURNED" : "ONGOING").append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return rentalId + " | Vehicle:" + vehicleId + " | Customer:" + customerId + " | Rent:" + rentDate + " | ExpectedReturn:" + returnDateExpected + " | Amount: ₹" + totalAmount + " | " + (isReturned ? "RETURNED" : "ONGOING");
    }
}
