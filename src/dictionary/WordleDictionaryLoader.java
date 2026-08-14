package dictionary;

import exceptions.DictionaryLoadException;
import exceptions.EmptyDictionaryException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private static final int REQUIRED_WORD_LENGTH = 5;

    public WordleDictionary loadDictionary(final String filename)
            throws DictionaryLoadException, EmptyDictionaryException {

        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename, StandardCharsets.UTF_8))) {

            while (reader.ready()) {
                String word = reader.readLine();

                if (word == null) {
                    continue;
                }

                word = normalizeWord(word);

                if (isSuitableWord(word)) {
                    words.add(word);
                }
            }

        } catch (IOException exception) {
            throw new DictionaryLoadException(
                    "Не удалось загрузить словарь.",
                    exception
            );
        }

        if (words.isEmpty()) {
            throw new EmptyDictionaryException(
                    "В словаре нет подходящих слов."
            );
        }

        return new WordleDictionary(words);
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

        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);

            if (character < 'а' || character > 'я') {
                return false;
            }
        }

        return true;
    }
}