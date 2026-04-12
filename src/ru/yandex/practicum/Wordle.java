package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.SystemException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameLogger logger = null;

        try {
            logger = new GameLogger(Config.LOG_FILE);
            logger.info("Игра запущенна");
        } catch (IOException ex) {
            System.err.println("Не удалось создать лог-файл: " + ex.getMessage());
            return;
//        } catch (RuntimeException ex) {
//            logger.error("Ошибка загрузки словаря: " + ex.getMessage());
//            System.out.println("Ошибка загрузки словаря");
//            return;
        }

        System.out.println("Добро пожаловать в Wordle!");
        System.out.println("Игра началась.");
        System.out.println();

        WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
        Map<Integer, List<String>> allWords = new HashMap<>();

        try {
            allWords = loader.loadDictionary();
            System.out.println("Словарь добавлен.");
        } catch (SystemException e) {
            logger.error("Ошибка загрузки словаря: " + e.getMessage());
            System.out.println("Ошибка загрузки словаря: " + e.getMessage());
            return;
        }

        WordleDictionary dictionary = new WordleDictionary(allWords, logger);

        System.out.println("Выберите длину отгадываемого слова: ");
        System.out.println("Длина доступных для игры слов от " + Config.MIN_WORD_LENGTH +
                " до " + Config.MAX_WORD_LENGTH + ".");

        while (true) {
            try {
                String lenth = sc.nextLine().trim();
                int wordLenth = Integer.parseInt(lenth);
                dictionary.setSelectedLength(wordLenth);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка ввода, число должно быть не меньше: " +
                        Config.MIN_WORD_LENGTH + " и не больше " +
                        Config.MAX_WORD_LENGTH + ".");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Попробуйте ещё раз.");
            }
        }

        System.out.println();

        String secret = dictionary.getRandomWord();
        WordleGame game = new WordleGame(secret, dictionary, logger);

        String input;
        while (!game.isGameWon() && game.getSteps() < Config.MAX_ATTEMPTS) {
            System.out.println("У вас " + (Config.MAX_ATTEMPTS - game.getSteps()) + " попыток.");
            input = sc.nextLine().trim().toLowerCase();
            //sc.nextLine();
            if (input.isEmpty()) {
                if (game.hasHit()) {
                    String hit = game.getHit();
                    System.out.println("Подсказка: " + hit);
                } else {
                    System.out.println("Подсказок больше нет.");
                }
                continue;
            }
            if (!dictionary.isValidRussianWord(input)) {
                System.out.println("Слово должно содержать только русские буквы.");
                continue;
            }

            try {
                String result = game.makeGuess(input);
                System.out.println("Результат: " + result);

               /* if (input.equals(secret)) {
                    System.out.println();
                    System.out.println("Вы угадали слово! Победа!!!");

                } else if (game.getSteps() <= 0) {
                    System.out.println();
                    System.out.println("Вам не удалось угадать загаданное слово.");
                    System.out.println("Слово которое мы загадали: " + secret);

                }*/
            } catch (IllegalArgumentException ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            } catch (IllegalStateException ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            }
            //sc.next();
        }
        if (game.isGameWon()) {
            System.out.println("Вы угадали слово за: " + game.getSteps() + " шагов.");
        } else {
            System.out.println("Слово не было отгадано, загаданное слово: " + secret);
        }
        if (logger != null) {
            logger.close();
        }
        sc.close();
    }

}
