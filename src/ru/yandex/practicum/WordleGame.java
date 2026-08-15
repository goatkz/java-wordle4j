package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleGame {

    private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;

    private final String answer;
    private final WordleDictionary dictionary;

    private final List<String> guesses;
    private final List<String> hints;
    private final Set<String> usedHints;

    private int remainingAttempts;
    private boolean won;

    public WordleGame(final WordleDictionary dictionary) {

        if (dictionary == null) {
            throw new IllegalArgumentException(
                    "Словарь не может быть null."
            );
        }

        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();

        this.guesses = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.usedHints = new HashSet<>();

        this.remainingAttempts = MAX_ATTEMPTS;
        this.won = false;
    }

    public String makeGuess(final String input) {

        String guessedWord = dictionary.normalizeWord(input);

        validateGuess(guessedWord);

        String hint = dictionary.buildHint(
                answer,
                guessedWord
        );

        guesses.add(guessedWord);
        hints.add(hint);

        remainingAttempts--;

        if (guessedWord.equals(answer)) {
            won = true;
        }

        return hint;
    }

    private void validateGuess(final String guessedWord) {

        if (guessedWord == null || guessedWord.isEmpty()) {
            throw new IllegalArgumentException(
                    "Введите слово."
            );
        }

        if (!dictionary.isValidLength(guessedWord)) {
            throw new IllegalArgumentException(
                    "Слово должно содержать 5 букв."
            );
        }

        if (!dictionary.containsOnlyRussianLetters(guessedWord)) {
            throw new IllegalArgumentException(
                    "Слово должно содержать только русские буквы."
            );
        }

        if (!dictionary.containsWord(guessedWord)) {
            throw new IllegalArgumentException(
                    "Такого слова нет в словаре."
            );
        }
    }

    public String getHint() {

        List<String> possibleWords =
                dictionary.findWordsMatchingHints(
                        guesses,
                        hints
                );

        possibleWords.removeAll(usedHints);

        if (possibleWords.isEmpty()) {
            return null;
        }

        String suggestedWord = possibleWords.get(0);

        usedHints.add(suggestedWord);

        return suggestedWord;
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