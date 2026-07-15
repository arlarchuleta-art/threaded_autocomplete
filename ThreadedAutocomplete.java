// Name: Adam Archuleta
// Date: July 2026
// Citation: AI guided the logic restructuring for the multi-word matching
// loop and regex boundary matching. I reviewed and adapted everything 
// manually for clarity, stability, and variable naming conventions.

import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.Arrays;

public class ThreadedAutocomplete extends Thread
{
    private String fileName;
    private JTextField enterField;
    private JTextArea output;
    private String last_word;
    
    private static final SecureRandom randGenerator = new SecureRandom();
    
    //Limit the array size
    private static final int SIZE_LIMIT = 100;

    //Constructor
    public ThreadedAutocomplete(String fileName, JTextField enterField, 
                                JTextArea output)
    {
        this.fileName = fileName;
        this.enterField = enterField;
        this.output = output;
    }
    
    @Override
    public void run()
    {
        //Connect to the file.
        last_word = getLastWord();
        String current_word = "";
        String autocomplete;
        
        //Search for matches in a continuous background loop.
        while(!current_word.equals("quit"))
        {
            //Wait until the word changes to reupdate
            waitOnNewWord(current_word);
            current_word = last_word;
            
            // MODIFICATION: QUIT TO EXIT
            // Intercepts the "quit" command to terminate cleanly.
            if (current_word.equals("quit")) {
                System.out.println("Quit received. Exiting application.");
                System.exit(0); 
            }
            
            //Populate an ArrayList with autocomplete options
            ArrayList<String> options = getOptions();
            
            //Select a particular option to display if matches exist
            if(options.size() > 0)
            {
                //Choose a random option from the list
                int randomInt = randGenerator.nextInt(options.size());
                autocomplete = options.get(randomInt);
                autocomplete = wrappedToFit(autocomplete);
                output.setText(autocomplete);
            }
            else
            {
                output.setText("");
            }
        }
        System.out.println("Thread "+getName()+" exiting.");
    }
    
    // Populate and return an ArrayList with autocompleted options
    // from the given file.
    private ArrayList<String> getOptions()
    {
        String line;
        //Reset with an all new Scanner
        Scanner input = getNewScanner();

        ArrayList<String> options = new ArrayList<String>();
        String autocomplete = null;
        
        // MODIFICATION: MULTIWORD MATCH
        // Grab the entire array of words the user typed
        String[] words = getTextFieldInput();
        
        //Find matches in one of the novels
        while(input.hasNextLine())
        {
            line = input.nextLine();
            String lineLower = line.toLowerCase();
            
            // Attempt to match the whole input. If it fails, drop the first 
            // word and try again, repeating until a match is found.
            for (int startIndex = 0; startIndex < words.length; startIndex++) {
                
                // Rebuild the search phrase dynamically 
                String searchPhrase = String.join(" ", 
                    Arrays.copyOfRange(words, startIndex, words.length));
                
                if (searchPhrase.trim().isEmpty()) continue;
                
                // Only match with blank space on both sides of target:
                int i = lineLower.indexOf(" " + searchPhrase + " ");
                
                if(i != -1)
                {
                    // Extract the text until the end of the sentence
                    autocomplete = getTextToPeriod(input, line.substring(i));
                    options.add(autocomplete);
                    break; // Match found, stop checking smaller chunks
                }
            }
            
            // Limit ArrayList size to preserve system memory
            if(options.size() > SIZE_LIMIT)
                break;
        }
        input.close();
        return options;
    }
    
    //Loop and wait until the user changes the most recent word entered.
    private void waitOnNewWord(String current_word)
    {
        while(last_word.equals(current_word))
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
            //Get the last word the user typed in
            last_word = getLastWord();
        }
    }
    
    //Retreive a new Scanner for accessing the given file.
    private Scanner getNewScanner()
    {
        Scanner input = null;
        File file = new File(fileName);
        try {
            input = new Scanner(file,"UTF-8");
        } catch (FileNotFoundException e) {
            System.out.printf("%nError on file: %s (either empty or " +
                              "wrong file format)%n%n", file);
            e.printStackTrace();
            System.exit(1);
        }
        return input;
    }
    
    // MODIFICATION: BREAK BETWEEN WORDS
    // Safely breaks strings at 60 characters by scanning backwards 
    // for the nearest space before enforcing the line break.
    private String wrappedToFit(String toFit)
    {
        StringBuilder toReturn = new StringBuilder();
        
        while(toFit.length() > 60)
        {
            // Find the last space character within the 60-char boundary
            int breakPoint = toFit.lastIndexOf(" ", 60);
            
            // Fallback for 60+ char strings with absolutely no spaces
            if (breakPoint == -1) {
                breakPoint = 60;
            }
            
            toReturn.append(toFit.substring(0, breakPoint)).append("\n");
            // Remove leading space on the new line
            toFit = toFit.substring(breakPoint).trim(); 
        }
        return toReturn.toString() + toFit;
    }
    
    // MODIFICATION: STOP ON EXCLAMATION & EXTRA CREDIT ELEGANCE
    // Dynamically pulls text until the next valid mark (., ?, !) is found.
    private String getTextToPeriod(Scanner input, String remainder)
    {
        // Keep pulling lines until we hit a termination character
        while (!remainder.matches(".*[.?!].*") && input.hasNextLine()) {
            remainder += " " + input.nextLine();
        }
        
        // Find the absolute closest ending punctuation
        int closestPunctuation = -1;
        char[] chars = remainder.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '.' || chars[i] == '?' || chars[i] == '!') {
                closestPunctuation = i;
                break;
            }
        }
        
        // If a mark was found, chop the string right after it
        if (closestPunctuation != -1) {
            return remainder.substring(0, closestPunctuation + 1);
        }
        
        // Return whatever is left if EOF was reached
        return remainder;
    }
    
    //Get text from the input field and return it as a String array.
    private String[] getTextFieldInput()
    {
        String text = enterField.getText();
        text = text.toLowerCase().trim();
        // Clean regex split on whitespace prevents formatting crashes
        return text.split("\\s+");
    }

    //Get and return the last word input by the user.
    private String getLastWord()
    {
        String[] user_input = getTextFieldInput();
        if(user_input.length == 0) return "";
        return user_input[user_input.length-1];
    }
}