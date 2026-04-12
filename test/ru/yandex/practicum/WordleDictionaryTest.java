package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private Map<Integer, List<String>> testWords;
    private GameLogger testLogger;

    @BeforeEach
    void setUp() {
        try {
            testLogger = new GameLogger("test_dict.log");
        } catch (Exception e) {
            testLogger = null;
        }

        testWords = new HashMap<>();
        testWords.put(4, Arrays.asList("кот", "дом", "сон", "лес", "ёлка"));
        testWords.put(5, Arrays.asList("герой", "гонец", "слово", "игра"));
        testWords.put(6, Arrays.asList("молоко", "корова", "собака"));

        dictionary = new WordleDictionary(testWords, testLogger);
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