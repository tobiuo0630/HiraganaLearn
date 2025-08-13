package mee.example.hiragana_learn;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VillagerTalkEventListener implements Listener {
    private final JavaPlugin plugin;
    private final List<JsonObject> mission;
    private final NamespacedKey explainedRuleKey;
    private final List<String> VillagerMessage;
    private int missionNumber;
    private JsonObject currentMission;

    private final ChestManager chestManager;

    public VillagerTalkEventListener(JavaPlugin plugin, List<JsonObject> mission){
        this.plugin = plugin;
        this.mission = mission;
        this.explainedRuleKey = new NamespacedKey(plugin,"explained_rule");
        this.missionNumber = 0;
        this.VillagerMessage = Arrays.asList(
                "§α[村人] §fこんにちは、わたしはむらたさん。",
                "§α[村人] §fこれから、きみにはたからさがしをしてもらうよ。",
                "§α[村人] §fおだいのあいてむをここまでもってきてね。",
                "§α[村人] §fじゅんびができたら、またはなしかけて"
        );

        World mainWorld = Bukkit.getWorlds().get(0);
        this.chestManager = new ChestManager(mainWorld);
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
            currentMission = mission.get(missionNumber);



        }else{
            if(currentMission.get("clear").getAsBoolean()){
                missionNumber++;
                if (missionNumber >= mission.size()) {
                    // 全ミッションクリア時の処理
                    player.sendMessage("§α[村人] §fすべてのたからをみつけたね！おめでとう！");
                    return;
                }
                setupNewMission(player);
            }
        }
    }


    private void setupNewMission(Player player){
        currentMission = mission.get(missionNumber);

        String itemName = currentMission.get("itemId").getAsString();

        try{
            Material correntMaterial = Material.valueOf(itemName);
            ItemStack correctItem = new ItemStack(correntMaterial);

            List<ItemStack> dummyItems = Arrays.asList(new ItemStack(Material.DIRT), new ItemStack(Material.COBBLESTONE));

            chestManager.setupMissionChest(correctItem, dummyItems);

            String missionName = currentMission.get("name").getAsString();
            new DelayedMessageTask(plugin, player, Arrays.asList("§α[村人] §fつぎのおだいは…", "§e" + missionName + "！")).runTaskLater(plugin, 200L);

        }catch (IllegalArgumentException e) {
            // Material.valueOfで指定された名前のMaterialが見つからなかった場合のエラー処理
            plugin.getLogger().severe("mission.jsonのitem名が無効です: " + itemName);
            player.sendMessage("§c[エラー] ミッションの設定に誤りがあります。");
        }
    }

}
