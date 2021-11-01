package com.witulski.cobble;

import org.bukkit.plugin.java.JavaPlugin;

public interface Callback {
    void call(Cobble plugin, String string);
}
