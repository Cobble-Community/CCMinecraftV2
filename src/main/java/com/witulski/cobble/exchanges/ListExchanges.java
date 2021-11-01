package com.witulski.cobble.exchanges;

import com.witulski.cobble.Cobble;
import com.witulski.cobble.Exchange;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListExchanges implements CommandExecutor {
    private final Cobble plugin;

    public ListExchanges(Cobble plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<Exchange> exchanges = plugin.getRegisteredExchanges();
        exchanges.forEach((ex) -> sender.sendMessage(ex.toString()));
        int exCount = exchanges.size();
        sender.sendMessage(exCount + " exchanges registered");
        return true;
    }
}
