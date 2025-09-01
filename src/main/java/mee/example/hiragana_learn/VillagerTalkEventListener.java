package mee.example.hiragana_learn;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class VillagerTalkEventListener implements Listener {
    private final JavaPlugin plugin;
    private final List<JsonObject> mission;
    private final NamespacedKey explainedRuleKey;
    private final List<String> VillagerMessage;
    private int missionNumber;
    private JsonObject currentMission;

    private final ChestManager chestManager;

    // プレイヤーごとに間違えた回数を保持するマップ
    private final HashMap<UUID, Integer> wrongAttempts = new HashMap<>();
    // 間違いの上限回数
    private static final int MAX_WRONG_ATTEMPTS = 3;

    public VillagerTalkEventListener(JavaPlugin plugin, List<JsonObject> mission){
        this.plugin = plugin;
        this.mission = mission;
        this.explainedRuleKey = new NamespacedKey(plugin,"explained_rule");
        this.missionNumber = 0;
        this.VillagerMessage = Arrays.asList(
                "§α[村人] §fこんにちは、わたしはむらたさん。",
                "§α[村人] §fこれから、きみにはたからさがしをしてもらうよ。",
                "§α[村人] §fおだいのあいてむをここまでもってきてね。",
                "§α[村人] §fそれでは、ゲームスタート",
                "§α[村人] §f最初のミッションは" + mission.get(0)
        );

        World mainWorld = Bukkit.getWorlds().get(0);
        this.chestManager = new ChestManager(mainWorld);
    }

    @EventHandler
    private void VillagerTalkEvent(PlayerInteractEntityEvent event){


        Player player = event.getPlayer();
        player.sendMessage("§e[デバッグ1] §fonVillagerTalkイベント発生！");

        if (event.getRightClicked() != null) {
            String entityTypeName = event.getRightClicked().getType().name();
            player.sendMessage("§e[デバッグ2] §fクリックしたエンティティ: " + entityTypeName);
        }

        if (!(event.getRightClicked() instanceof Villager)) {
            return; // 村人以外ならここで処理を終了
        }

        player.sendMessage("§e[デバッグ3] §a村人との対話を開始します。");

        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!data.has(explainedRuleKey, PersistentDataType.BYTE)){
            new DelayedMessageTask(plugin,player,VillagerMessage).runTaskTimer(plugin,0L,200L);
            data.set(explainedRuleKey,PersistentDataType.BYTE,(byte) 1);
        }else{
            if (currentMission == null){
                player.sendMessage("§a[村人] §fまた何かあったらよろしくね！");
                return;
            }

            String correctItemName = currentMission.get("item").getAsString(); // JSONのキーを"item"に修正
            Material correctMaterial = Material.valueOf(correctItemName);
            ItemStack correctItem = new ItemStack(correctMaterial);

            if(player.getInventory().containsAtLeast(correctItem, 1)){
                // --- 正解時の処理 ---
                player.sendMessage("§a[村人] §fせいかい！よくみつけたね！");
                player.getInventory().removeItem(correctItem); // インベントリから正解アイテムを1つ削除

                wrongAttempts.remove(player.getUniqueId()); // 間違えた回数をリセット

                missionNumber++;
                if (missionNumber >= mission.size()) {
                    // 全ミッションクリア
                    player.sendMessage("§a[村人] §fすべてのたからをみつけたね！おめでとう！");
                    player.sendTitle("§eゲームクリア！", "§fおめでとう！", 10, 100, 20);
                    currentMission = null; // ミッションを終了
                    return;
                }
                // 次のミッションへ
                setupNewMission(player);

            }
        }
    }


    private void setupNewMission(Player player){
        currentMission = mission.get(missionNumber);

        String itemName = currentMission.get("item").getAsString();
        player.sendMessage(itemName);

        try{
            Material correntMaterial = Material.valueOf(itemName);
            ItemStack correctItem = new ItemStack(correntMaterial);

            List<ItemStack> dummyItems = Arrays.asList(new ItemStack(Material.DIRT), new ItemStack(Material.COBBLESTONE));

            chestManager.setupMissionChest(player,correctItem, dummyItems);

            String missionName = currentMission.get("name").getAsString();
            new DelayedMessageTask(plugin, player, Arrays.asList("§a[村人] §fつぎのおだいは…"+"§e" + missionName + "！","1")).runTaskLater(plugin, 200L);

        }catch (IllegalArgumentException e) {
            // Material.valueOfで指定された名前のMaterialが見つからなかった場合のエラー処理
            plugin.getLogger().severe("mission.jsonのitem名が無効です: " + itemName);
            player.sendMessage("§c[エラー] ミッションの設定に誤りがあります。");
        }
    }

}
