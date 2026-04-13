package ru.yandex.practicum.exceptions;

public class SystemException extends Exception {
    public SystemException(String messege) {
        super(messege);
    }

    public SystemException(String messege, Throwable trow) {
        super(messege, trow);
    }
}
