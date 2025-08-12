package mee.example.hiragana_learn;

import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class EventListener implements Listener {
    private final JavaPlugin plugin;

    private boolean start = true;

    public EventListener(JavaPlugin plugin,ArrayList<JsonObject> mission){
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        World world = player.getWorld();

        if(start && block!=null && block.getType().name().contains("OAK_WALL_SIGN")){
            start = false;
            start_player_teleport(player,world);
        }
    }


    //スタート時実行関数
    private void start_player_teleport(Player player,World world){
        final float start_x = -44.5f;
        final float start_y = -11.0f;
        final float start_z = 114.5f;

        Location start_location = new Location(world,start_x,start_y,start_z);
        start_location.setYaw(0f);
        start_location.setPitch(0f);
        player.teleport(start_location);
        player.sendMessage("ゲームスタート");
        player.sendTitle("ゲームスタート","クリアをめざそう",10,100,20);
    }

}
