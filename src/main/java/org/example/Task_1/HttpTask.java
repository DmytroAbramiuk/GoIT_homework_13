package org.example.Task_1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpTask {
    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";

    public static String convertJsonToString(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private static String getResponceBody(HttpResponse httpResponse) throws IOException {
        return "Status code: "
                + httpResponse.getStatusLine().getStatusCode()
                + "\nResponce body: "
                + EntityUtils.toString(httpResponse.getEntity());
    }

    public static String sendGet() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL);

        return getResponceBody(httpClient.execute(httpGet));
    }

    public static String sendPost(String pathToAFile) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        String jsonHuman = convertJsonToString(pathToAFile);
        System.out.println(jsonHuman);
        System.out.println("==============================================================================");

        httpPost.setEntity(new StringEntity(jsonHuman));
        httpPost.setHeader("Content-Type", "application/json");

        return getResponceBody(httpClient.execute(httpPost));
    }

    public static String sendPut(String pathToAFile) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        String jsonHuman = convertJsonToString(pathToAFile);
        System.out.println(jsonHuman);
        System.out.println("==============================================================================");

        JsonElement jsonElement = JsonParser.parseString(jsonHuman);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        HttpPut httpPut = new HttpPut(API_URL + "/" + jsonObject.get("id").getAsString());

        httpPut.setEntity(new StringEntity(jsonHuman));

        httpPut.setHeader("Content-Type", "application/json");

        return getResponceBody(httpClient.execute(httpPut));
    }

    public static String sendDelete(String pathToAFile) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();

        String jsonHuman = convertJsonToString(pathToAFile);
        System.out.println(jsonHuman);
        System.out.println("==============================================================================");

        JsonElement jsonElement = JsonParser.parseString(jsonHuman);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        HttpDelete httpDelete = new HttpDelete(API_URL + "/" + jsonObject.get("id").getAsString());

        return getResponceBody(httpClient.execute(httpDelete));
    }

    public static String sendGetByIndex(String id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL + "/" + id);

        return getResponceBody(httpClient.execute(httpGet));
    }

    public static String sendGetByUsername(String username) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL + "?username=" + username);

        return getResponceBody(httpClient.execute(httpGet));
    }
}
