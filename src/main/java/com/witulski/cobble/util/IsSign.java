package com.witulski.cobble.util;

import org.bukkit.Material;

public class IsSign {
    public static boolean isSign(Material mat){
        return mat.equals(Material.OAK_SIGN) || mat.equals(Material.SPRUCE_SIGN) || mat.equals(Material.ACACIA_SIGN) || mat.equals(Material.BIRCH_SIGN) || mat.equals(Material.CRIMSON_SIGN) || mat.equals(Material.DARK_OAK_SIGN) || mat.equals(Material.JUNGLE_SIGN) || mat.equals(Material.WARPED_SIGN) ||
                mat.equals(Material.OAK_WALL_SIGN) || mat.equals(Material.SPRUCE_WALL_SIGN) || mat.equals(Material.ACACIA_WALL_SIGN) || mat.equals(Material.BIRCH_WALL_SIGN) || mat.equals(Material.CRIMSON_WALL_SIGN) || mat.equals(Material.DARK_OAK_WALL_SIGN) || mat.equals(Material.JUNGLE_WALL_SIGN) || mat.equals(Material.WARPED_WALL_SIGN);
    }
}
