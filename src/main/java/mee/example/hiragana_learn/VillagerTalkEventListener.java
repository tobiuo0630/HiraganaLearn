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

            setupNewMission(player);
        }else{
            if (currentMission == null) return;

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

            } else {
                // --- 不正解時の処理 ---
                UUID playerUUID = player.getUniqueId();
                int attempts = wrongAttempts.getOrDefault(playerUUID, 0) + 1;
                wrongAttempts.put(playerUUID, attempts);

                if (attempts >= MAX_WRONG_ATTEMPTS) {
                    // 失敗回数が上限に達した
                    player.sendMessage("§c[村人] §fざんねん…3かいまちがえてしまったね。");
                    player.sendTitle("§cゲームオーバー", "§fまたちょうせんしてね", 10, 100, 20);

                    // ゲームをリセットするなどの処理をここに追加（例：スタート地点に戻す）
                    // EventListenerにあるテレポート処理を参考に実装できます

                    wrongAttempts.remove(playerUUID); // カウントをリセット
                    // 必要であれば、ミッション進行度もリセット
                    // missionNumber = 0;

                } else {
                    // まだ挑戦できる
                    int remaining = MAX_WRONG_ATTEMPTS - attempts;
                    player.sendMessage("§c[村人] §fうーん、ちがうようだね。");
                    player.sendMessage("§cのこり" + remaining + "かいまでまちがえられるよ。");
                }
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
            new DelayedMessageTask(plugin, player, Arrays.asList("§a[村人] §fつぎのおだいは…", "§e" + missionName + "！")).runTaskLater(plugin, 200L);

        }catch (IllegalArgumentException e) {
            // Material.valueOfで指定された名前のMaterialが見つからなかった場合のエラー処理
            plugin.getLogger().severe("mission.jsonのitem名が無効です: " + itemName);
            player.sendMessage("§c[エラー] ミッションの設定に誤りがあります。");
        }
    }

}
