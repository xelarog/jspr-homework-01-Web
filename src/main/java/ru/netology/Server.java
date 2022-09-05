package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server {
    private final int maxThreads = 64;
    private final int portServer;

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();

    private final List<String> validPaths = List.of(
            "/index.html",
            "/spring.svg",
            "/spring.png",
            "/resources.html",
            "/styles.css",
            "/app.js",
            "/links.html",
            "/forms.html",
            "/classic.html",
            "/events.html",
            "/events.js");

    public Server(int port) {
        portServer = port;
        validPaths.addAll(validPaths);
    }

    public void startServer() {

        ExecutorService threadpool = Executors.newFixedThreadPool(maxThreads);
        try (final var serverSocket = new ServerSocket(portServer);) {
            while (true) {
                final var socket = serverSocket.accept();
                threadpool.execute(() -> process(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(Socket socket) {
        while (true) {
            try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 final var out = new BufferedOutputStream(socket.getOutputStream())) {

                final var requestLine = in.readLine();
                if (requestLine == null) break;
                final var parts = requestLine.split(" ");

                if (parts.length != 3) {
                    // just close socket
                    break;
                }

                Request request = new Request(parts[0], parts[1], parts[2]);
                final var path = parts[1];
                if (!validPaths.contains(path)) {
                    out.write((
                            "HTTP/1.1 404 Not Found\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.flush();
                    break;
                }

                final var filePath = Path.of(".", "public", path);
                final var mimeType = Files.probeContentType(filePath);

                // special case for classic
                if (path.equals("/classic.html")) {
                    final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + content.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.write(content);
                    out.flush();
                    break;
                }

                final var length = Files.size(filePath);
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, out);
                out.flush();
                break;

            } catch (IOException e) {
            }

            public void addHandler (String method, String path, Handler handler){

                ConcurrentHashMap<String, Handler> handlersValue = handlers.get(method);
                handlersValue.putIfAbsent(path, handler);
                handlers.putIfAbsent(method, handlersValue);

            }


        }

