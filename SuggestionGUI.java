// Name: Adam Archuleta
// Date: July 2026
// Citation: Consulted AI docs for Java Swing JButton layout and FileWriter 
// append mode operations. I manually integrated them into the existing GUI.

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Component;

//Extra imports for improved interface readability
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestionGUI extends JFrame 
{
    private JTextField enterField;
    
    // Declaring the text areas for the three different novels
    private JTextArea frankensteinArea;
    private JTextArea mobyDickArea;
    private JTextArea greatExpectationsArea;
    
    // set up GUI
    public SuggestionGUI()
    {
        super("Autocomplete from novels");
        
        // Use a JPanel as the main container to easily apply a border
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // This restores the 10px margin around the entire application
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);
        
        enterField = new JTextField("Enter text here");
        enterField.setFont(new Font("Serif", Font.PLAIN, 32));
        enterField.setBackground(Color.blue);
        enterField.setForeground(Color.yellow);
        enterField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(enterField);
        
        mainPanel.add(Box.createVerticalStrut(10)); // Gap below input field
        
        // Initializing the text areas and positioning them on the GUI
        frankensteinArea = new JTextArea(5, 50);
        frankensteinArea.setLineWrap(true);
        frankensteinArea.setWrapStyleWord(true);
        // Restores the visual box around the text area
        frankensteinArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        frankensteinArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(frankensteinArea);
        
        mainPanel.add(Box.createVerticalStrut(5)); // Gap below text area
        
        // MODIFICATION: THREE BUTTON OUTPUT (Refactored)
        JButton btnFrankenstein = new JButton("Save Frankenstein Snippet");
        btnFrankenstein.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnFrankenstein.addActionListener(e -> 
            saveIndividual("Frankenstein", frankensteinArea.getText()));
        mainPanel.add(btnFrankenstein);

        mainPanel.add(Box.createVerticalStrut(15)); // Gap before next section

        mobyDickArea = new JTextArea(5, 50);
        mobyDickArea.setLineWrap(true);
        mobyDickArea.setWrapStyleWord(true);
        mobyDickArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mobyDickArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(mobyDickArea);
        
        mainPanel.add(Box.createVerticalStrut(5)); 
        
        JButton btnMobyDick = new JButton("Save Moby Dick Snippet");
        btnMobyDick.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMobyDick.addActionListener(e -> 
            saveIndividual("MobyDick", mobyDickArea.getText()));
        mainPanel.add(btnMobyDick);

        mainPanel.add(Box.createVerticalStrut(15));

        greatExpectationsArea = new JTextArea(5, 50);
        greatExpectationsArea.setLineWrap(true);
        greatExpectationsArea.setWrapStyleWord(true);
        greatExpectationsArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        greatExpectationsArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(greatExpectationsArea);
        
        mainPanel.add(Box.createVerticalStrut(5));
        
        JButton btnGreatExp = new JButton("Save Great Exp. Snippet");
        btnGreatExp.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGreatExp.addActionListener(e -> 
            saveIndividual("GreatExpectations", 
                           greatExpectationsArea.getText()));
        mainPanel.add(btnGreatExp);

        mainPanel.add(Box.createVerticalStrut(5));

        // Dedicated Mashup button to combine all three text areas
        JButton btnMashup = new JButton("Save Mashup Snippet");
        btnMashup.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMashup.addActionListener(e -> saveMashup());
        mainPanel.add(btnMashup);

        setSize(850, 900); 
        setVisible(true);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Creating background threads for each novel, passing the input field
        ThreadedAutocomplete thread1 = new ThreadedAutocomplete(
            "Frankenstein.txt", enterField, frankensteinArea);
        ThreadedAutocomplete thread2 = new ThreadedAutocomplete(
            "MobyDick.txt", enterField, mobyDickArea);
        ThreadedAutocomplete thread3 = new ThreadedAutocomplete(
            "GreatExpectations.txt", enterField, greatExpectationsArea);
        
        // Starting all background search threads concurrently
        thread1.start();
        thread2.start();
        thread3.start();
    }

    // Save individual novel to its own specific file
    private void saveIndividual(String novelName, String text) {
        if (text == null || text.trim().isEmpty()) {
            return; // Prevent saving empty blanks to the file
        }
        
        String fileName = novelName + "_Log.txt";
        
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // Injects the novel name into the text file directly
            out.println("Novel: " + novelName);
            out.println("Prompt: " + enterField.getText());
            out.println("Snippet: " + text);
            out.println("--------------------------------------------------");
        } catch (IOException ex) {
            System.out.println("Error saving " + novelName);
        }
    }

    // The "Mashup" button calls this to grab all three areas at once
    private void saveMashup() {
        try (FileWriter fw = new FileWriter("MashupStory.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
             
            out.println("--- Full Mashup: " + enterField.getText() + " ---");
            out.println("Frankenstein: " + frankensteinArea.getText());
            out.println("Moby Dick: " + mobyDickArea.getText());
            out.println("Great Exp: " + greatExpectationsArea.getText());
            out.println("==================================================");
        } catch (IOException ex) {
            System.out.println("Error saving mashup");
        }
    }
}