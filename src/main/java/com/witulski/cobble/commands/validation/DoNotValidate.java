package com.witulski.cobble.commands.validation;

import com.witulski.cobble.Callback;
import com.witulski.cobble.Cobble;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

public class DoNotValidate implements CommandExecutor {

    private final Cobble plugin;

    public DoNotValidate(Cobble plugin){this.plugin=plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            doNotValidate(new Callback() {
                @Override
                public void call(Cobble plugin, String string) {
                    if(string.equals("success")){
                        player.sendMessage("Successfully removed mc username from associated account");
                    }else {
                        player.sendMessage("An error occurred");
                        player.sendMessage("Either your name is not associated with an account, or your account is already validated");
                    }
                }
            }, this.plugin, plugin.getLoginToken(), player);
        }
        return true;
    }

    public void doNotValidate(final Callback callback, Cobble plugin, String loginToken, Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/doNotValidate/";
                Map req = new HashMap();
                req.put("mcusername",  player.getName());
                String jsonReq = JSONValue.toJSONString(req);

                try{
                    String response = doPost(url, jsonReq, loginToken);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;

                    callback.call(plugin, jsonRes.get("message").toString());

                }catch (Exception e){
                    logger.log(Level.WARNING, "Could not remove MC Username");
                    logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
                    player.sendMessage("An error occurred, please contact server admin and report this issue.");
                }

            }
        }.runTaskAsynchronously(plugin);
    }
}
