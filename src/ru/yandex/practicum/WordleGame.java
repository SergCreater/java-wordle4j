package ru.yandex.practicum;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;                //загаданное слово
    private int steps;                    //попытки
    private boolean gameWon;              //статус игры
    private WordleDictionary dictionary;  //словарь

    private List<String> guesses;       //введённые слова
    private List<String> hints;           //подсказки

    private Set<Character> wrongLetter;    //буквы которых нет
    private Map<Character, Set<Integer>> correctPositions;// буквы на нужных позициях
    private Map<Character, Set<Integer>> wrongPositions;//буквы которые есть, но не на тех местах

    private Set<String> usedSuggestions;    //предложенные подсказки

    private GameLogger logger;

    public WordleGame(String answer, WordleDictionary wordleDictionary, GameLogger logger) {
        this.answer = answer;
        this.gameWon = false;
        this.steps = 0;
        this.guesses = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.dictionary = wordleDictionary;
        this.logger = logger;
        this.usedSuggestions = new HashSet<>();
        this.wrongLetter = new HashSet<>();
        this.correctPositions = new HashMap<>();
        this.wrongPositions = new HashMap<>();
    }

    public String getHit() {

        List<String> allWords = dictionary.getAllWords();
        List<String> variableWords = new ArrayList<>(allWords);

        variableWords.removeAll(guesses);
        variableWords.removeAll(hints);
        variableWords.removeAll(usedSuggestions);

        variableWords.removeIf(word -> !matchesCurrentState(word));

        if (variableWords.isEmpty()) {
            return "Подсказок нет.";
        }
        Random rand = new Random();
        String hint = variableWords.get(rand.nextInt(variableWords.size()));
        hints.add(hint);
        usedSuggestions.add(hint);
        return hint;
    }

    public boolean hasHit() {
        List<String> allWords = dictionary.getAllWords();
        for (String word : allWords) {
            if (!guesses.contains(word) && !hints.contains(word) && matchesCurrentState(word)) {
                return true;
            }
        }
        return false;
    }

    public String makeGuess(String word) {
        if (gameWon) {
            throw new IllegalStateException("Игра уже выиграна.");
        }
        if (steps >= Config.MAX_ATTEMPTS) {
            throw new IllegalArgumentException("Вы использовали все попытки.");
        }
        if (word.length() != answer.length()) {
            throw new IllegalArgumentException("Длина слова не совпала.");
        }
        if (!dictionary.contains(word)) {
            throw new IllegalArgumentException(word + " отсутствует в словаре.");
        }
        steps++;
        guesses.add(word);

        String result = compareWords(word, answer);
        updateAnalis(word, result);

        if (result.equals("+".repeat(answer.length()))) {
            gameWon = true;
        }
        return result;
    }

    public String compareWords(String word, String answer) {
        int length = answer.length();
        StringBuilder result = new StringBuilder();
        boolean[] wordChar = new boolean[length];

        for (int i = 0; i < length; i++) {
            if (word.charAt(i) == answer.charAt(i)) {
                result.append('+');
                wordChar[i] = true;
            } else {
                result.append(' ');
            }
        }
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) == '+') {
                continue;
            }
            char charsterInword = word.charAt(i);
            boolean found = false;

            for (int j = 0; j < length; j++) {
                if (!wordChar[j] && answer.charAt(j) == charsterInword) {
                    found = true;
                    wordChar[j] = true;
                    break;
                }
            }
            if (found) {
                result.setCharAt(i, '^');
            } else {
                result.setCharAt(i, '-');
            }
        }
        return result.toString();
    }

    public void updateAnalis(String word, String result) {
        for (int i = 0; i < word.length(); i++) {
            char w = word.charAt(i);
            char r = result.charAt(i);
            if (r == '+') {
                correctPositions.computeIfAbsent(w, k -> new HashSet<>()).add(i);
            } else if (r == '^') {
                wrongPositions.computeIfAbsent(w, k -> new HashSet<>()).add(i);
            } else if (r == '-') {
                if (!correctPositions.containsKey(w) && !wrongPositions.containsKey(w)) {
                    wrongLetter.add(w);
                }
            }
        }
    }

    public boolean matchesCurrentState(String word) {
        for (char c : wrongLetter) {
            if (word.indexOf(c) != -1) {
                return false;
            }
        }
        for (Map.Entry<Character, Set<Integer>> entry : correctPositions.entrySet()) {
            char c = entry.getKey();
            for (int pos : entry.getValue()) {
                if (word.charAt(pos) != c) {
                    return false;
                }
            }
        }
        for (Map.Entry<Character, Set<Integer>> entry : wrongPositions.entrySet()) {
            char required = entry.getKey();
            Set<Integer> unlegitPos = entry.getValue();

            if (word.indexOf(required) == -1) {
                return false;
            }
            for (int pos : unlegitPos) {
                if (word.charAt(pos) == required) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getSteps() {
        return steps;
    }
}
