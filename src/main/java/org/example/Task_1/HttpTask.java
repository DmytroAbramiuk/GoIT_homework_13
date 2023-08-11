package org.example.Task_1;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.human.Human;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

    //=======================================TASK_1================================================================

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

    public static String sendGetByIndex(int id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL + "/" + id);

        return getResponceBody(httpClient.execute(httpGet));
    }

    public static String sendGetByUsername(String username) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL + "?username=" + username);

        return getResponceBody(httpClient.execute(httpGet));
    }

    //=======================================TASK_2================================================================
    private static void toJson(int id, int postId, HttpResponse comments) throws IOException {
        String pathOfNewFile = "src/user-" + id + "-post-" + postId + "-comments.json";

        new File(pathOfNewFile).createNewFile();
        File toFile = new File(pathOfNewFile);

        try (FileWriter fw = new FileWriter(toFile)) {
            fw.write(EntityUtils.toString(comments.getEntity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getLastPostIndex(HttpResponse response) throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        Gson gson = new Gson();

        JsonArray postsArray = gson.fromJson(json, JsonArray.class);

        try {
            JsonObject lastPost = postsArray.get(postsArray.size() - 1).getAsJsonObject();
            return lastPost.get("id").getAsInt();
        } catch (RuntimeException e){
            return -1;
        }
    }

    public static void sendGetLastPostComments(int id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGetPosts = new HttpGet(API_URL + "/" + id + "/posts");

        HttpResponse postsOfUserResponse = httpClient.execute(httpGetPosts);
        int lastPostId = getLastPostIndex(postsOfUserResponse);

        if(lastPostId==-1)
            throw new RuntimeException("There isn't user with current id");

        HttpGet httpGetComments = new HttpGet(API_URL.replace("users", "posts/" + lastPostId + "/comments"));

        HttpResponse commentsResponse = httpClient.execute(httpGetComments);
        toJson(id, lastPostId, commentsResponse);
    }

    //=======================================TASK_3================================================================

    private static String parseTodos(HttpResponse todosOfUserResponse) throws IOException {
        String json = EntityUtils.toString(todosOfUserResponse.getEntity());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray todosArray = gson.fromJson(json, JsonArray.class);
        JsonArray openedTodosArray = new JsonArray();

        for(JsonElement obj : todosArray){
            if(!obj.getAsJsonObject().get("completed").getAsBoolean())
                openedTodosArray.add(obj);
        }

        return gson.toJson(openedTodosArray);
    }

    public static String sendGetAllOpenedTodos(int id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGetAllTodos = new HttpGet(API_URL + "/" + id + "/todos");

        HttpResponse todosOfUserResponse = httpClient.execute(httpGetAllTodos);

        return parseTodos(todosOfUserResponse);
    }
}
