package com.example.literr.classes.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UsoAPI implements HttpClientExample {
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    @Override
    public String client(String titulo) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(ENDERECO+ titulo.replace(" ", "%20"))).build();
        HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }



}
