package mee.example.hiragana_learn;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VillagerTalkEventListener implements Listener {
    private final JavaPlugin plugin;
    private final ArrayList<JsonObject> mission;
    private final NamespacedKey explainedRuleKey;
    private final List<String> VillagerMessage;

    public VillagerTalkEventListener(JavaPlugin plugin, ArrayList<JsonObject> mission){
        this.plugin = plugin;
        this.mission = mission;
        this.explainedRuleKey = new NamespacedKey(plugin,"explained_rule");
        this.VillagerMessage = Arrays.asList(
                "§α[村人] §fこんにちは、わたしはむらたさん。",
                "§α[村人] §fこれから、きみにはたからさがしをしてもらうよ。",
                "§α[村人] §fおだいのあいてむをここまでもってきてね。",
                "§α[村人] §fじゅんびができたら、またはなしかけて"
        );
    }

    @EventHandler
    private void VillagerTalkEvent(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof Villager)){
            return;
        }

        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(explainedRuleKey, PersistentDataType.BYTE)){
            new DelayedMessageTask(plugin,player,VillagerMessage).runTaskTimer(plugin,0L,200L);
            data.set(explainedRuleKey,PersistentDataType.BYTE,(byte) 1);

        }else{

        }
    }

}
