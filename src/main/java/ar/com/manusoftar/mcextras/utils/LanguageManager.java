package ar.com.manusoftar.mcextras.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageManager {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> langFiles = new HashMap<>();

    public LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadLanguages();
    }

    private void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // Cargar todos los archivos YML en la carpeta 'lang'
        for (File file : langFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                langFiles.put(file.getName().replace(".yml", ""), YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    public FileConfiguration getLanguageFile(String locale) {
        return langFiles.getOrDefault(locale, langFiles.get("en_US")); // Idioma por defecto
    }

    public String getMessage(String locale, String path) {        
        FileConfiguration lang = getLanguageFile(locale);
        String message = lang.getString("messages." + path);
        return message != null ? message : langFiles.get("en_US").getString("messages." + path);
    }
}
