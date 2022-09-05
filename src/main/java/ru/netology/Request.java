package ru.netology;

public class Request {

    private String method;
    private String headers;
    private String body;

    public Request(String method, String headers, String body) {
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }
}
