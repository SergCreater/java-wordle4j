package ru.yandex.practicum.exceptions;

public class WordNotFoundException extends GameException {
    public WordNotFoundException(String messege){
        super(messege);
    }
}
