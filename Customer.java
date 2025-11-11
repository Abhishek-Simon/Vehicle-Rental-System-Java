import java.io.Serializable;
import java.util.UUID;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerId;
    private String name;
    private String phone;
    private String licenseNo;
    private String address;

    public Customer(String name, String phone, String licenseNo, String address) {
        this.customerId = "C-" + UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.address = address;
    }

    
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getLicenseNo() { return licenseNo; }
    public String getAddress() { return address; }

    
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }
    public void setAddress(String address) { this.address = address; }

    public void display() {
        System.out.println("Customer ID : " + customerId);
        System.out.println("Name        : " + name);
        System.out.println("Phone       : " + phone);
        System.out.println("License No. : " + licenseNo);
        System.out.println("Address     : " + address);
    }

    @Override
    public String toString() {
        return customerId + " | " + name + " | " + phone + " | License: " + licenseNo;
    }
}
