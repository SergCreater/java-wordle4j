package ru.yandex.practicum.exceptions;

public class InvalidWordLengthException extends GameException {
    public InvalidWordLengthException(String messege) {
        super(messege);
    }
}
