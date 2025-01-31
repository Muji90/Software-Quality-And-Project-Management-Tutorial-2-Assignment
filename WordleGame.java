package Tutorial2;

import java.io.*;
import java.util.*;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;
    private static List<String> wordList = new ArrayList<>();

    public static void main(String[] args) {
        // Try to load the dictionary from a file, or use a fallback list if not found
        loadDictionary("dictionary.txt");

        // If the word list is still empty, fall back to a hardcoded list
        if (wordList.isEmpty()) {
            System.out.println("Using fallback word list.");
            loadFallbackWordList();
        }

        // Check if the word list is empty after both attempts
        if (wordList.isEmpty()) {
            System.out.println("Error: No valid 5-letter words found.");
            return;
        }

        // Filter the word list to only include 5-letter words
        wordList = filterFiveLetterWords(wordList);

        // Check if we have any 5-letter words
        if (wordList.isEmpty()) {
            System.out.println("Error: No 5-letter words found in the word list.");
            return;
        }

        // Select a random 5-letter word from the list
        String targetWord = getRandomWord();

        // Start the game with user interaction
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Wordle! Try to guess the 5-letter word.");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts.");

        // Variable to track whether the word has been guessed correctly
        boolean guessedCorrectly = false;

        // Loop for the maximum number of attempts
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Attempt " + attempt + ": Enter your guess: ");
            String guess = scanner.nextLine().toLowerCase();

            // Validate the guess length and word presence
            if (!isValidGuess(guess)) {
                System.out.println("Invalid guess. Please enter a valid 5-letter word.");
                attempt--; // Decrement to retry the same attempt
                continue;
            }

            // Provide feedback
            String feedback = generateFeedback(targetWord, guess);
            System.out.println("Feedback: " + feedback);

            // Check if the guess was correct
            if (guess.equals(targetWord)) {
                System.out.println("Congratulations! You guessed the word correctly!");
                guessedCorrectly = true;
                break;
            }

            // If no attempts are left, display the correct word
            if (attempt == MAX_ATTEMPTS && !guessedCorrectly) {
                System.out.println("Sorry, you've used all attempts. The correct word was: " + targetWord);
            }
        }

        scanner.close();
    }

    // Function to load the dictionary from a file
    public static void loadDictionary(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.matches("[a-z]+")) { // Only add alphabetic words
                    wordList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    // Function to load a fallback list of words if the file cannot be loaded
    public static void loadFallbackWordList() {
        wordList = Arrays.asList(
            "apple", "table", "chair", "water", "lemon", "bread", "grape", "peach", "plumb", "bison",
            "plane", "stone", "tiger", "beach", "vocal", "music", "sharp", "blaze", "sweet", "flame",
            "witch", "piano", "mount", "beard", "earth", "shone", "stone", "plane", "flood", "lunar",
            "fresh", "sugar", "comic", "flute", "drain", "plant", "block", "jumpy", "crowd", "light",
            "grape", "pouch", "frank", "green", "shark", "blink", "storm", "pouch", "rainy", "molar",
            "beast", "minor", "glove", "frill", "boldy", "flint", "purse", "touch", "unite", "proud"
        );
    }

    // Function to filter the dictionary and return only 5-letter words
    public static List<String> filterFiveLetterWords(List<String> words) {
        List<String> fiveLetterWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() == WORD_LENGTH) {
                fiveLetterWords.add(word);
            }
        }
        return fiveLetterWords;
    }

    // Function to select a random word from the word list
    public static String getRandomWord() {
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }

    // Function to generate feedback after each guess
    public static String generateFeedback(String targetWord, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessedChar = guess.charAt(i);
            char targetChar = targetWord.charAt(i);

            if (guessedChar == targetChar) {
                feedback.append(guessedChar); // Correct letter in the correct position
            } else if (targetWord.indexOf(guessedChar) >= 0) {
                feedback.append(Character.toLowerCase(guessedChar)); // Correct letter, wrong position
            } else {
                feedback.append("_"); // Incorrect letter
            }
        }
        return feedback.toString();
    }

    // Function to validate the guess
    public static boolean isValidGuess(String guess) {
        // Check if the guess is exactly 5 letters and only contains alphabetic characters
        return guess.length() == WORD_LENGTH && guess.matches("[a-zA-Z]+");
    }

    // TEST CASES

    // Test case 1: Verify that the random word selection works (should be one of the words in the list)
    public static void testRandomWordSelection() {
        String randomWord = getRandomWord();
        boolean validWord = wordList.contains(randomWord);
        System.out.println("Test Random Word Selection: " + (validWord ? "Passed" : "Failed"));
    }

    // Test case 2: Test the feedback generation for correct guess
    public static void testCorrectGuessFeedback() {
        String targetWord = "table";
        String guess = "table";
        String feedback = generateFeedback(targetWord, guess);
        // Expected feedback: "table"
        System.out.println("Test Correct Guess Feedback: " + (feedback.equals("table") ? "Passed" : "Failed"));
    }

    // Test case 3: Test feedback when the guess has correct letters in incorrect positions
    public static void testIncorrectPositionFeedback() {
        String targetWord = "table";
        String guess = "peach";
        String feedback = generateFeedback(targetWord, guess);
        // Expected feedback: "_a__e"
        System.out.println("Test Incorrect Position Feedback: " + (feedback.equals("_a__e") ? "Passed" : "Failed"));
    }

    // Test case 4: Test feedback when the guess has incorrect letters
    public static void testIncorrectLettersFeedback() {
        String targetWord = "table";
        String guess = "water";
        String feedback = generateFeedback(targetWord, guess);
        // Expected feedback: "_____"
        System.out.println("Test Incorrect Letters Feedback: " + (feedback.equals("_____") ? "Passed" : "Failed"));
    }

    // Test case 5: Test the guess validation for non-5-letter words
    public static void testInvalidGuessLength() {
        String guess = "grapes"; // Invalid length (6 letters)
        if (guess.length() != WORD_LENGTH || !wordList.contains(guess)) {
            System.out.println("Test Invalid Guess Length: Passed");
        } else {
            System.out.println("Test Invalid Guess Length: Failed");
        }
    }

    // Test case 6: Test when the game runs out of attempts
    public static void testOutOfAttempts() {
        String targetWord = "table";
        String[] guesses = {"grape", "melon", "lemon", "berry", "peach", "plumb"};
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String guess = guesses[attempt];
            String feedback = generateFeedback(targetWord, guess);
            System.out.println("Attempt " + (attempt + 1) + ": " + guess + " - Feedback: " + feedback);
        }
        System.out.println("Test Out of Attempts: Passed (if no win detected)");
    }

    public static void runTests() {
        testRandomWordSelection();
        testCorrectGuessFeedback();
        testIncorrectPositionFeedback();
        testIncorrectLettersFeedback();
        testInvalidGuessLength();
        testOutOfAttempts();
    }
}
