package ru.yandex.practicum.exceptions;

public class DictionaryLoadException extends SystemException{
    public DictionaryLoadException(String messege, Throwable trow){
        super(messege, trow);
    }
}
