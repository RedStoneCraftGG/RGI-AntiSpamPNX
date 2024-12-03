package com.redstone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.PluginBase;

public class AntiSpam extends PluginBase implements Listener {
    private List<String> bannedWords;
    private List<String> specialWords;
    private final Map<String, Integer> spamCount = new HashMap<>();
    private final Map<String, Long> lastKillTime = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        bannedWords = getConfig().getStringList("banned-words");
        specialWords = getConfig().getStringList("special-words");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AntiSpam plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiSpam plugin disabled!");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        for (String word : bannedWords) {
            if (message.toLowerCase().contains(word)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou're not allowed to send that!");
                return;
            }
        }

        // Praxx Filter

        String formattedMessage = message.replaceAll("@\\S+", "$#");
        boolean matches = specialWords.stream().anyMatch(formattedMessage::contains);

        if (matches) {
            if (lastKillTime.containsKey(player.getName())) {
                long killTime = lastKillTime.get(player.getName());
                long currentTime = System.currentTimeMillis();

                if (currentTime - killTime <= 2000) {
                    spamCount.put(player.getName(), spamCount.getOrDefault(player.getName(), 0) + 1);

                    if (spamCount.get(player.getName()) > 3) {
                        player.kick("You have been kicked for spamming after kills.", false);
                        spamCount.remove(player.getName());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getEntity() instanceof Player) {
            Player killer = (Player) event.getEntity().getLastDamageCause().getEntity();
            lastKillTime.put(killer.getName(), System.currentTimeMillis());
        }
    }
    
}