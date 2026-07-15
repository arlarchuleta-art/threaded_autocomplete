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
        
        setLayout(null);
        
        enterField = new JTextField("Enter text here");
        enterField.setFont(new Font("Serif", Font.PLAIN, 32));
        enterField.setBackground(Color.blue);
        enterField.setForeground(Color.yellow);
        enterField.setBounds(10,10,800,60);
        add(enterField);
        
        // Initializing the text areas and positioning them on the GUI
        // Heights slightly reduced to 160 to make room for the EC buttons
        frankensteinArea = new JTextArea();
        frankensteinArea.setBounds(10, 80, 800, 160);
        frankensteinArea.setLineWrap(true);
        frankensteinArea.setWrapStyleWord(true);
        add(frankensteinArea);
        
        mobyDickArea = new JTextArea();
        mobyDickArea.setBounds(10, 300, 800, 160);
        mobyDickArea.setLineWrap(true);
        mobyDickArea.setWrapStyleWord(true);
        add(mobyDickArea);
        
        greatExpectationsArea = new JTextArea();
        greatExpectationsArea.setBounds(10, 520, 800, 160);
        greatExpectationsArea.setLineWrap(true);
        greatExpectationsArea.setWrapStyleWord(true);
        add(greatExpectationsArea);
        
        // MODIFICATION: THREE BUTTON OUTPUT (Refactored)
        // Individual buttons save to their respective novel logs.
        JButton btnFrankenstein = new JButton("Save Frankenstein Snippet");
        btnFrankenstein.setBounds(10, 245, 250, 30);
        btnFrankenstein.addActionListener(e -> 
            saveIndividual("Frankenstein", frankensteinArea.getText()));
        add(btnFrankenstein);

        JButton btnMobyDick = new JButton("Save Moby Dick Snippet");
        btnMobyDick.setBounds(10, 465, 250, 30);
        btnMobyDick.addActionListener(e -> 
            saveIndividual("MobyDick", mobyDickArea.getText()));
        add(btnMobyDick);

        JButton btnGreatExp = new JButton("Save Great Exp. Snippet");
        btnGreatExp.setBounds(10, 685, 250, 30);
        btnGreatExp.addActionListener(e -> 
            saveIndividual("GreatExpectations", 
                           greatExpectationsArea.getText()));
        add(btnGreatExp);

        // Dedicated Mashup button to combine all three text areas
        JButton btnMashup = new JButton("Save Mashup Snippet");
        btnMashup.setBounds(10, 725, 250, 30);
        btnMashup.addActionListener(e -> saveMashup());
        add(btnMashup);

        setSize(840, 840); // set size of window
        setVisible(true);  // show window
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