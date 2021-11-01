package com.witulski.cobble;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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


public class CheckAwaitingValidation implements Listener {

    private final Cobble plugin;

    public CheckAwaitingValidation (Cobble plugin) {
        this.plugin = plugin;
    }

    public static void checkValidation(final Callback callback, Cobble plugin, String loginToken, PlayerJoinEvent event){
        Player player = event.getPlayer();
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/toValidate/";
                Map req = new HashMap();
                req.put("mcusername",  player.getName());
                String jsonReq = JSONValue.toJSONString(req);

                try{
                    String response = doPost(url, jsonReq, loginToken);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;

                    logger.log(Level.WARNING,"-----Checking Validation-----");
                    callback.call(plugin, jsonRes.get("message").toString());

                }catch (Exception e){
                    logger.log(Level.WARNING, "Could not verify user.");
                    logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
                    player.sendMessage("Could not verify, please contact server admin and report this issue.");
                }

            }
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.getLogger().log(Level.WARNING, "Player Joined!");
        checkValidation(
                new Callback() {
                    @Override
                    public void call(Cobble plugin, String string) {
                        if(string.equals("awaiting")){
                            event.getPlayer().sendMessage("Your account is awaiting validation, if you DID NOT make a new account and link your mc to it, then type /declineValidation.");
                            event.getPlayer().sendMessage("If this is you, type /validate to finalize your new account.");
                        }
                    }
                }
                , plugin, plugin.getLoginToken(), event);
    }
}
