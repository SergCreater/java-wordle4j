package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.Charset;

public class PrintWriter extends PrintStream {
    public PrintWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintWriter(File file, Charset charset) throws IOException {
        super(file, charset);
    }

    public PrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    public PrintWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public PrintWriter(String fileName, Charset charset) throws IOException {
        super(fileName, charset);
    }

    public PrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public PrintWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public PrintWriter(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
    }

    public PrintWriter(OutputStream out, boolean autoFlush, Charset charset) {
        super(out, autoFlush, charset);
    }

    public PrintWriter(OutputStream out) {
        super(out);
    }
}
