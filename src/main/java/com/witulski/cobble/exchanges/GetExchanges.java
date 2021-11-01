package com.witulski.cobble.exchanges;

import com.witulski.cobble.Cobble;
import com.witulski.cobble.ExSign;
import com.witulski.cobble.Exchange;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import static com.witulski.cobble.util.DoGet.doGet;

public class GetExchanges {
    public static void getExchanges(Cobble plugin, String loginToken, ArrayList<Exchange> exchanges){
        String url = plugin.getURLBase() + "api/router/exchanges/";
        int countExs = 0;
        try {
            String response = doGet(url, loginToken);
            Object res = JSONValue.parse(response);
            JSONArray jsonArr = (JSONArray) res;
            for(int i=0; i<jsonArr.size(); i++){
                //System.out.println(jsonArr.get(i).toString());
                JSONObject jsonOb = (JSONObject) jsonArr.get(i);

                String name = (String) jsonOb.get("name");
                String registree = (String) jsonOb.get("registeree");
                long exFee = (long) jsonOb.get("exFee");

                Exchange exchange = new Exchange(registree, name, exFee);

                String coalSignOwner = (String) jsonOb.get("coalSignOwner");
                long coalSignFee = (long) jsonOb.get("coalSignFee");
                boolean coalSignSet = (boolean) jsonOb.get("coalSignSet");
                String coalSignLocationWorld = (String) jsonOb.get("coalSignLocationWorld");
                long coalSignX = (long) jsonOb.get("coalSignLocationX");
                long coalSignY = (long) jsonOb.get("coalSignLocationY");
                long coalSignZ = (long) jsonOb.get("coalSignLocationZ");
                if(coalSignSet){
                    Location coalLoc = new Location(plugin.getServer().getWorld(coalSignLocationWorld), coalSignX, coalSignY, coalSignZ);
                    ExSign coalSign = new ExSign(coalLoc, "coal", (int) coalSignFee, coalSignOwner, exchange);
                    exchange.setCoalSign(coalSign);
                }

                String ironSignOwner = (String) jsonOb.get("ironSignOwner");
                long ironSignFee = (long) jsonOb.get("ironSignFee");
                boolean ironSignSet = (boolean) jsonOb.get("ironSignSet");
                String ironSignLocationWorld = (String) jsonOb.get("ironSignLocationWorld");
                long ironSignX = (long) jsonOb.get("ironSignLocationX");
                long ironSignY = (long) jsonOb.get("ironSignLocationY");
                long ironSignZ = (long) jsonOb.get("ironSignLocationZ");
                if(ironSignSet){
                    Location ironLoc = new Location(plugin.getServer().getWorld(ironSignLocationWorld), ironSignX, ironSignY, ironSignZ);
                    ExSign ironSign = new ExSign(ironLoc, "iron", (int) ironSignFee, ironSignOwner, exchange);
                    exchange.setIronSign(ironSign);
                }

                String goldSignOwner = (String) jsonOb.get("goldSignOwner");
                long goldSignFee = (long) jsonOb.get("goldSignFee");
                boolean goldSignSet = (boolean) jsonOb.get("goldSignSet");
                String goldSignLocationWorld = (String) jsonOb.get("goldSignLocationWorld");
                long goldSignX = (long) jsonOb.get("goldSignLocationX");
                long goldSignY = (long) jsonOb.get("goldSignLocationY");
                long goldSignZ = (long) jsonOb.get("goldSignLocationZ");
                if(goldSignSet){
                    Location goldLoc = new Location(plugin.getServer().getWorld(goldSignLocationWorld), goldSignX, goldSignY, goldSignZ);
                    ExSign goldSign = new ExSign(goldLoc, "gold", (int) goldSignFee, goldSignOwner, exchange);
                    exchange.setIronSign(goldSign);
                }

                String diamondSignOwner = (String) jsonOb.get("diamondSignOwner");
                long diamondSignFee = (long) jsonOb.get("diamondSignFee");
                boolean diamondSignSet = (boolean) jsonOb.get("diamondSignSet");
                String diamondSignLocationWorld = (String) jsonOb.get("diamondSignLocationWorld");
                long diamondSignX = (long) jsonOb.get("diamondSignLocationX");
                long diamondSignY = (long) jsonOb.get("diamondSignLocationY");
                long diamondSignZ = (long) jsonOb.get("diamondSignLocationZ");
                if(diamondSignSet){
                    Location diamondLoc = new Location(plugin.getServer().getWorld(diamondSignLocationWorld), diamondSignX, diamondSignY, diamondSignZ);
                    ExSign diamondSign = new ExSign(diamondLoc, "diamond", (int) diamondSignFee, diamondSignOwner, exchange);
                    exchange.setIronSign(diamondSign);
                }

                exchanges.add(exchange);

                countExs += 1;

            }
            plugin.getLogger().log(Level.WARNING, "Added "+countExs+" Exchanges");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }

    }
}
