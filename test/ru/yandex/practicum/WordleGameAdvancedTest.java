package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class WordleGameAdvancedTest {

    private WordleGame game;
    private WordleDictionary dictionary;
    private GameLogger testLogger;
    private Map<Integer, List<String>> testWords;

    @BeforeEach
    void setUp() {
        try {
            testLogger = new GameLogger("test_game_advanced.log");
        } catch (Exception e) {
            testLogger = null;
        }

        testWords = new HashMap<>();
        testWords.put(5, new ArrayList<>(Arrays.asList(
                "герой", "гонец", "слово", "игра", "книга",
                "мышка", "кошка", "окно", "стол", "стул",
                "лампа", "ручка", "тетрадь", "касса", "масса"
        )));

        dictionary = new WordleDictionary(testWords, testLogger);
        dictionary.setSelectedLength(5);
    }

    @Test
    @DisplayName("Сравнение слов с повторяющимися буквами - сложный случай")
    void testCompareWordsWithMultipleOccurrences() {
        game = new WordleGame("касса", dictionary, testLogger);

        // В слове "салат": с - есть, а - есть (но их две), л - нет, а - есть, т - нет
        String result = game.compareWords("салат", "касса");
        assertEquals("^^---", result);

        // В слове "масса": м - нет, а - правильно, с - есть но не там, с - правильно, а - правильно
        String result2 = game.compareWords("масса", "касса");
        assertEquals("-+^++", result2);
    }

    @Test
    @DisplayName("Подсказки не содержат исключенные буквы")
    void testHintsExcludeWrongLetters() {
        game = new WordleGame("герой", dictionary, testLogger);

        // Делаем ход, где точно нет определенных букв
        game.makeGuess("мышка"); // м, ы, ш, к - отсутствуют

        // Все подсказки не должны содержать эти буквы
        for (int i = 0; i < 3 && game.hasHit(); i++) {
            String hint = game.getHit();
            assertFalse(hint.contains("м"));
            assertFalse(hint.contains("ы"));
            assertFalse(hint.contains("ш"));
            assertFalse(hint.contains("к"));
        }
    }

    @Test
    @DisplayName("Подсказки учитывают правильные позиции")
    void testHintsRespectCorrectPositions() {
        game = new WordleGame("герой", dictionary, testLogger);

        game.makeGuess("гонец"); // 'г' на позиции 0 - правильно

        for (int i = 0; i < 3 && game.hasHit(); i++) {
            String hint = game.getHit();
            assertEquals('г', hint.charAt(0), "Подсказка должна начинаться с 'г'");
        }
    }

    @Test
    @DisplayName("Подсказки содержат буквы, которые есть в слове")
    void testHintsContainPresentLetters() {
        game = new WordleGame("герой", dictionary, testLogger);

        game.makeGuess("гонец"); // 'о' и 'е' есть в слове

        boolean foundO = false;
        boolean foundE = false;

        for (int i = 0; i < 5 && game.hasHit(); i++) {
            String hint = game.getHit();
            if (hint.contains("о")) foundO = true;
            if (hint.contains("е")) foundE = true;
        }

        assertTrue(foundO, "Хотя бы одна подсказка должна содержать 'о'");
        assertTrue(foundE, "Хотя бы одна подсказка должна содержать 'е'");
    }

    @Test
    @DisplayName("Обновление анализа корректно обрабатывает все символы")
    void testUpdateAnalisAllSymbols() {
        game = new WordleGame("герой", dictionary, testLogger);

        String result = "+^-^-"; // г - +, о - ^, н - -, е - ^, ц - -
        game.updateAnalis("гонец", result);

        // Проверяем через matchesCurrentState
        assertTrue(game.matchesCurrentState("герой"));  // правильное слово
        assertTrue(game.matchesCurrentState("город"));  // г на месте, есть о и е
        assertFalse(game.matchesCurrentState("гонец")); // содержит н и ц
        assertFalse(game.matchesCurrentState("слово")); // не начинается с г
    }

    @Test
    @DisplayName("Исключение при попытке сделать ход после победы")
    void testMakeGuessAfterWin() {
        game = new WordleGame("герой", dictionary, testLogger);

        game.makeGuess("герой");
        assertTrue(game.isGameWon());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            game.makeGuess("слово");
        });

        assertEquals("Игра уже выиграна.", exception.getMessage());
    }

    @Test
    @DisplayName("Исключение при превышении лимита попыток")
    void testMakeGuessExceedLimit() {
        game = new WordleGame("герой", dictionary, testLogger);

        // Делаем максимальное количество попыток
        for (int i = 0; i < Config.MAX_ATTEMPTS; i++) {
            game.makeGuess("слово");
        }

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            game.makeGuess("игра");
        });

        assertEquals("Вы использовали все попытки.", exception.getMessage());
    }
}