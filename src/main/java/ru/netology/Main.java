package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Main {
    private static final int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server();

        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream out) throws IOException {

                out.write((
                        "HTTP/1.1 201 Created\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            }
        });

        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream out) throws IOException {
                out.write((
                        "HTTP/1.1 202 Accepted\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
            }

        });

        server.startServer(PORT);
    }
}


