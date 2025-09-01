package mee.example.hiragana_learn;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DelayedMessageTask extends BukkitRunnable {
    private final Player player;
    private final List<String> messages;
    private int messageIndex = 1; // 次に送信するメッセージのインデックス

    public DelayedMessageTask(JavaPlugin plugin, Player player, List<String> messages) {
        this.player = player;
        this.messages = messages;
    }

    @Override
    public void run() {
        // プレイヤーがオフラインになったか、メッセージを全て送り終えたらタスクを停止
        if (!player.isOnline() || messageIndex >= messages.size()) {
            this.cancel(); // このタスクを停止する
            return;
        }

        // リストからメッセージを1つ送信
        player.sendMessage(messages.get(messageIndex));

        // 次のメッセージを指すようにインデックスを増やす
        messageIndex++;
    }
}
