package ru.yandex.practicum.exceptions;

public class DictionaryNotFoundException extends SystemException {
    public DictionaryNotFoundException(String messege){
        super(messege);
    }
}
