package ru.netology;

public class Main {
    private static final int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server();
        server.startServer(PORT);

    }
}


