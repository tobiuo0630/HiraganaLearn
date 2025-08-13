package mee.example.hiragana_learn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChestManager {
    private final List<Location> chestLocations;

    public ChestManager(World world){
        this.chestLocations = Arrays.asList(
                new Location(world,-9,-9,93),//体育館壇上
                new Location(world,32,-6,93),//体育館キャットウォーク
                new Location(world,15,-11,93),//体育館コート中央
                new Location(world,77,-10,72),//プール
                new Location(world,13,-11,60),//グラウンド
                new Location(world,26,-9,132),//1-1
                new Location(world,34,-9,132),//1-2
                new Location(world,34,-9,148),//1-3
                new Location(world,18,-9,152),//1階トイレ
                new Location(world,0,-9,132),//職員室
                new Location(world,-16,-8,136),//校長室
                new Location(world,-14,-9,151),//保健室
                new Location(world,-7,-9,146),//用具入れ
                new Location(world,18,-2,132),//2-1
                new Location(world,-14,-2,141),//視聴覚室
                new Location(world,-5,-2,131),//理科室
                new Location(world,-18,-2,131),//理科準備室
                new Location(world,18,-2,152),//2階トイレ
                new Location(world,44,-2,140),//図書館
                new Location(world,27,4,140)//屋上
        );
    }

    public void setupMissionChest(Player player, ItemStack correctItem, List<ItemStack> dummyItems){
        player.sendMessage("§e[デバッグ] setupMissionChestが呼び出されました。");
        clearAllChest(player);

        List<Location> shuffledLocations = new ArrayList<>(this.chestLocations);
        Collections.shuffle(shuffledLocations);

        player.sendMessage("§e[デバッグ] 正解アイテム: " + correctItem.getType().name());
        setChestItem(shuffledLocations.get(0),correctItem,player);

        for(int i=0;i<dummyItems.size();i++){
            if(i+1 >= shuffledLocations.size()){
                break;
            }
            player.sendMessage("§e[デバッグ] ダミーアイテム: " + dummyItems.get(i).getType().name());
            setChestItem(shuffledLocations.get(i+1),dummyItems.get(i),player);
        }
    }

    private void clearAllChest(Player player){
        int clearedCount = 0;
        for(Location loc: chestLocations){
            if (loc.getChunk().isLoaded()) {
                Block block = loc.getBlock();
                if(block.getState() instanceof Chest){
                    Chest chest = (Chest) block.getState();
                    chest.getInventory().clear();
                    clearedCount++;
                }
            }
        }
        player.sendMessage("§e[デバッグ] §fロード済みチャンク内の " + clearedCount + " 個のチェストを空にしました。");
    }


    private void setChestItem(Location loc,ItemStack item,Player player){
        if (!loc.getChunk().isLoaded()) {
            player.sendMessage("§c[デバッグ] §fチャンクがロードされていません: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            return;
        }

        Block block = loc.getBlock();
        if(block.getState() instanceof Chest){
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();
            int randomSlot = (int) (Math.random() * chest.getInventory().getSize());
            chest.getInventory().setItem(randomSlot,item);
            player.sendMessage("§a[デバッグ] §fアイテム設置完了: " + item.getType().name() + " at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        }else{
            player.sendMessage("§c[デバッグ] §f指定座標にチェストがありません: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        }
    }
}
