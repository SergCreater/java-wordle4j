package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

//import static jdk.internal.reflect.ConstantPool.Tag.UTF8;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private Map<Integer, List<String>> wordByLength = new HashMap<>();
    String filePath = Config.DICTIONARY_FILE_PATH;

    public Map<Integer, List<String>> loadDictionary() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {

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
            throw new RuntimeException("Файл словаря не найден: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла словаря: " + filePath, e);
        }
        for (int len = Config.MIN_WORD_LENGTH; len <= Config.MAX_WORD_LENGTH; len++) {
            if (!wordByLength.containsKey(len) || wordByLength.get(len).isEmpty()) {
                throw new IllegalStateException("Нет слов длины " + len + " в словаре");
            }
        }
        return wordByLength;
    }
}
