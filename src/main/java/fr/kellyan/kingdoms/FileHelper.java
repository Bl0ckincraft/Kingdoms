package fr.kellyan.kingdoms;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is a helper which is used to save/load files.
 */
public class FileHelper {
    /**
     * Get the lang.yml configuration from the file.
     * @return the lang configuration.
     */
    public static FileConfiguration getLangConfig() {
        //Load the lang file.
        File langFile = new File(Kingdoms.getInstance().getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            Kingdoms.getInstance().saveResource("lang.yml", false);
        }

        //Load the lang config from the lang file.
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        final InputStream defConfigStream = Kingdoms.getInstance().getResource("lang.yml");
        if (defConfigStream != null) {
            langConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }

        return langConfig;
    }
}
