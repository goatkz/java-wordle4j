package game;

import dictionary.WordleDictionary;
import exceptions.InvalidWordException;
import exceptions.WordNotFoundInDictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleGame {

    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;

    private final WordleDictionary dictionary;
    private final String answer;

    private final List<String> guesses;
    private final List<String> hints;
    private final Set<String> usedHints;

    private int remainingAttempts;
    private boolean won;

    public WordleGame(final WordleDictionary dictionary) {

        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не может быть null.");
        }

        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();

        this.guesses = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.usedHints = new HashSet<>();

        this.remainingAttempts = MAX_ATTEMPTS;
        this.won = false;
    }

    public String makeGuess(final String input)
            throws InvalidWordException, WordNotFoundInDictionary {

        String guess = dictionary.normalizeWord(input);

        validateGuess(guess);

        if (guess.equals(answer)) {
            won = true;
        }

        String hint = dictionary.buildHint(answer, guess);

        guesses.add(guess);
        hints.add(hint);

        remainingAttempts--;

        return hint;
    }

    private void validateGuess(final String guess)
            throws InvalidWordException, WordNotFoundInDictionary {

        if (guess == null || guess.isEmpty()) {
            throw new InvalidWordException(
                    "Введите слово."
            );
        }

        if (guess.length() != WORD_LENGTH) {
            throw new InvalidWordException(
                    "Слово должно содержать 5 букв."
            );
        }

        if (!dictionary.containsOnlyRussianLetters(guess)) {
            throw new InvalidWordException(
                    "Слово должно содержать только русские буквы."
            );
        }

        if (!dictionary.containsWord(guess)) {
            throw new WordNotFoundInDictionary(
                    "Такого слова нет в словаре."
            );
        }
    }

    public String getHint() {

        List<String> possibleWords = dictionary.findWordsMatchingHints(
                guesses,
                hints
        );

        possibleWords.removeAll(usedHints);

        if (possibleWords.isEmpty()) {
            return null;
        }

        String hint = possibleWords.get(0);
        usedHints.add(hint);

        return hint;
    }

    public boolean hasWon() {
        return won;
    }

    public boolean hasAttemptsRemaining() {
        return remainingAttempts > 0;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getGuesses() {
        return new ArrayList<>(guesses);
    }

    public List<String> getHints() {
        return new ArrayList<>(hints);
    }

    public boolean isGameOver() {
        return won || remainingAttempts <= 0;
    }
}