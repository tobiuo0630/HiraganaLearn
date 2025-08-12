package mee.example.hiragana_learn;

import com.google.gson.JsonObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;

public class VillagerTalkEventListener implements Listener {
    private final Plugin plugin;
    private final ArrayList<JsonObject> mission;

    public VillagerTalkEventListener(JavaPlugin plugin, ArrayList<JsonObject> mission){
        this.plugin = plugin;
        this.mission = mission;
    }

    @EventHandler
    private void VillagerTalkEvent(PlayerInteractEvent event){

    }

}
