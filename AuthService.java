import java.io.*;
import java.util.*;
@SuppressWarnings("resource")

public class AuthService {
    private static final String USER_FILE = "users.txt";
    private Map<String, String> users = new HashMap<>();

    public AuthService() {
        loadUsers();
    }

    /* -------------------- File Handling -------------------- */
    private void loadUsers() {
        users.clear();
        File file = new File(USER_FILE);
        if (!file.exists()) {
            // Add a default admin user on first run
            users.put("admin", "admin123");
            saveUsers();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /* -------------------- Authentication -------------------- */
    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // already exists
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (authenticate(username, oldPassword)) {
            users.put(username, newPassword);
            saveUsers();
            return true;
        }
        return false;
    }

    public List<String> listUsers() {
        return new ArrayList<>(users.keySet());
    }

    /* -------------------- Login & Signup Logic -------------------- */
    public String loginOrSignup() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- LOGIN / SIGNUP ---");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter username: ");
                String u = sc.nextLine();
                System.out.print("Enter password: ");
                String p = sc.nextLine();
                if (authenticate(u, p)) {
                    System.out.println("Login successful. Welcome, " + u + "!");
                    return u;
                } else {
                    System.out.println("Invalid credentials. Try again.");
                }
            } else if (choice.equals("2")) {
                System.out.print("Choose a username: ");
                String newU = sc.nextLine();
                if (users.containsKey(newU)) {
                    System.out.println("Username already exists!");
                    continue;
                }
                System.out.print("Choose a password: ");
                String newP = sc.nextLine();
                addUser(newU, newP);
                System.out.println("Signup successful! You can now login.");
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }
}
