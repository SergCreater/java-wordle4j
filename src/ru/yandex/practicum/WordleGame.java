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


    public WordleGame(WordleDictionary dictionary, int lengthWord) {
        this.answer = dictionary.getRandomWord(lengthWord);
        this.steps = 6;
        this.gameWon = false;
        this.dictionary = dictionary;
        this.guesses = new ArrayList<String>();
        this.wrongLetter = new HashSet<Character>();
        this.hints = new ArrayList<String>();
        this.correctPositions = new HashMap<Character, Set<Integer>>();
        this.wrongPositions = new HashMap<Character, Set<Integer>>();
        this.usedSuggestions = new HashSet<String>();
    }

    public String makeGuess(String word, int length){
        word = word.toLowerCase().replace("ё", "е");
        dictionary
    }
}
