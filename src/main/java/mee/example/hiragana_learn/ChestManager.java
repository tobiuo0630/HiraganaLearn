package mee.example.hiragana_learn;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public class ChestManager {
    private final List<Location> chestLocations;

    public ChestManager(World world){
        this.chestLocations = Arrays.asList(
                new Location(world,-9,-10,93),//体育館壇上
                new Location(world,32,-7,93),//体育館キャットウォーク
                new Location(world,15,-12,93),//体育館コート中央
                new Location(world,77,-11,72),//プール
                new Location(world,13,-12,60),//グラウンド
                new Location(world,26,-10,132),//1-1
                new Location(world,34,-10,132),//1-2
                new Location(world,34,-10,148),//1-3
                new Location(world,18,-10,152),//1階トイレ
                new Location(world,0,-10,132),//職員室
                new Location(world,-16,-9,136),//校長室
                new Location(world,-14,-10,151),//保健室
                new Location(world,-7,-10,146),//用具入れ
                new Location(world,18,-3,132),//2-1
                new Location(world,-14,-3,141),//視聴覚室
                new Location(world,-5,-3,131),//理科室
                new Location(world,-18,-3,131),//理科準備室
                new Location(world,18,-3,152),//2階トイレ
                new Location(world,44,-3,140),//図書館
                new Location(world,27,3,140)//屋上
        );
    }
}
