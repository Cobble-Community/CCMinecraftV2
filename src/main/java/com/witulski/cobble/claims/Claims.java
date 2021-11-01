package com.witulski.cobble.claims;

import com.witulski.cobble.Cobble;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class Claims implements CommandExecutor, Listener {
    private final Cobble plugin;

    public Claims(Cobble plugin) {this.plugin = plugin;}

    List<Block> blocks = new ArrayList<Block>();

    @EventHandler
    public void OnInteract(PlayerInteractEvent event){
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(action.equals(Action.RIGHT_CLICK_BLOCK) && event.getHand() == EquipmentSlot.HAND && player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_AXE)){
            player.sendMessage("Right Clicked.");
            player.sendMessage(String.valueOf(block.getLocation()));
            blocks.add(block);
            //block.setType(Material.DIAMOND);        <------- THIS IS THE ERROR!!!
        }
    }

    private void resetBlock(){
        for(int i=0; i<blocks.size(); i++){
            Block block = blocks.get(i);
            block.setType(block.getType());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            resetBlock();
            player.sendMessage("Reset!");
        }
        return true;
    }
}
