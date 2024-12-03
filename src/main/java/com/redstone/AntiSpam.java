package com.redstone;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

public class AntiSpam extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("MyFirstPlugin aktif!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MyFirstPlugin dinonaktifkan!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(TextFormat.GREEN + "Selamat datang di server!");
    }
}