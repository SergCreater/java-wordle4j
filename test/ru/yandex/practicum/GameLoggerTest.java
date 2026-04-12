package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;

class GameLoggerTest {

    private static final String TEST_LOG_FILE = "test_wordle.log";
    private GameLogger logger;

    @AfterEach
    void tearDown() throws IOException {
        if (logger != null) {
            logger.close();
        }
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
    }

    @Test
    @DisplayName("Создание логгера и запись информационного сообщения")
    void testLogInfo() throws IOException {
        logger = new GameLogger(TEST_LOG_FILE);
        logger.info("Тестовое сообщение");
        logger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("[Info] Тестовое сообщение"));
    }

    @Test
    @DisplayName("Запись сообщения об ошибке")
    void testLogError() throws IOException {
        logger = new GameLogger(TEST_LOG_FILE);
        logger.error("Тестовая ошибка");
        logger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("[Error] Тестовая ошибка"));
    }

    @Test
    @DisplayName("Множественная запись в лог")
    void testMultipleLogEntries() throws IOException {
        logger = new GameLogger(TEST_LOG_FILE);
        logger.info("Сообщение 1");
        logger.info("Сообщение 2");
        logger.error("Ошибка 1");
        logger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("Сообщение 1"));
        assertTrue(content.contains("Сообщение 2"));
        assertTrue(content.contains("Ошибка 1"));
    }

    @Test
    @DisplayName("Закрытие логгера")
    void testLoggerClose() throws IOException {
        logger = new GameLogger(TEST_LOG_FILE);
        logger.info("До закрытия");
        logger.close();

        // Проверяем, что файл существует и доступен для чтения
        assertTrue(Files.exists(Paths.get(TEST_LOG_FILE)));

        // Попытка записи после закрытия вызовет исключение
        // (не тестируем напрямую, так как это может привести к непредсказуемому поведению)
    }
}