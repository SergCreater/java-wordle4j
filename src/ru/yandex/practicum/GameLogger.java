package ru.yandex.practicum;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GameLogger {

    private PrintWriter writer;

    public GameLogger(String fileName) throws IOException {
        this.writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
    }

    public void info(String messege) {
        writer.println("[Info] " + messege);
        writer.flush();
    }

    public void error(String messege) {
        writer.println("[Error] " + messege);
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
