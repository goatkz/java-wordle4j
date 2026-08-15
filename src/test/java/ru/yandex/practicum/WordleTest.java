package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleTest {

    @Test
    void shouldNormalizeWord() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой")
        );

        assertEquals("герой", dictionary.normalizeWord(" ГЕРОЙ "));
        assertEquals("елка", dictionary.normalizeWord("ЁЛКА"));
    }

    @Test
    void shouldValidateWordLength() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой")
        );

        assertTrue(dictionary.isValidLength("герой"));
        assertFalse(dictionary.isValidLength("дом"));
    }

    @Test
    void shouldValidateRussianLetters() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой")
        );

        assertTrue(dictionary.containsOnlyRussianLetters("герой"));
        assertFalse(dictionary.containsOnlyRussianLetters("hello"));
    }

    @Test
    void shouldFindWordInDictionary() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой", "гонец")
        );

        assertTrue(dictionary.containsWord("герой"));
        assertFalse(dictionary.containsWord("домик"));
    }

    @Test
    void shouldBuildCorrectHint() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("герой")
        );

        assertEquals(
                "+^-^-",
                dictionary.buildHint("герой", "гонец")
        );
    }
}