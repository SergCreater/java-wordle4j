package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private Map<Integer, List<String>> testWords;
    private GameLogger testLogger;

    @BeforeEach
    void setUp() {
        try {
            testLogger = new GameLogger(Config.TEST_LOG_FILE);
        } catch (Exception e) {
            testLogger = null;
        }

        testWords = new HashMap<>();
        testWords.put(4, Arrays.asList("коты", "дома", "соня", "леса", "ёлка", "елка"));
        testWords.put(5, Arrays.asList("герой", "гонец", "слово", "игра"));
        testWords.put(6, Arrays.asList("молоко", "корова", "собака"));

        dictionary = new WordleDictionary(testWords, testLogger);
    }

    @AfterEach
    void closeLogger() {
        if (testLogger != null) {
            try {
                testLogger.close();
            } catch (Exception ignore) {
            }
        }
        try {
            java.nio.file.Files.deleteIfExists(
                    java.nio.file.Paths.get(Config.TEST_LOG_FILE));
        } catch (java.io.IOException ignore) {
        }
    }

    @Test
    @DisplayName("Нормализация слова в contains - замена ё на е")
    void testContainsWithYo() {
        dictionary.setSelectedLength(4);

        // В словаре "ёлка" хранится как "елка" после нормализации в загрузчике
        // Но мы добавили "ёлка" напрямую, нужно нормализовать
        assertTrue(dictionary.contains("ёлка"));
        assertTrue(dictionary.contains("елка"));
        assertTrue(dictionary.contains("ЁЛКА"));
    }

    @Test
    @DisplayName("Нормализация регистра в contains")
    void testContainsCaseInsensitive() {
        dictionary.setSelectedLength(5);

        assertTrue(dictionary.contains("ГЕРОЙ"));
        assertTrue(dictionary.contains("Герой"));
        assertTrue(dictionary.contains("герой"));
    }

    @Test
    @DisplayName("Валидация русских слов с разным регистром")
    void testIsValidRussianWordCaseInsensitive() {
        dictionary.setSelectedLength(5);

        assertTrue(dictionary.isValidRussianWord("ГЕРОЙ"));
        assertTrue(dictionary.isValidRussianWord("Герой"));
        assertTrue(dictionary.isValidRussianWord("герой"));
        assertFalse(dictionary.isValidRussianWord("hello"));
        assertFalse(dictionary.isValidRussianWord("герой123"));
    }

    @Test
    @DisplayName("Валидация слов с буквой Ё в разных регистрах")
    void testIsValidRussianWordWithYo() {
        dictionary.setSelectedLength(4);

        assertTrue(dictionary.isValidRussianWord("ёлка"));
        assertTrue(dictionary.isValidRussianWord("ЁЛКА"));
        assertTrue(dictionary.isValidRussianWord("Ёлка"));
    }

    @Test
    @DisplayName("Установка длины с несуществующей длиной")
    void testSetSelectedLengthNonExistent() {
        assertThrows(IllegalStateException.class, () -> {
            dictionary.setSelectedLength(7); // Нет слов длины 7
        });
    }
}