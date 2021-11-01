package com.witulski.cobble;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.witulski.cobble.util.DoPost.doPost;

public class ServerLogin {

    public static void serverLogin(final Callback callback, Cobble plugin){
        new BukkitRunnable(){

            @Override
            public void run() {
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/api-token-auth/";
                Map req = new HashMap();
                req.put("username", "backendLogin");
                req.put("password", "$$5TiWnAvE");
                String jsonReq = JSONValue.toJSONString(req);

                try{
                    String response = doPost(url, jsonReq, null);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;
                    logger.log(Level.WARNING, "-----Recieved Token-----");
                    callback.call(plugin, jsonRes.get("token").toString());
                    //loginToken.accept(jsonRes.get("token").toString());
                }catch (Exception e){
                    logger.log(Level.WARNING, "-----THERE ARE ERRORS WITH GETTING TOKEN-----");
                    logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}
