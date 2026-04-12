package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class WordleIntegrationTest {

    private GameLogger testLogger;
    private WordleDictionary dictionary;
    private Map<Integer, List<String>> testWords;

    @BeforeEach
    void setUp() {
        try {
            testLogger = new GameLogger("test_integration.log");
        } catch (Exception e) {
            testLogger = null;
        }

        testWords = new HashMap<>();
        testWords.put(5, new ArrayList<>(Arrays.asList(
                "герой", "гонец", "слово", "игра", "книга",
                "мышка", "кошка", "окно", "стол", "стул",
                "лампа", "ручка", "тетрадь", "слава"
        )));

        dictionary = new WordleDictionary(testWords, testLogger);
        dictionary.setSelectedLength(5);
    }

    @Test
    @DisplayName("Полный игровой сценарий - победа")
    void testFullGameScenarioWin() {
        String secret = "герой";
        WordleGame game = new WordleGame(secret, dictionary, testLogger);

        String result1 = game.makeGuess("слава");
        assertEquals("-----", result1);
        assertEquals(1, game.getSteps());
        assertFalse(game.isGameWon());

        String result2 = game.makeGuess("гонец");
        assertEquals("+^-^-", result2);
        assertEquals(2, game.getSteps());

        assertTrue(game.hasHit());
        String hint = game.getHit();
        assertNotNull(hint);
        assertEquals('г', hint.charAt(0));

        String result3 = game.makeGuess("герой");
        assertEquals("+++++", result3);
        assertTrue(game.isGameWon());
        assertEquals(3, game.getSteps());
    }

    @Test
    @DisplayName("Полный игровой сценарий - проигрыш")
    void testFullGameScenarioLose() {
        String secret = "герой";
        WordleGame game = new WordleGame(secret, dictionary, testLogger);

        for (int i = 0; i < Config.MAX_ATTEMPTS; i++) {
            assertFalse(game.isGameWon());
            game.makeGuess("слово");
        }

        assertEquals(Config.MAX_ATTEMPTS, game.getSteps());
        assertFalse(game.isGameWon());

        assertThrows(IllegalArgumentException.class, () -> {
            game.makeGuess("игра");
        });
    }

    @Test
    @DisplayName("Сценарий использования подсказок")
    void testHintScenario() {
        String secret = "герой";
        WordleGame game = new WordleGame(secret, dictionary, testLogger);

        assertTrue(game.hasHit());

        Set<String> hints = new HashSet<>();
        for (int i = 0; i < 5 && game.hasHit(); i++) {
            String hint = game.getHit().trim().toLowerCase();
            hints.add(hint);
            assertTrue(dictionary.contains(hint));
        }

        assertTrue(hints.size() > 1);

        game.makeGuess("игра");

        assertTrue(game.hasHit());
    }
}