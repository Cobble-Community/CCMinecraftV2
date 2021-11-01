package com.witulski.cobble.exchanges;

import com.witulski.cobble.Callback;
import com.witulski.cobble.Cobble;
import com.witulski.cobble.Exchange;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.witulski.cobble.util.DoPost.doPost;

public class RegisterExchange implements CommandExecutor {

    private final Cobble plugin;

    public RegisterExchange(Cobble plugin){this.plugin=plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        String name = args[0];
        String exFee = args[1];

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(this.plugin.isPlayerLogged(player)){
                Exchange exchange = new Exchange(player.getName(), name, Integer.parseInt(exFee));
                registerExchangeAPI(new Callback() {
                    @Override
                    public void call(Cobble plugin, String string) {
                        if(string.equals("success")){
                            sender.sendMessage("Successfully registered exchange");
                        }else{
                            sender.sendMessage("Failed to register exchange");
                        }
                    }
                }, plugin, plugin.getLoginToken(), exchange, player);

                this.plugin.addRegisteredExchanges(exchange);
                this.plugin.logReggisteredExchanges();
            }
            else{
                player.sendMessage("Please login");
                player.sendMessage("If you do not have an account, please visit our website.");
            }
        }
        return true;
    }

    public void registerExchangeAPI(final Callback callback, Cobble plugin, String loginToken, Exchange ex, Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                Logger logger = plugin.getLogger();
                String url = plugin.getURLBase() + "api/router/exchanges/";
                Map req = new HashMap();
                req.put("registeree",  ex.getRegisteree());
                req.put("name", ex.getName());
                req.put("exFee", ex.getExFee());

                String jsonReq = JSONValue.toJSONString(req);

                try{
                    String response = doPost(url, jsonReq, loginToken);
                    Object res = JSONValue.parse(response);
                    JSONObject jsonRes = (JSONObject) res;

                    callback.call(plugin, jsonRes.get("message").toString());

                }catch (Exception e){
                    logger.log(Level.WARNING, "Could not verify user.");
                    logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
                    player.sendMessage("Could not verify, please contact server admin and report this issue.");
                }

            }
        }.runTaskAsynchronously(plugin);
    }

}
