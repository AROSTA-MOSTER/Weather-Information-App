package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryManager {
    private static final String HISTORY_FILE = "history.txt";
    private List<String> history;

    public SearchHistoryManager() {
        history = new ArrayList<>();
        loadHistory();
    }

    public void addSearch(String location) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String entry = location + " (" + timestamp + ")";
        
        // Remove existing entry for exact location to avoid duplicates
        history.removeIf(h -> h.startsWith(location + " ("));
        
        history.add(0, entry); // Add to the top
        if (history.size() > 10) { // Keep last 10 searches
            history.remove(history.size() - 1);
        }
        saveHistory();
    }

    public List<String> getHistory() {
        return history;
    }

    private void loadHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            // File might not exist yet, which is fine.
        }
    }

    private void saveHistory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            for (String entry : history) {
                writer.println(entry);
            }
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
}
