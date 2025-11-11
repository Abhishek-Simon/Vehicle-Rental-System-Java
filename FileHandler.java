import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {

    public static <T> void saveList(List<T> list, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(list);
        } catch (IOException e) {
            System.err.println("Failed saving to " + fileName + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = in.readObject();
            return (List<T>) obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed loading from " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    
    public static void saveTextLines(List<String> lines, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (String l : lines) pw.println(l);
        } catch (IOException e) {
            System.err.println("Failed writing text file " + fileName + ": " + e.getMessage());
        }
    }

    public static List<String> loadTextLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File f = new File(fileName);
        if (!f.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String s;
            while ((s = br.readLine()) != null) lines.add(s);
        } catch (IOException e) {
            System.err.println("Failed reading text file " + fileName + ": " + e.getMessage());
        }
        return lines;
    }
}
