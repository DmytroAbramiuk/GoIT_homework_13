package org.example.Task_1;


import java.io.*;

public class HttpTaskTest {

    public static void main(String[] args) throws IOException {
        System.out.println("=====================================================================");
        System.out.println("===============================POST==================================");
        System.out.println("=====================================================================");

        System.out.println("send POST = " + HttpTask.sendPost("src/post.json"));

        System.out.println("=====================================================================");
        System.out.println("================================PUT==================================");
        System.out.println("=====================================================================");

        System.out.println("send PUT = " + HttpTask.sendPut("src/put.json"));

        System.out.println("=====================================================================");
        System.out.println("===============================DELETE================================");
        System.out.println("=====================================================================");

        System.out.println("send DELETE = " + HttpTask.sendDelete("src/put.json"));

        System.out.println("=====================================================================");
        System.out.println("================================GET==================================");
        System.out.println("=====================================================================");

        System.out.println("send GET = " + HttpTask.sendGet());

        System.out.println("=====================================================================");
        System.out.println("=============================GET BY INDEX============================");
        System.out.println("=====================================================================");

        System.out.println("send GET (By Index) = " + HttpTask.sendGetByIndex("9"));

        System.out.println("=====================================================================");
        System.out.println("==========================GET BY USERNAME============================");
        System.out.println("=====================================================================");

        System.out.println("send GET (By Username) = " + HttpTask.sendGetByUsername("Delphine"));
    }
}