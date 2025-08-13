package mee.example.hiragana_learn;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Hiragana_learn extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        String mission_file_name = "mission.json";
        File mission_json = new File(getDataFolder(), mission_file_name);

        List<JsonObject> missionData = null;
        try (FileReader reader = new FileReader(mission_json)) {
            Gson gson = new Gson();
            JsonObject[] mission = gson.fromJson(reader, JsonObject[].class);
            missionData = new ArrayList<>(Arrays.asList(mission));
            for (JsonObject i : missionData) {
                String name = i.get("name").getAsString();
                getLogger().info("missionName:" + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("JSONの読み込みに失敗しました");
        }

        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
