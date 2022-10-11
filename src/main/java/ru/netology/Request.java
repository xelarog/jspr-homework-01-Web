package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;


public class Request {

    private final String method;
    private final String path;
    private final List<String> headers;
    private final String body;

    public Request(String method, String path, List<String> headers, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public List<NameValuePair> getQueryParams() throws URISyntaxException {
        return URLEncodedUtils.parse(new URI(path), StandardCharsets.UTF_8);
     }

    public String getQueryParam(String name) throws URISyntaxException {
        Optional<NameValuePair> list =  URLEncodedUtils.parse(new URI(path), StandardCharsets.UTF_8).stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
        return list.map(NameValuePair::getValue).orElse(null);
    }

    @Override
    public String toString() {
        return "Метод запроса: " + method + "\n" +
                "Путь: " + path + "\n" +
                "Заголовки: " + headers.toString() + "\n" +
                "Тело запроса: " + body;
    }
}
