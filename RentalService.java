import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class RentalService {
    private static final String RENT_FILE = "rentals.dat";
    private List<Rental> rentals;
    private VehicleService vehicleService;
    private CustomerService customerService;

    // fine per extra day (fixed); you can adjust or make dynamic
    private static final double LATE_FEE_PER_DAY = 500.0;

    public RentalService(VehicleService vs, CustomerService cs) {
        this.vehicleService = vs;
        this.customerService = cs;
        rentals = FileHandler.loadList(RENT_FILE);
        if (rentals == null) rentals = new java.util.ArrayList<>();
    }

    public void save() {
        FileHandler.saveList(rentals, RENT_FILE);
    }

    public boolean rentVehicle(String vehicleId, String customerId, LocalDate from, LocalDate to) {
        Vehicle v = vehicleService.findById(vehicleId);
        Customer c = customerService.findById(customerId);
        if (v == null || c == null) return false;
        if (!v.isAvailable()) return false;
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        if (days <= 0) return false;
        double amount = days * v.getRentPerDay();
        Rental r = new Rental(vehicleId, customerId, from, to, amount);
        rentals.add(r);
        v.setAvailable(false);
        vehicleService.save();
        save();
        generateBillFile(r);
        return true;
    }

    public boolean returnVehicle(String rentalId, LocalDate actualReturnDate) {
        Rental r = findById(rentalId);
        if (r == null || r.isReturned()) return false;
        Vehicle v = vehicleService.findById(r.getVehicleId());
        if (v == null) return false;
        // compute extra days
        long expectedDays = ChronoUnit.DAYS.between(r.getRentDate(), r.getReturnDateExpected()) + 1;
        long actualDays = ChronoUnit.DAYS.between(r.getRentDate(), actualReturnDate) + 1;
        double extra = 0.0;
        if (actualDays > expectedDays) {
            long extraDays = actualDays - expectedDays;
            extra = extraDays * LATE_FEE_PER_DAY;
        }
        r.returnVehicle(actualReturnDate, extra);
        v.setAvailable(true);
        vehicleService.save();
        save();
        generateBillFile(r);
        return true;
    }

    public Rental findById(String id) {
        return rentals.stream().filter(r -> r.getRentalId().equals(id)).findFirst().orElse(null);
    }

    public List<Rental> listAll() {
        return rentals;
    }

    public List<Rental> listActive() {
        return rentals.stream().filter(r -> !r.isReturned()).collect(Collectors.toList());
    }

    public double totalEarnings() {
        return rentals.stream().mapToDouble(Rental::getTotalAmount).sum();
    }

    private void generateBillFile(Rental r) {
        // create a human-readable bill file named bill_<rentalId>.txt
        String fileName = "bill_" + r.getRentalId() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(fileName)))) {
            pw.println("===== VEHICLE RENTAL BILL =====");
            pw.println(r.toBillString());
            Vehicle v = vehicleService.findById(r.getVehicleId());
            if (v != null) {
                pw.println("----- VEHICLE DETAILS -----");
                pw.println(v.toString());
            }
            Customer c = customerService.findById(r.getCustomerId());
            if (c != null) {
                pw.println("----- CUSTOMER DETAILS -----");
                pw.println(c.toString());
            }
            pw.flush();
        } catch (Exception e) {
            System.err.println("Failed to write bill file: " + e.getMessage());
        }
    }
}
