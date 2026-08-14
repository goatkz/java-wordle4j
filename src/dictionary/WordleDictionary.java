package dictionary;

import exceptions.EmptyDictionaryException;

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
    private final Random random;

    public WordleDictionary(final List<String> words) throws EmptyDictionaryException {
        if (words == null || words.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст.");
        }

        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
        this.random = new Random();
    }

    public boolean containsWord(final String word) {
        return wordSet.contains(normalizeWord(word));
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
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

        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);

            if (character < 'а' || character > 'я') {
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

        for (String candidate : words) {
            boolean matchesAllHints = true;

            for (int i = 0; i < guesses.size(); i++) {
                String guess = guesses.get(i);
                String hint = hints.get(i);

                String candidateHint = buildHint(candidate, guess);

                if (!candidateHint.equals(hint)) {
                    matchesAllHints = false;
                    break;
                }
            }

            if (matchesAllHints) {
                matchingWords.add(candidate);
            }
        }

        return matchingWords;
    }

    public String buildHint(final String answer, final String guess) {
        char[] result = new char[WORD_LENGTH];
        boolean[] usedAnswerLetters = new boolean[WORD_LENGTH];

        // Сначала ищем буквы на правильных позициях.
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                usedAnswerLetters[i] = true;
            }
        }

        // Затем ищем буквы, которые есть в слове,
        // но находятся в другом месте.
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (result[i] == '+') {
                continue;
            }

            boolean found = false;

            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!usedAnswerLetters[j]
                        && guess.charAt(i) == answer.charAt(j)) {

                    found = true;
                    usedAnswerLetters[j] = true;
                    break;
                }
            }

            result[i] = found ? '^' : '-';
        }

        return new String(result);
    }
}