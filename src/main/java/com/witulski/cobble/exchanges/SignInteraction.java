package com.witulski.cobble.exchanges;

import com.witulski.cobble.Cobble;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.witulski.cobble.util.IsSign.isSign;

public class SignInteraction implements Listener {
    private final Cobble plugin;

    public SignInteraction(Cobble plugin){this.plugin = plugin;}

    @EventHandler
    public void onSignClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(plugin.isPlayerLogged(player)){
            //Check Right Click
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Block clickedBlock = event.getClickedBlock();
                if(isSign(clickedBlock.getType())){
                    Location loc = clickedBlock.getLocation();
                    if(plugin.containsSignLoc(loc)){
                       player.sendMessage("Exchange Sign Right Clicked Sign At, World: " + loc.getWorld() + " X: "+loc.getBlockX()+" Y: "+loc.getBlockY()+" Z: "+loc.getBlockZ());
                    }
                }
            }

            //Check Left Click
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Block clickedBlock = event.getClickedBlock();
                if(isSign(clickedBlock.getType())){
                    Location loc = clickedBlock.getLocation();
                    if(plugin.containsSignLoc(loc)){
                        player.sendMessage("Exchange Sign Left Clicked Sign At, World: " + loc.getWorld() + " X: "+loc.getBlockX()+" Y: "+loc.getBlockY()+" Z: "+loc.getBlockZ());
                    }
                }
            }
        }
    }
}

