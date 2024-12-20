package TypingGame;


	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;
	import java.io.BufferedReader;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.Random;

	public class GamePanel extends JPanel implements Runnable, KeyListener {
	    private final ArrayList<Word> words; // List of words to display and move
	    private final Random random; // Random generator for positions
	    private boolean running; // Controls the game loop
	    private String inputText = ""; // Stores user's current input

	    // Constructor for initializing the game panel
	    public GamePanel() {
	        setBackground(Color.BLACK); // Set the background color to black
	        words = new ArrayList<>(); // Initialize the word list
	        random = new Random(); // Initialize the random generator
	        generateWords(); // Populate the initial list of words
	        setFocusable(true); // Make the panel focusable for keyboard input
	        addKeyListener(this); // Add key listener to the panel
	    }

	    // Generate a list of words with random positions
	    private void generateWords() {
	        ArrayList<String> wordList = readWordsFromFile("words.txt");
	        for (String word : wordList) {
	            int x, y;
	            boolean overlapping;

	            do {
	                overlapping = false;
	                // Rastgele bir x ve y koordinatı oluştur
	                x = random.nextInt(600); 
	                y = random.nextInt(200); 

	                // Çakışma kontrolü
	                for (Word existingWord : words) {
	                    // Kelimelerin en ve boylarını göz önünde bulundurarak çakışma kontrolü yapıyoruz
	                    int wordWidth = getFontMetrics(getFont()).stringWidth(existingWord.text);
	                    int wordHeight = getFontMetrics(getFont()).getHeight();

	                    if (Math.abs(existingWord.x - x) < wordWidth && Math.abs(existingWord.y - y) < wordHeight) {
	                        overlapping = true;  // Çakışma varsa, yeniden konum oluştur
	                        break;
	                    }
	                }
	            } while (overlapping);  // Eğer çakışma varsa yeni konum oluşturulacak

	            // Yeni kelimeyi listeye ekle
	            words.add(new Word(word, x, y));
	        }
	    }


	    // Read words from the file
	    private ArrayList<String> readWordsFromFile(String filename) {
	        ArrayList<String> wordList = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(
	                getClass().getResourceAsStream(filename)))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                wordList.add(line.trim());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return wordList;
	    }

	    // Draw words and user input
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g); // Call parent method to ensure proper painting
	        g.setColor(Color.WHITE); // Set the text color to white

	        // Draw words
	        for (Word word : words) {
	            g.drawString(word.text, word.x, word.y); // Draw each word at its position
	        }

	        // Draw user input
	        g.setColor(Color.GREEN);
	        g.drawString("Your Input: " + inputText, 10, getHeight() - 10); // Display user input at the bottom
	    }

	    // Start the game loop in a new thread
	    public void start() {
	        running = true; // Set the running flag to true
	        Thread thread = new Thread(this); // Create a new thread for the game loop
	        thread.start(); // Start the thread
	    }

	    // Game loop
	    @Override
	    public void run() {
	        while (running) {
	            // Update word positions
	            for (Word word : words) {
	                word.y += 2; // Move the word downward
	                if (word.y > getHeight()) { // Reset word if it falls below the panel
	                    word.y = 0;
	                    word.x = random.nextInt(600);
	                }
	            }

	            // Check if the user's input matches any word
	            boolean wordMatched = words.removeIf(word -> word.text.equalsIgnoreCase(inputText));
	            if (wordMatched) {
	                inputText = ""; // Clear the input text if a word is matched
	            }

	            repaint(); // Redraw the panel
	            try {
	                Thread.sleep(150); // Pause to control speed
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                break;
	            }
	        }
	    }


	    // Handle key typed (record character input)
	    @Override
	    public void keyTyped(KeyEvent e) {
	        char keyChar = e.getKeyChar();
	        if (Character.isLetterOrDigit(keyChar)) {
	            inputText += keyChar; // Append the character to input text
	        } else if (keyChar == '\b' && inputText.length() > 0) {
	            inputText = inputText.substring(0, inputText.length() - 1); // Handle backspace
	        }
	    }

	    // Handle key pressed (optional, not used here)
	    @Override
	    public void keyPressed(KeyEvent e) {}

	    // Handle key released (optional, not used here)
	    @Override
	    public void keyReleased(KeyEvent e) {}
	}



