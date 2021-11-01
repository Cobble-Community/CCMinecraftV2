package com.witulski.cobble;

import com.witulski.cobble.claims.Claims;
import com.witulski.cobble.commands.validation.Validate;
import com.witulski.cobble.commands.validation.DoNotValidate;

import com.witulski.cobble.exchanges.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public final class Cobble extends JavaPlugin {

    private String URL_Base = "http://192.168.1.219:8000/";

    private String loginToken = "";

    private ArrayList<Player> loggedPlayers = new ArrayList<Player>();

    private ArrayList<Exchange> registeredExchanges = new ArrayList<Exchange>();

    public String getURLBase(){return URL_Base;}

    public ExSign removeSign(Location signLoc){
        for(int i=0; i<registeredExchanges.size(); i++){
            Exchange ex = registeredExchanges.get(i);
            if (ex.getCoalSign() != null && ex.getCoalSign().getLocation().equals(signLoc)){
                ExSign sign = ex.getCoalSign();
                ex.setCoalSign(null);
                return sign;
            }
            else if (ex.getIronSign() != null && ex.getIronSign().getLocation().equals(signLoc)){
                ExSign sign = ex.getIronSign();
                ex.setIronSign(null);
                return sign;
            }
            else if (ex.getGoldSign() != null && ex.getGoldSign().getLocation().equals(signLoc)){
                ExSign sign = ex.getGoldSign();
                ex.setGoldSign(null);
                return sign;
            }
            else if (ex.getDiamondSign() != null && ex.getDiamondSign().getLocation().equals(signLoc)){
                ExSign sign = ex.getDiamondSign();
                ex.setDiamondSign(null);
                return sign;
            }
        }
        return null;
    }

    public boolean containsSignLoc(Location signLoc) {
        for(int i=0; i<registeredExchanges.size(); i++){
            Exchange ex = registeredExchanges.get(i);
            if (ex.getCoalSign() != null && ex.getCoalSign().getLocation().equals(signLoc)){
                return true;
            }
            else if (ex.getIronSign() != null && ex.getIronSign().getLocation().equals(signLoc)){
                return true;
            }
            else if (ex.getGoldSign() != null && ex.getGoldSign().getLocation().equals(signLoc)){
                return true;
            }
            else if (ex.getDiamondSign() != null && ex.getDiamondSign().getLocation().equals(signLoc)){
                return true;
            }
        }
        return false;
    }

    public ExSign getSignFromLoc(Location signLoc) {
        for (int i=0; i<registeredExchanges.size(); i++){
            Exchange ex = registeredExchanges.get(i);
            if (ex.getCoalSign() != null && ex.getCoalSign().getLocation().equals(signLoc)){
                return ex.getCoalSign();
            }
            else if (ex.getIronSign() != null && ex.getIronSign().getLocation().equals(signLoc)){
                return ex.getIronSign();
            }
            else if (ex.getGoldSign() != null && ex.getGoldSign().getLocation().equals(signLoc)){
                return ex.getGoldSign();
            }
            else if (ex.getDiamondSign() != null && ex.getDiamondSign().getLocation().equals(signLoc)){
                return ex.getDiamondSign();
            }
        }
        return null;
    }

    public void addRegisteredExchanges(Exchange exchange){
        registeredExchanges.add(exchange);
    }

    public ArrayList<Exchange> getRegisteredExchanges(){return registeredExchanges;}

    public Exchange getExchangeFromName(String exchangeName) {
        for (int i = 0; i < registeredExchanges.size(); i++) {
            if (registeredExchanges.get(i).getName().equals(exchangeName)) {
                return registeredExchanges.get(i);
            }
        }
        return null;
    }

    public void logReggisteredExchanges(){
        registeredExchanges.forEach((e) -> this.getLogger().log(Level.WARNING, e.toString()));
    }

    public String getLoginToken(){
        return loginToken;
    }

    public void addLoggedPlayers(Player player){
        if(!loggedPlayers.contains(player))
            loggedPlayers.add(player);
    }

    public void removeLoggedPlayers(Player player){
        if(loggedPlayers.contains(player))
            loggedPlayers.remove(player);
    }

    public Boolean isPlayerLogged(Player player){
        return loggedPlayers.contains(player);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        //this.getCommand("validate").setExecutor(new Validate());
        this.getLogger().log(Level.WARNING, "---HELLO FROM COBBLE---");

        Bukkit.getPluginManager().registerEvents(new CheckAwaitingValidation(this), this);
        Bukkit.getPluginManager().registerEvents(new Claims(this), this);
        Bukkit.getPluginManager().registerEvents(new LoginPlayer(this), this);
        Bukkit.getPluginManager().registerEvents(new PlaceSign(this), this);
        Bukkit.getPluginManager().registerEvents(new SignInteraction(this), this);
        Bukkit.getPluginManager().registerEvents(new DestroySign(this), this);

        this.getCommand("login").setExecutor(new LoginPlayer(this));
        this.getCommand("validate").setExecutor(new Validate(this));
        this.getCommand("declineValidation").setExecutor(new DoNotValidate(this));
        this.getCommand("checkLogin").setExecutor(new CheckLogin(this));
        this.getCommand("registerExchange").setExecutor(new RegisterExchange(this));
        this.getCommand("listExchanges").setExecutor(new ListExchanges(this));
        this.getCommand("resetBlocks").setExecutor(new Claims(this));

        /*
        ANYTHING REQUIRING THE LOGIN TOKEN MUST BE CALLED IN HERE
         */
        ServerLogin.serverLogin(new Callback() {
            @Override
            public void call(Cobble plugin, String string) {
                loginToken = string;
                plugin.getLogger().log(Level.WARNING, loginToken);

                //AFTER LOGIN TOKEN RECIEVED!

                GetExchanges.getExchanges(plugin, loginToken, registeredExchanges);


            }
        }, this);

        this.getLogger().log(Level.WARNING, "---Getting Exchanges---");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
