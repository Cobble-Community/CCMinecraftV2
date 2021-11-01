package com.witulski.cobble;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
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

public class LoginPlayer implements CommandExecutor, Listener {
    private final Cobble plugin;

    public LoginPlayer(Cobble plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String secret = args[0];
        if(sender instanceof Player){
            Player player = (Player) sender;
            login(new Callback() {
                @Override
                public void call(Cobble plugin, String string) {
                    if(string.equals("correct")){
                        ((Cobble) plugin).addLoggedPlayers(player);
                        player.sendMessage("Successfully Logged In");
                    }else {
                        player.sendMessage("Unable to login");
                        player.sendMessage("Incorrect secret");
                    }
                }
            }, this.plugin, plugin.getLoginToken(), secret, player);
        }
        return true;
    }

    public void login(final Callback callback, Cobble plugin, String loginToken, String secret, Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/checkSecret/";
                Map req = new HashMap();
                req.put("mcsecret",  secret);
                req.put("mcusername", player.getName());
                String jsonReq = JSONValue.toJSONString(req);
                try{
                    String response = doPost(url, jsonReq, loginToken);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;

                    callback.call(plugin, jsonRes.get("message").toString());

                }catch (Exception e){
                    logger.log(Level.WARNING, "Could not login user.");
                    logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
                    player.sendMessage("Could not verify, please contact server admin and report this issue.");
                }

            }
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        plugin.removeLoggedPlayers(player);
        plugin.getLogger().log(Level.WARNING, "Player Disconnected");
    }
}
