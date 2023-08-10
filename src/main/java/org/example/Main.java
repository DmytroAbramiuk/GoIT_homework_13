package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";

    public static String convertJsonToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/task.json")));
    }

    private static String getResponceBody(HttpResponse httpResponse) throws IOException {
        return "Status code: "
                + httpResponse.getStatusLine().getStatusCode()
                + "\nResponce body: "
                + EntityUtils.toString(httpResponse.getEntity());
    }

    public static void sendGet() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL);

        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();

        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("Status Code: " + statusCode + "\nResponce: " + responseBody);
    }

    private static HttpResponse sendPost(String pathToAFile) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        String jsonHuman = convertJsonToString(pathToAFile);
        System.out.println(jsonHuman);
        System.out.println("==============================================================================");

        httpPost.setEntity(new StringEntity(jsonHuman));
        httpPost.setHeader("Content-Type", "application/json");

        return httpClient.execute(httpPost);
    }

    public static HttpResponse sendPut(String pathToAFile) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(API_URL);

        String jsonHuman = convertJsonToString(pathToAFile);
        System.out.println(jsonHuman);
        System.out.println("==============================================================================");

        httpPut.setEntity(new StringEntity(jsonHuman));

        httpPut.setHeader("Content-Type", "application/json");

        return httpClient.execute(httpPut);
    }

    public static void main(String[] args) throws IOException {
        HttpResponse sendResponse = sendPost("src/task.json");
        System.out.println("getResponceBody(response) = " + getResponceBody(sendResponse));

        System.out.println("=====================================================================");
        System.out.println("=====================================================================");
        System.out.println("=====================================================================");

        HttpResponse putResponce = sendPut("src/task.json");
        System.out.println("getResponceBody(response) = " + getResponceBody(putResponce));
    }
}