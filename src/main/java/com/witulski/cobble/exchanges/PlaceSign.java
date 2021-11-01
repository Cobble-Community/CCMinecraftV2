package com.witulski.cobble.exchanges;

import com.witulski.cobble.Callback;
import com.witulski.cobble.Cobble;
import com.witulski.cobble.ExSign;
import com.witulski.cobble.Exchange;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.witulski.cobble.util.DoPost.doPost;
import static com.witulski.cobble.util.GetTargetBlock.getTargetBlock;

public class PlaceSign implements Listener{

    private final Cobble plugin;

    public PlaceSign (Cobble plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        int fee = 0;
        Player player = event.getPlayer();
        if(Objects.equals(event.getLine(0), "[EXCHANGE]")){
            if(plugin.isPlayerLogged(player)){

                String exchange = Objects.requireNonNull(event.getLine(1));
                String resource = Objects.requireNonNull(event.getLine(2)).toLowerCase();
                String strFee = Objects.requireNonNull(event.getLine(3)).toLowerCase();

                Block playerTargetBlock = getTargetBlock(player, 5);
                Location signLoc = playerTargetBlock.getLocation();


                try {
                    fee = Integer.parseInt(strFee);
                }
                catch (Exception e){
                    player.sendMessage("Fee must be a number");
                    return;
                }
                // Call backend to see how many signs are available
                int finalFee = fee;
                placeSign(new Callback() {
                    @Override
                    public void call(Cobble plugin, String string) {
                        plugin.getLogger().log(Level.WARNING, string);
                        if(string.equals("success")){
                            Exchange exchangeObj = plugin.getExchangeFromName(exchange);
                            ExSign newSign = new ExSign(signLoc, resource, finalFee, player.getName(), exchangeObj);
                            exchangeObj.setSign(newSign);
                            player.sendMessage("Successfully Created Exchange Sign");
                        }else {
                            player.sendMessage("Unable to create Exchange");
                        }
                    }
                }, this.plugin, plugin.getLoginToken(), player, exchange, resource, fee, signLoc);


            }
            else{
                player.sendMessage("You must be logged in to make an exchange sign!");
            }

        }
    }

    public void placeSign(final Callback callback, Cobble plugin, String loginToken, Player player, String exchange, String resource, int fee, Location signLoc){
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/placeSign/";
                Map req = new HashMap();
                req.put("resource", resource);
                req.put("exchangeName", exchange);
                req.put("locationWorld", Objects.requireNonNull(signLoc.getWorld()).getName());
                req.put("locationX", signLoc.getBlockX());
                req.put("locationY", signLoc.getBlockY());
                req.put("locationZ", signLoc.getBlockZ());
                req.put("mcusername",  player.getName());
                String jsonReq = JSONValue.toJSONString(req);

                try{
                    String response = doPost(url, jsonReq, loginToken);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;

                    callback.call(plugin, jsonRes.get("message").toString());

                }catch (Exception e){
                    logger.log(Level.WARNING, "Could not place sign.");
                    logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
                    player.sendMessage("An error occurred, please contact server admin and report this issue.");
                }

            }
        }.runTaskAsynchronously(plugin);
    }

}
