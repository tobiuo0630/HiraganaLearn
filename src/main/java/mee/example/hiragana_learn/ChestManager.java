package mee.example.hiragana_learn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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

    public void setupMissionChest(ItemStack correctItem,List<ItemStack> dummyItems){

        clearAllChest();

        List<Location> shuffledLocations = new ArrayList<>(this.chestLocations);
        Collections.shuffle(shuffledLocations);

        setChestItem(shuffledLocations.get(0),correctItem);

        for(int i=0;i<dummyItems.size();i++){
            if(i+1 >= shuffledLocations.size()){
                break;
            }
            setChestItem(shuffledLocations.get(i+1),dummyItems.get(i));
        }
    }

    private void clearAllChest(){
        for(Location loc: chestLocations){
            Block block = loc.getBlock();
            if(block.getState() instanceof Chest){
                Chest chest = (Chest) block.getState();
                chest.getInventory().clear();
            }
        }
    }


    private void setChestItem(Location loc,ItemStack item){
        Block block = loc.getBlock();
        if(block.getState() instanceof Chest){
            Chest chest = (Chest) block.getState();
            chest.getInventory().clear();
            int randomSlot = (int) (Math.random() * chest.getInventory().getSize());
            chest.getInventory().setItem(randomSlot,item);
        }
    }
}
