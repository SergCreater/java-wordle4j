package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private GameLogger logger;
    private final Map<Integer, List<String>> wordsByLength;
    private int selectedLength;

    public WordleDictionary(Map<Integer, List<String>> wordsByLength, GameLogger logger) {
        this.wordsByLength = wordsByLength;
        this.logger = logger;
    }

    public String getRandomWord() {
        if (selectedLength == 0) {
            throw new IllegalStateException("Длина слова не выбрана.");
        }
        List<String> words = wordsByLength.get(selectedLength);
        Random random = new Random();
        return words.get(random.nextInt(words.size()));

    }

    public void setSelectedLength(int length) {
        if (length < Config.MIN_WORD_LENGTH || length > Config.MAX_WORD_LENGTH) {
            throw new IllegalArgumentException("Длина должна быть от " +
                    Config.MIN_WORD_LENGTH + " до " + Config.MAX_WORD_LENGTH);
        }
        if (!wordsByLength.containsKey(length) || wordsByLength.get(length).isEmpty()) {
            throw new IllegalStateException("Нет слов длины " + length + " в словаре");
        }
        this.selectedLength = length;
    }

    public boolean contains(String word) {
        if (word.length() != selectedLength) {
            return false;
        }
        word = word.toLowerCase().replace('ё', 'е');
        List<String> words = wordsByLength.get(selectedLength);
        return words != null && words.contains(word);
    }

    public List<String> getAllWords() {
        return new ArrayList<>(wordsByLength.get(selectedLength));
    }

    public int getSelectedLength() {
        return selectedLength;
    }

    public int size() {
        return wordsByLength.get(selectedLength).size();
    }

    public boolean isValidRussianWord(String word) {
        if (word == null || word.length() != selectedLength) {
            return false;
        }
        return word.matches("[а-яА-ЯёЁ]+");
    }
}
