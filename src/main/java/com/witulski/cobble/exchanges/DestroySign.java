package com.witulski.cobble.exchanges;

import com.witulski.cobble.Callback;
import com.witulski.cobble.Cobble;
import com.witulski.cobble.ExSign;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.witulski.cobble.util.DoPost.doPost;
import static com.witulski.cobble.util.IsSign.isSign;

public class DestroySign implements Listener {
    private final Cobble plugin;

    public DestroySign(Cobble plugin){this.plugin = plugin;}

    @EventHandler
    public void onSignDestroyed(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(isSign(block.getType())){
            ExSign sign = plugin.removeSign(block.getLocation());
            if(sign != null){
                destroySign(new Callback() {
                    @Override
                    public void call(Cobble plugin, String string) {
                        plugin.getLogger().log(Level.WARNING, string);
                        if(string.equals("success")){

                            player.sendMessage("Successfully Destroyed Exchange Sign");
                        }else {
                            player.sendMessage("Got something other than Success");
                        }
                    }
                }, this.plugin, plugin.getLoginToken(), player, sign.getAssociatedExchange().getName(), sign.getResource());
            }
            else {
                player.sendMessage("Unable to destroy exchange sign");
            }
        }
    }

    public void destroySign(final Callback callback, Cobble plugin, String loginToken, Player player, String exchange, String resource){
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/removeSign/";
                Map req = new HashMap();
                req.put("resource", resource);
                req.put("exchangeName", exchange);
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
