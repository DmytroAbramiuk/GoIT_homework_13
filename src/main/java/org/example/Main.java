package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.human.Human;
import org.jsoup.Jsoup;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String apiUrl = "https://jsonplaceholder.typicode.com/users";

    public static void getContent(){
        try {
            String json = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();

            Type type = TypeToken.getParameterized(List.class, Human.class)
                    .getType();

            List<Human> humanList = new Gson().fromJson(json, type);

            System.out.println(humanList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendPOST() throws IOException {
        File humanInfo = new File("src/task.json");

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(humanInfo.toPath()));

        //read json file with a new human
        Scanner sc = new Scanner(humanInfo);
        String human = "";
        while(sc.hasNext())
            human += sc.nextLine();

        Gson json = new Gson();
        System.out.println("json = " + json.toJson(human));

        sc.close();
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("POST response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

    public static void main(String[] args) throws IOException {
        sendPOST();
    }
}