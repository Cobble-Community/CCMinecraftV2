package com.witulski.cobble.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class DoPost {
    public static String doPost(String strURL, String postData, String authToken) throws Exception{
        URL url = new URL(strURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        if(authToken != null){
            con.setRequestProperty("Authorization", "Token "+authToken);
        }
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()){
            byte[] input = postData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null){
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public static void main(String args[]){
        String url = "http://127.0.0.1:8000/api/api-token-auth/";
        String echoUrl = "http://127.0.0.1:8000/api/echo/";
        Map req = new HashMap();
        req.put("username", "green");
        req.put("password", "evanwit5");
        String jsonReq = JSONValue.toJSONString(req);

        //String request = "{\"username\": \"green\", \"password\": \"evanwit5\"}";
        try{
            String response = doPost(url, jsonReq, null);
            Object res = JSONValue.parse(response);
            JSONObject jsonRes = (JSONObject) res;
            System.out.println(jsonRes.toJSONString());
            System.out.println(jsonRes.get("token"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
