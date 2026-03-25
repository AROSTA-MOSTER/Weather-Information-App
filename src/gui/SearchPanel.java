package gui;

import util.SearchHistoryManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {
    private JTextField locationField;
    private JButton searchButton;
    private JComboBox<String> historyComboBox;
    private JButton unitToggleButton;
    private MainFrame mainFrame;
    private SearchHistoryManager historyManager;

    public SearchPanel(MainFrame mainFrame, SearchHistoryManager historyManager) {
        this.mainFrame = mainFrame;
        this.historyManager = historyManager;

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        locationField = new JTextField(15);
        searchButton = new JButton("Search");
        unitToggleButton = new JButton("Switch to °F");
        
        historyComboBox = new JComboBox<>();
        historyComboBox.setPreferredSize(new Dimension(200, 25));
        updateHistoryDropdown();

        add(new JLabel("Location:"));
        add(locationField);
        add(searchButton);
        add(new JLabel("History:"));
        add(historyComboBox);
        add(unitToggleButton);

        searchButton.addActionListener(e -> performSearch(locationField.getText()));
        locationField.addActionListener(e -> performSearch(locationField.getText())); // Enter key

        historyComboBox.addActionListener(e -> {
            if (historyComboBox.getSelectedIndex() > 0) { 
                String selected = (String) historyComboBox.getSelectedItem();
                if (selected != null) {
                    int parenIndex = selected.lastIndexOf(" (");
                    if (parenIndex != -1) {
                        String loc = selected.substring(0, parenIndex);
                        locationField.setText(loc);
                        performSearch(loc);
                    }
                }
            }
        });

        unitToggleButton.addActionListener(e -> {
            mainFrame.toggleUnits();
            if (mainFrame.isCelsius()) {
                unitToggleButton.setText("Switch to °F");
            } else {
                unitToggleButton.setText("Switch to °C");
            }
        });
    }

    public void updateHistoryDropdown() {
        historyComboBox.removeAllItems();
        historyComboBox.addItem("Select past search...");
        List<String> hist = historyManager.getHistory();
        for (String h : hist) {
            historyComboBox.addItem(h);
        }
    }

    private void performSearch(String location) {
        if (location != null && !location.trim().isEmpty()) {
            mainFrame.fetchWeather(location.trim());
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a location.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
