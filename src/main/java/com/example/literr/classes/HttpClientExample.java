package com.example.literr.classes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientExample {


    private static final String URL = "https://jsonplaceholder.typicode.com/posts/1";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static void main(String[] args) {
        HttpClientExample example = new HttpClientExample();
        String responseBody = example.jsonMethod();
        if (responseBody != null) {
            System.out.println("Conteúdo da resposta recebida:\n" + responseBody);
        } else {
            System.out.println("Falha ao obter a resposta.");
        }
    }

    public String jsonMethod() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        try {

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());


            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Corpo da Resposta (JSON): " + responseBody);

            return responseBody;

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
            return null;
        }
    }
}

