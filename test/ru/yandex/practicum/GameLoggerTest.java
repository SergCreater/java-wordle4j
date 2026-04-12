package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.Config.TEST_LOG_FILE;

class GameLoggerTest {


    private GameLogger testLogger;

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
    @DisplayName("Создание логгера и запись информационного сообщения")
    void testLogInfo() throws IOException {
        testLogger = new GameLogger(TEST_LOG_FILE);
        testLogger.info("Тестовое сообщение");
        testLogger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("[Info] Тестовое сообщение"));
    }

    @Test
    @DisplayName("Запись сообщения об ошибке")
    void testLogError() throws IOException {
        testLogger = new GameLogger(TEST_LOG_FILE);
        testLogger.error("Тестовая ошибка");
        testLogger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("[Error] Тестовая ошибка"));
    }

    @Test
    @DisplayName("Множественная запись в лог")
    void testMultipleLogEntries() throws IOException {
        testLogger = new GameLogger(TEST_LOG_FILE);
        testLogger.info("Сообщение 1");
        testLogger.info("Сообщение 2");
        testLogger.error("Ошибка 1");
        testLogger.close();

        String content = Files.readString(Paths.get(TEST_LOG_FILE));
        assertTrue(content.contains("Сообщение 1"));
        assertTrue(content.contains("Сообщение 2"));
        assertTrue(content.contains("Ошибка 1"));
    }

    @Test
    @DisplayName("Закрытие логгера")
    void testLoggerClose() throws IOException {
        testLogger = new GameLogger(TEST_LOG_FILE);
        testLogger.info("До закрытия");
        testLogger.close();

        // Проверяем, что файл существует и доступен для чтения
        assertTrue(Files.exists(Paths.get(TEST_LOG_FILE)));

        // Попытка записи после закрытия вызовет исключение
        // (не тестируем напрямую, так как это может привести к непредсказуемому поведению)
    }
}