package com.example.literr.classes.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpClientExample {
    public String client(String endereço) throws IOException, InterruptedException;



}

