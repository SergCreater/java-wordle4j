package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.practicum.exceptions.SystemException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

class WordleDictionaryLoaderTest {

    private WordleDictionaryLoader loader;
    private GameLogger testLogger;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        try {
            testLogger = new GameLogger("test_loader.log");
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