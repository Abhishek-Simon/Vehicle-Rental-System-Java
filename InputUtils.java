import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {
    private static final Scanner SC = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SC.nextLine();
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = SC.nextLine();
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = SC.nextLine();
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (YYYY-MM-DD): ");
                String s = SC.nextLine();
                return LocalDate.parse(s.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Please enter date in YYYY-MM-DD format.");
            }
        }
    }

    public static void pause() {
        System.out.println("Press Enter to continue...");
        SC.nextLine();
    }
}
