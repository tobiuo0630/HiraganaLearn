package mee.example.hiragana_learn;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ResetDataCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ResetDataCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // コマンド実行者がプレイヤーかどうかを確認
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみが実行できます。");
            return true;
        }

        Player player = (Player) sender;
        PersistentDataContainer data = player.getPersistentDataContainer();

        // VillagerTalkEventListenerで使っているのと同じキーを作成
        NamespacedKey explainedRuleKey = new NamespacedKey(plugin, "explained_rule");

        // キーが存在するか確認
        if (data.has(explainedRuleKey, PersistentDataType.BYTE)) {
            // キーを削除してデータをリセット
            data.remove(explainedRuleKey);
            player.sendMessage("§a[成功] §fクエストの進行状況データをリセットしました。");
            player.sendMessage("§fもう一度村人に話しかけると、最初の説明から始まります。");
        } else {
            player.sendMessage("§e[情報] §fリセットするデータが見つかりませんでした。");
        }

        return true;
    }
}