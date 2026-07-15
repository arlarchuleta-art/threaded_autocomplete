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
        
        // MODIFICATION: THREE BUTTON OUTPUT
        // Adding buttons directly below each text area. When clicked, they 
        // pull the current text and save it to a mashup story file.
        JButton btnFrankenstein = new JButton("Save Frankenstein Snippet");
        btnFrankenstein.setBounds(10, 245, 250, 30);
        btnFrankenstein.addActionListener(e -> 
            appendToMashup(frankensteinArea.getText()));
        add(btnFrankenstein);

        JButton btnMobyDick = new JButton("Save Moby Dick Snippet");
        btnMobyDick.setBounds(10, 465, 250, 30);
        btnMobyDick.addActionListener(e -> 
            appendToMashup(mobyDickArea.getText()));
        add(btnMobyDick);

        JButton btnGreatExpectations = new JButton("Save Great Exp. Snippet");
        btnGreatExpectations.setBounds(10, 685, 250, 30);
        btnGreatExpectations.addActionListener(e -> 
            appendToMashup(greatExpectationsArea.getText()));
        add(btnGreatExpectations);

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

    // Helper method for the THREE BUTTON OUTPUT modification.
    // Appends the current search term and the suggested text to a file.
    // Opens the file in "append" mode (true) so data isn't overwritten.
    private void appendToMashup(String suggestionText) {
        if (suggestionText == null || suggestionText.trim().isEmpty()) {
            return; // Prevent saving empty blanks to the file
        }
        try (FileWriter fw = new FileWriter("MashupStory.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println("Prompt: " + enterField.getText());
            out.println("Snippet: " + suggestionText);
            out.println("--------------------------------------------------");
        } catch (IOException ex) {
            System.out.println("Error writing to MashupStory.txt");
        }
    }
}