package com.witulski.cobble.util;

import com.witulski.cobble.Exchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DoGet {
    public static String doGet(String strUrl, String authToken) throws Exception{
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        if(authToken != null){
            con.setRequestProperty("Authorization", "Token "+authToken);
        }
        con.setDoOutput(true);

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
        String url = "http://127.0.0.1:8000/api/router/exchanges/";
        String loginToken = "8173a6df5ee81a9f56138a405111c4f39b52c135";
        ArrayList<Exchange> exchanges = new ArrayList<Exchange>();
        try {
            String response = doGet(url, loginToken);
            Object res = JSONValue.parse(response);
            JSONArray jsonArr = (JSONArray) res;
            for(int i=0; i<jsonArr.size(); i++){
                System.out.println(jsonArr.get(i).toString());
                JSONObject jsonOb = (JSONObject) jsonArr.get(i);

                String name = (String) jsonOb.get("name");
                String registree = (String) jsonOb.get("registeree");
                long exFee = (long) jsonOb.get("exFee");

                Exchange exchange = new Exchange(registree, name, exFee);
                exchanges.add(exchange);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        exchanges.forEach((ex) -> System.out.println(ex.toString()));

    }
}
