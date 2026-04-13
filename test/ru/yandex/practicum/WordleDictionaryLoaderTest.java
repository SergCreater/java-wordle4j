package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.SystemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    private WordleDictionaryLoader loader;
    private GameLogger testLogger;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        try {
            testLogger = new GameLogger(Config.TEST_LOG_FILE);
        } catch (Exception e) {
            testLogger = null;
        }

        loader = new WordleDictionaryLoader(testLogger);

        // Создаем временный тестовый файл
        testFilePath = Paths.get(Config.DICTIONARY_FILE_PATH);
        List<String> testWords = Arrays.asList(
                "кот", "дом", "сон",
                "герой", "гонец", "слово",
                "молоко", "корова",
                "ёлка", "ЁЖИК",
                "оченьдлинноеслово", // Должно быть отфильтровано
                "да"                   // Должно быть отфильтровано
        );
        Files.write(testFilePath, testWords, java.nio.charset.StandardCharsets.UTF_8);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
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
    @DisplayName("Загрузка и нормализация словаря")
    void testLoadDictionaryNormalization() throws SystemException {
        Map<Integer, List<String>> dictionary = loader.loadDictionary();

        assertNotNull(dictionary);

        // Проверяем нормализацию ё -> е
        List<String> words4 = dictionary.get(4);
        assertTrue(words4.contains("елка"));  // ёлка -> елка
        assertTrue(words4.contains("ежик"));  // ёжик -> ежик

        // Проверяем приведение к нижнему регистру
        for (List<String> words : dictionary.values()) {
            for (String word : words) {
                assertEquals(word, word.toLowerCase());
            }
        }
    }

    @Test
    @DisplayName("Фильтрация слов по длине")
    void testLoadDictionaryLengthFilter() throws SystemException {
        Map<Integer, List<String>> dictionary = loader.loadDictionary();

        // Проверяем, что все слова в допустимом диапазоне
        for (int len : dictionary.keySet()) {
            assertTrue(len >= Config.MIN_WORD_LENGTH);
            assertTrue(len <= Config.MAX_WORD_LENGTH);
        }

        // Проверяем, что слишком короткие и длинные слова отфильтрованы
        List<String> words4 = dictionary.get(4);
        assertFalse(words4.contains("да")); // Слишком короткое

        assertFalse(dictionary.containsKey(15)); // Слишком длинное
    }

    @Test
    @DisplayName("Проверка существования файла")
    void testFileExistence() throws IOException {
        Files.deleteIfExists(testFilePath);

        SystemException exception = assertThrows(SystemException.class, () -> {
            loader.loadDictionary();
        });

        assertTrue(exception.getMessage().contains("Файл словаря не найден"));
    }
}