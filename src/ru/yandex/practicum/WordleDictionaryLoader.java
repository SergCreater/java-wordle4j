package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.DictionaryNotFoundException;
import ru.yandex.practicum.exceptions.SystemException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.nio.*;

//import static jdk.internal.reflect.ConstantPool.Tag.UTF8;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    GameLogger logger;
    public WordleDictionaryLoader(GameLogger logger){
        this.logger = logger;
    }
    //private Map<Integer, List<String>> wordByLength;
    Path filePath = Paths.get(Config.DICTIONARY_FILE_PATH);

    public Map<Integer, List<String>> loadDictionary() throws SystemException {
    logger.info("Загрузка словаря из файла: " + Config.DICTIONARY_FILE_PATH);
        if(!Files.exists(filePath)){
            logger.error("Файл словаря не найден: " + Config.DICTIONARY_FILE_PATH);
            throw new SystemException("Файл словаря не найден: " + Config.DICTIONARY_FILE_PATH);
        }
    logger.info("Файл загружен.");
        Map<Integer, List<String>> wordByLength = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(Config.DICTIONARY_FILE_PATH), StandardCharsets.UTF_8))) {

            String word;

            while ((word = br.readLine()) != null) {
                word = word.toLowerCase().trim().replace("ё", "е");
                if (word.length() <= Config.MAX_WORD_LENGTH && word.length() >= Config.MIN_WORD_LENGTH) {
                    int length = word.length();
                    List<String> list = wordByLength.get(length);
                    if (list == null) {
                        list = new ArrayList<>();
                        wordByLength.put(length, list);
                    }
                    list.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DictionaryNotFoundException("Файл словаря не найден: " +
                    Config.DICTIONARY_FILE_PATH);
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка чтения файла словаря: " +
                    Config.DICTIONARY_FILE_PATH, e);
        }
        for (int len = Config.MIN_WORD_LENGTH; len <= Config.MAX_WORD_LENGTH; len++) {
            if (!wordByLength.containsKey(len) || wordByLength.get(len).isEmpty()) {
                throw new IllegalStateException("Нет слов длины " + len + " в словаре");
            }
        }
        return wordByLength;
    }

   /* public List<String> getAllWord(int length){
        List<String> words = wordByLength.get(length);
        return words == null ? new ArrayList<>() : new ArrayList<>(words);
    }*/
}
