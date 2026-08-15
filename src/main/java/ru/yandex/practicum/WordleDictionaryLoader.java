package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private static final int REQUIRED_WORD_LENGTH = 5;

    public WordleDictionary loadDictionary(final String filename)
            throws IOException {

        List<String> validWords = new ArrayList<>();

        try (BufferedReader dictionaryReader = new BufferedReader(
                new FileReader(filename, StandardCharsets.UTF_8))) {

            String dictionaryLine;

            while ((dictionaryLine = dictionaryReader.readLine()) != null) {

                String normalizedWord = normalizeWord(dictionaryLine);

                if (isSuitableWord(normalizedWord)) {
                    validWords.add(normalizedWord);
                }
            }
        }

        if (validWords.isEmpty()) {
            throw new IllegalArgumentException(
                    "В словаре нет подходящих слов."
            );
        }

        return new WordleDictionary(validWords);
    }

    private String normalizeWord(final String word) {
        return word
                .trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    private boolean isSuitableWord(final String word) {
        if (word.length() != REQUIRED_WORD_LENGTH) {
            return false;
        }

        for (int position = 0; position < word.length(); position++) {

            char currentCharacter = word.charAt(position);

            if (currentCharacter < 'а'
                    || currentCharacter > 'я') {
                return false;
            }
        }

        return true;
    }
}