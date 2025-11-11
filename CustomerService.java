import java.util.List;
import java.util.stream.Collectors;

public class CustomerService {
    private static final String CUST_FILE = "customers.dat";
    private List<Customer> customers;

    public CustomerService() {
        customers = FileHandler.loadList(CUST_FILE);
        if (customers == null) customers = new java.util.ArrayList<>();
    }

    public void save() {
        FileHandler.saveList(customers, CUST_FILE);
    }

    public void addCustomer(Customer c) {
        customers.add(c);
        save();
    }

    public boolean updateCustomer(String customerId, String name, String phone, String licenseNo, String address) {
        Customer c = findById(customerId);
        if (c == null) return false;
        c.setName(name);
        c.setPhone(phone);
        c.setLicenseNo(licenseNo);
        c.setAddress(address);
        save();
        return true;
    }

    public Customer findById(String id) {
        return customers.stream().filter(c -> c.getCustomerId().equals(id)).findFirst().orElse(null);
    }

    public List<Customer> searchByName(String name) {
        return customers.stream().filter(c -> c.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    public void seedSampleIfEmpty() {
        if (!customers.isEmpty()) return;
        customers.add(new Customer("John Doe", "9876543210", "DL-01-123456", "Some address"));
        customers.add(new Customer("Alice Smith", "9123456780", "DL-02-654321", "Some other address"));
        save();
    }

    public List<Customer> listAll() {
        return customers;
    }
}
