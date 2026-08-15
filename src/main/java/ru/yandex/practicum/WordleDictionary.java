package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {

    private static final int WORD_LENGTH = 5;

    private final List<String> words;
    private final Set<String> wordSet;
    private final Random randomGenerator;

    public WordleDictionary(final List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь пуст.");
        }

        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
        this.randomGenerator = new Random();
    }

    public boolean containsWord(final String word) {
        String normalizedWord = normalizeWord(word);

        return wordSet.contains(normalizedWord);
    }

    public String getRandomWord() {
        int randomIndex = randomGenerator.nextInt(words.size());

        return words.get(randomIndex);
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public int getWordCount() {
        return words.size();
    }

    public String normalizeWord(final String word) {
        if (word == null) {
            return null;
        }

        return word
                .trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    public boolean isValidLength(final String word) {
        return word != null && word.length() == WORD_LENGTH;
    }

    public boolean containsOnlyRussianLetters(final String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        for (int position = 0; position < word.length(); position++) {
            char currentCharacter = word.charAt(position);

            if (currentCharacter < 'а' || currentCharacter > 'я') {
                return false;
            }
        }

        return true;
    }

    public List<String> findWordsMatchingHints(
            final List<String> guesses,
            final List<String> hints
    ) {
        List<String> matchingWords = new ArrayList<>();

        for (String candidateWord : words) {
            boolean matchesAllHints = true;

            for (int guessIndex = 0;
                 guessIndex < guesses.size();
                 guessIndex++) {

                String guessedWord = guesses.get(guessIndex);
                String expectedHint = hints.get(guessIndex);

                String candidateHint = buildHint(
                        candidateWord,
                        guessedWord
                );

                if (!candidateHint.equals(expectedHint)) {
                    matchesAllHints = false;
                    break;
                }
            }

            if (matchesAllHints) {
                matchingWords.add(candidateWord);
            }
        }

        return matchingWords;
    }

    public String buildHint(
            final String correctWord,
            final String guessedWord
    ) {
        char[] hintResult = new char[WORD_LENGTH];

        boolean[] usedAnswerPositions =
                new boolean[WORD_LENGTH];

        for (int position = 0; position < WORD_LENGTH; position++) {
            if (guessedWord.charAt(position)
                    == correctWord.charAt(position)) {

                hintResult[position] = '+';
                usedAnswerPositions[position] = true;
            }
        }

        for (int guessedPosition = 0;
             guessedPosition < WORD_LENGTH;
             guessedPosition++) {

            if (hintResult[guessedPosition] == '+') {
                continue;
            }

            boolean matchingLetterFound = false;

            for (int answerPosition = 0;
                 answerPosition < WORD_LENGTH;
                 answerPosition++) {

                if (!usedAnswerPositions[answerPosition]
                        && guessedWord.charAt(guessedPosition)
                        == correctWord.charAt(answerPosition)) {

                    matchingLetterFound = true;
                    usedAnswerPositions[answerPosition] = true;
                    break;
                }
            }

            hintResult[guessedPosition] =
                    matchingLetterFound ? '^' : '-';
        }

        return new String(hintResult);
    }
}