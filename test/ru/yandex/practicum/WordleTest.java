package ru.yandex.practicum;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Базовые тесты конфигурации Wordle")
class WordleTest {

    @Test
    @DisplayName("Проверка констант конфигурации")
    void testConfigConstants() {
        assertEquals(4, Config.MIN_WORD_LENGTH, "Минимальная длина слова должна быть 4");
        assertEquals(8, Config.MAX_WORD_LENGTH, "Максимальная длина слова должна быть 8");
        assertEquals(6, Config.MAX_ATTEMPTS, "Максимальное количество попыток должно быть 6");
        assertEquals("words_ru.txt", Config.DICTIONARY_FILE_PATH, "Путь к файлу словаря");
        assertEquals("test_wordle.log", Config.TEST_LOG_FILE, "Имя лог-файла");
    }

    @Test
    @DisplayName("Проверка диапазона длин слов")
    void testWordLengthRange() {
        assertTrue(Config.MIN_WORD_LENGTH < Config.MAX_WORD_LENGTH,
                "Минимальная длина должна быть меньше максимальной");
        assertTrue(Config.MIN_WORD_LENGTH > 0,
                "Минимальная длина должна быть положительной");
        assertTrue(Config.MAX_WORD_LENGTH > 0,
                "Максимальная длина должна быть положительной");
    }

    @Test
    @DisplayName("Проверка количества попыток")
    void testMaxAttempts() {
        assertTrue(Config.MAX_ATTEMPTS > 0,
                "Количество попыток должно быть положительным");
        assertTrue(Config.MAX_ATTEMPTS <= 10,
                "Количество попыток не должно превышать разумный лимит");
    }
}