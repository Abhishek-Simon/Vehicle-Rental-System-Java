import java.util.*;
import java.util.stream.Collectors;

public class VehicleService {
    private static final String VEH_FILE = "vehicles.dat";
    private List<Vehicle> vehicles;

    public VehicleService() {
        vehicles = FileHandler.loadList(VEH_FILE);
        if (vehicles == null) vehicles = new ArrayList<>();
    }

    public void save() {
        FileHandler.saveList(vehicles, VEH_FILE);
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
        save();
    }

    public boolean updateVehicle(String vehicleId, String brand, String model, Category category, double rentPerDay, String extraInfo) {
        Vehicle v = findById(vehicleId);
        if (v == null) return false;
        v.setBrand(brand);
        v.setModel(model);
        v.setCategory(category);
        v.setRentPerDay(rentPerDay);
        v.setExtraInfo(extraInfo);
        save();
        return true;
    }

    public boolean deleteVehicle(String vehicleId) {
        Vehicle v = findById(vehicleId);
        if (v == null) return false;
        vehicles.remove(v);
        save();
        return true;
    }

    public List<Vehicle> listAll() {
        return vehicles;
    }

    public Vehicle findById(String id) {
        return vehicles.stream().filter(v -> v.getVehicleId().equals(id)).findFirst().orElse(null);
    }

    public List<Vehicle> searchByBrand(String brand) {
        return vehicles.stream().filter(v -> v.getBrand().equalsIgnoreCase(brand)).collect(Collectors.toList());
    }

    public List<Vehicle> filterByCategory(Category category) {
        return vehicles.stream().filter(v -> v.getCategory() == category).collect(Collectors.toList());
    }

    public List<Vehicle> listAvailable() {
        return vehicles.stream().filter(Vehicle::isAvailable).collect(Collectors.toList());
    }

    public boolean setAvailability(String vehicleId, boolean available) {
        Vehicle v = findById(vehicleId);
        if (v == null) return false;
        v.setAvailable(available);
        save();
        return true;
    }

    public void seedSampleDataIfEmpty() {
        if (!vehicles.isEmpty()) return;
        vehicles.add(new Vehicle("Hero", "Splendor", Category.BIKES, 400, "100cc"));
        vehicles.add(new Vehicle("Yamaha", "R15", Category.SUPERBIKES, 1500, "155cc"));
        vehicles.add(new Vehicle("MG", "Hector", Category.XUVS, 2500, "5 seats"));
        vehicles.add(new Vehicle("Hyundai", "Verna", Category.SEDAN, 2000, "4 seats"));
        vehicles.add(new Vehicle("Audi", "R8", Category.SUPERCARS, 25000, "V10"));
        vehicles.add(new Vehicle("Bajaj", "CT100", Category.OTHER, 300, "100cc"));
        save();
    }
}
