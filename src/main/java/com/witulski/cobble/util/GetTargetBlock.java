package com.witulski.cobble.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class GetTargetBlock {
    public static Block getTargetBlock(Player player, int range){
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()){
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR){
                continue;
            }
            break;
        }
        return lastBlock;
    }
}
