import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class User {
    protected String username;
    protected int unlockedLevels;
    protected int[] bestTimes;

    protected int[] stars;

    public User(String username) {
        this.username = username;
        this.unlockedLevels = 1;  // Domyślnie poziom 1 odblokowany
        this.bestTimes = new int[20];  // Tablica do przechowywania najlepszych czasów
        this.stars = new int[20];
        saveUserToFile();
    }

    private void saveUserToFile() {
        if (!Objects.equals(username, "GOŚĆ")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                // Format zapisu: nazwa, odblokowane poziomy, czasy
                StringBuilder data = new StringBuilder();
                data.append(username).append(",").append(unlockedLevels);
                for (int time : bestTimes) {
                    data.append(",").append(time);
                }
                for (int star : stars) {
                    data.append(",").append(star);
                }
                writer.write(data.toString());
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisywania użytkownika: " + e.getMessage());
            }
        }
    }
}