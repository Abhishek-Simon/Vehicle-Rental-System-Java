import java.time.LocalDate;
import java.util.List;


public class Main {
    private static AuthService authService = new AuthService();
    private static VehicleService vehicleService = new VehicleService();
    private static CustomerService customerService = new CustomerService();
    private static RentalService rentalService = new RentalService(vehicleService, customerService);

    public static void main(String[] args) {
        // Seed sample data (for demo)
        vehicleService.seedSampleDataIfEmpty();
        customerService.seedSampleIfEmpty();

        System.out.println("=== Vehicle Rental System (Core Java + File Handling) ===");

        // Authentication (Login / Signup)
        String currentUser = authService.loginOrSignup();

        // Once logged in, go to main menu
        mainMenu(currentUser);
    }

    private static void mainMenu(String currentUser) {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Vehicle Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Rental Processing");
            System.out.println("4. Billing & Reports");
            System.out.println("5. Admin (Users)");
            System.out.println("0. Logout");
            int choice = InputUtils.readInt("Choose an option: ");
            switch (choice) {
                case 1 -> vehicleManagementMenu();
                case 2 -> customerManagementMenu();
                case 3 -> rentalProcessingMenu();
                case 4 -> billingAndReportsMenu();
                case 5 -> adminMenu();
                case 0 -> {
                    System.out.println("Logging out... See you soon, " + currentUser + "!");
                    return; // go back to login/signup
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ---------- Vehicle Management ---------- */
    private static void vehicleManagementMenu() {
        while (true) {
            System.out.println("\n--- VEHICLE MANAGEMENT ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Update Vehicle");
            System.out.println("3. Delete Vehicle");
            System.out.println("4. List All Vehicles");
            System.out.println("5. List Available Vehicles");
            System.out.println("6. Search by Brand");
            System.out.println("7. Filter by Category");
            System.out.println("0. Back");
            int c = InputUtils.readInt("Choice: ");
            switch (c) {
                case 1 -> addVehicleFlow();
                case 2 -> updateVehicleFlow();
                case 3 -> deleteVehicleFlow();
                case 4 -> listAllVehicles();
                case 5 -> listAvailableVehicles();
                case 6 -> searchByBrandFlow();
                case 7 -> filterByCategoryFlow();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addVehicleFlow() {
        System.out.println("--- Add Vehicle ---");
        String brand = InputUtils.readLine("Brand: ");
        String model = InputUtils.readLine("Model: ");
        System.out.println("Categories: BIKES, SUPERBIKES, XUVS, SEDAN, SUPERCARS, OTHER");
        String catStr = InputUtils.readLine("Category: ");
        Category cat = Category.fromString(catStr);
        if (cat == null) {
            System.out.println("Invalid category.");
            return;
        }
        double rent = InputUtils.readDouble("Rent per day (₹): ");
        String extra = InputUtils.readLine("Extra info (e.g., seats, engine): ");
        Vehicle v = new Vehicle(brand, model, cat, rent, extra);
        vehicleService.addVehicle(v);
        System.out.println("✅ Vehicle added: " + v.getVehicleId());
    }

    private static void updateVehicleFlow() {
        System.out.println("--- Update Vehicle ---");
        String id = InputUtils.readLine("Vehicle ID: ");
        Vehicle v = vehicleService.findById(id);
        if (v == null) {
            System.out.println("Vehicle not found.");
            return;
        }
        String brand = InputUtils.readLine("New Brand (" + v.getBrand() + "): ");
        if (brand.isBlank()) brand = v.getBrand();
        String model = InputUtils.readLine("New Model (" + v.getModel() + "): ");
        if (model.isBlank()) model = v.getModel();
        System.out.println("Categories: BIKES, SUPERBIKES, XUVS, SEDAN, SUPERCARS, OTHER");
        String catStr = InputUtils.readLine("New Category (" + v.getCategory() + "): ");
        Category cat = catStr.isBlank() ? v.getCategory() : Category.fromString(catStr);
        if (cat == null) {
            System.out.println("Invalid category.");
            return;
        }
        String rentStr = InputUtils.readLine("New Rent per day (" + v.getRentPerDay() + "): ");
        double rent = rentStr.isBlank() ? v.getRentPerDay() : Double.parseDouble(rentStr);
        String extra = InputUtils.readLine("New Extra Info (" + v.getExtraInfo() + "): ");
        if (extra.isBlank()) extra = v.getExtraInfo();
        boolean ok = vehicleService.updateVehicle(id, brand, model, cat, rent, extra);
        System.out.println(ok ? "✅ Vehicle updated." : "❌ Failed to update.");
    }

    private static void deleteVehicleFlow() {
        System.out.println("--- Delete Vehicle ---");
        String id = InputUtils.readLine("Vehicle ID: ");
        boolean ok = vehicleService.deleteVehicle(id);
        System.out.println(ok ? "✅ Vehicle deleted." : "❌ Vehicle not found.");
    }

    private static void listAllVehicles() {
        System.out.println("--- All Vehicles ---");
        System.out.printf("%-10s %-10s %-10s %-12s %-8s %-10s%n", "ID", "Brand", "Model", "Category", "Rent", "Status");
        for (Vehicle v : vehicleService.listAll()) v.displayShort();
    }

    private static void listAvailableVehicles() {
        System.out.println("--- Available Vehicles ---");
        System.out.printf("%-10s %-10s %-10s %-12s %-8s %-10s%n", "ID", "Brand", "Model", "Category", "Rent", "Status");
        for (Vehicle v : vehicleService.listAvailable()) v.displayShort();
    }

    private static void searchByBrandFlow() {
        String brand = InputUtils.readLine("Brand to search: ");
        List<Vehicle> list = vehicleService.searchByBrand(brand);
        if (list.isEmpty()) {
            System.out.println("No vehicles found for brand: " + brand);
        } else {
            list.forEach(Vehicle::displayShort);
        }
    }

    private static void filterByCategoryFlow() {
        String catStr = InputUtils.readLine("Category: ");
        Category cat = Category.fromString(catStr);
        if (cat == null) {
            System.out.println("Invalid category.");
            return;
        }
        List<Vehicle> list = vehicleService.filterByCategory(cat);
        if (list.isEmpty()) {
            System.out.println("No vehicles found in category: " + cat);
        } else {
            list.forEach(Vehicle::displayShort);
        }
    }

    /* ---------- Customer Management ---------- */
    private static void customerManagementMenu() {
        while (true) {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Register Customer");
            System.out.println("2. Update Customer");
            System.out.println("3. List Customers");
            System.out.println("4. Search by Name");
            System.out.println("0. Back");
            int c = InputUtils.readInt("Choice: ");
            switch (c) {
                case 1 -> registerCustomerFlow();
                case 2 -> updateCustomerFlow();
                case 3 -> listCustomersFlow();
                case 4 -> searchCustomerFlow();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerCustomerFlow() {
        System.out.println("--- Register Customer ---");
        String name = InputUtils.readLine("Name: ");
        String phone = InputUtils.readLine("Phone: ");
        String license = InputUtils.readLine("License No.: ");
        String address = InputUtils.readLine("Address: ");
        Customer c = new Customer(name, phone, license, address);
        customerService.addCustomer(c);
        System.out.println("✅ Customer registered: " + c.getCustomerId());
    }

    private static void updateCustomerFlow() {
        System.out.println("--- Update Customer ---");
        String id = InputUtils.readLine("Customer ID: ");
        Customer c = customerService.findById(id);
        if (c == null) {
            System.out.println("Customer not found.");
            return;
        }
        String name = InputUtils.readLine("New Name (" + c.getName() + "): ");
        if (name.isBlank()) name = c.getName();
        String phone = InputUtils.readLine("New Phone (" + c.getPhone() + "): ");
        if (phone.isBlank()) phone = c.getPhone();
        String license = InputUtils.readLine("New License (" + c.getLicenseNo() + "): ");
        if (license.isBlank()) license = c.getLicenseNo();
        String address = InputUtils.readLine("New Address (" + c.getAddress() + "): ");
        if (address.isBlank()) address = c.getAddress();
        boolean ok = customerService.updateCustomer(id, name, phone, license, address);
        System.out.println(ok ? "✅ Customer updated." : "❌ Failed to update.");
    }

    private static void listCustomersFlow() {
        System.out.println("--- All Customers ---");
        for (Customer c : customerService.listAll()) {
            System.out.println(c.toString());
        }
    }

    private static void searchCustomerFlow() {
        String name = InputUtils.readLine("Name to search: ");
        List<Customer> list = customerService.searchByName(name);
        if (list.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            list.forEach(Customer::display);
        }
    }

    /* ---------- Rental Processing ---------- */
    private static void rentalProcessingMenu() {
        while (true) {
            System.out.println("\n--- RENTAL PROCESSING ---");
            System.out.println("1. Rent a Vehicle");
            System.out.println("2. Return a Vehicle");
            System.out.println("3. List Active Rentals");
            System.out.println("4. List All Rentals");
            System.out.println("0. Back");
            int c = InputUtils.readInt("Choice: ");
            switch (c) {
                case 1 -> rentVehicleFlow();
                case 2 -> returnVehicleFlow();
                case 3 -> listActiveRentals();
                case 4 -> listAllRentals();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void rentVehicleFlow() {
        System.out.println("--- Rent Vehicle ---");
        listAvailableVehicles();
        String vid = InputUtils.readLine("Enter Vehicle ID to rent: ");
        Vehicle v = vehicleService.findById(vid);
        if (v == null) {
            System.out.println("Vehicle not found.");
            return;
        }
        if (!v.isAvailable()) {
            System.out.println("Vehicle is not available.");
            return;
        }
        System.out.println("Customers:");
        listCustomersFlow();
        String cid = InputUtils.readLine("Enter existing Customer ID or leave blank to register: ");
        if (cid.isBlank()) {
            registerCustomerFlow();
            System.out.println("Please enter the new Customer ID shown above:");
            cid = InputUtils.readLine("Customer ID: ");
        }
        Customer c = customerService.findById(cid);
        if (c == null) {
            System.out.println("Customer not found.");
            return;
        }
        LocalDate from = InputUtils.readDate("Rent start date");
        LocalDate to = InputUtils.readDate("Expected return date");
        boolean ok = rentalService.rentVehicle(vid, cid, from, to);
        System.out.println(ok ? "Vehicle rented successfully." : "Failed to rent vehicle.");
    }

    private static void returnVehicleFlow() {
        System.out.println("--- Return Vehicle ---");
        listActiveRentals();
        String rid = InputUtils.readLine("Enter Rental ID to return: ");
        Rental r = rentalService.findById(rid);
        if (r == null || r.isReturned()) {
            System.out.println("Invalid rental ID or already returned.");
            return;
        }
        LocalDate actual = InputUtils.readDate("Actual return date");
        boolean ok = rentalService.returnVehicle(rid, actual);
        System.out.println(ok ? "Vehicle returned successfully." : "Failed to process return.");
    }

    private static void listActiveRentals() {
        System.out.println("--- Active Rentals ---");
        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-8s %-8s%n", "RID", "VID", "CID", "RentDate", "ReturnDate", "Amount", "Status");
        for (Rental r : rentalService.listActive()) r.displayShort();
    }

    private static void listAllRentals() {
        System.out.println("--- All Rentals ---");
        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-8s %-8s%n", "RID", "VID", "CID", "RentDate", "ReturnDate", "Amount", "Status");
        for (Rental r : rentalService.listAll()) r.displayShort();
    }

    /* ---------- Billing & Reports ---------- */
    private static void billingAndReportsMenu() {
        while (true) {
            System.out.println("\n--- BILLING & REPORTS ---");
            System.out.println("1. View Bill File for a Rental");
            System.out.println("2. Total Earnings");
            System.out.println("3. Generate Summary Report (text)");
            System.out.println("0. Back");
            int c = InputUtils.readInt("Choice: ");
            switch (c) {
                case 1 -> viewBillFileFlow();
                case 2 -> showTotalEarnings();
                case 3 -> generateSummaryReport();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewBillFileFlow() {
        String rid = InputUtils.readLine("Enter Rental ID: ");
        Rental r = rentalService.findById(rid);
        if (r == null) {
            System.out.println("Rental not found.");
            return;
        }
        String fileName = "bill_" + r.getRentalId() + ".txt";
        java.io.File f = new java.io.File(fileName);
        if (!f.exists()) {
            System.out.println("Bill file not found. If you recently created the rental, a bill should have been generated.");
            return;
        }
        System.out.println("Displaying bill file: " + fileName);
        try (java.util.Scanner sc = new java.util.Scanner(f)) {
            while (sc.hasNextLine()) System.out.println(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Failed to read bill file: " + e.getMessage());
        }
    }

    private static void showTotalEarnings() {
        double total = rentalService.totalEarnings();
        System.out.println("Total earnings from all rentals: ₹" + String.format("%.2f", total));
    }

    private static void generateSummaryReport() {
        String fileName = "summary_report.txt";
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(fileName))) {
            pw.println("=== VEHICLE RENTAL SUMMARY REPORT ===");
            pw.println("Total vehicles: " + vehicleService.listAll().size());
            pw.println("Total customers: " + customerService.listAll().size());
            pw.println("Total rentals: " + rentalService.listAll().size());
            pw.println("Total earnings: ₹" + String.format("%.2f", rentalService.totalEarnings()));
            pw.println("");
            pw.println("---- RENTALS DETAIL ----");
            for (Rental r : rentalService.listAll()) {
                pw.println(r.toString());
            }
            pw.flush();
            System.out.println("Summary report generated at " + fileName);
        } catch (Exception e) {
            System.out.println("Failed to generate summary report: " + e.getMessage());
        }
    }

    /* ---------- Admin (users) ---------- */
    private static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add User");
            System.out.println("2. Change Password");
            System.out.println("3. List Users");
            System.out.println("0. Back");
            int c = InputUtils.readInt("Choice: ");
            switch (c) {
                case 1 -> addUserFlow();
                case 2 -> changePasswordFlow();
                case 3 -> listUsersFlow();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addUserFlow() {
        String u = InputUtils.readLine("New username: ");
        String p = InputUtils.readLine("New password: ");
        boolean ok = authService.addUser(u, p);
        System.out.println(ok ? "User added." : "User already exists.");
    }

    private static void changePasswordFlow() {
        String u = InputUtils.readLine("Username: ");
        String oldp = InputUtils.readLine("Old password: ");
        String newp = InputUtils.readLine("New password: ");
        boolean ok = authService.changePassword(u, oldp, newp);
        System.out.println(ok ? "Password changed." : "Failed: wrong credentials or user not found.");
    }

    private static void listUsersFlow() {
        System.out.println("Users:");
        for (String u : authService.listUsers()) System.out.println("- " + u);
    }
}
