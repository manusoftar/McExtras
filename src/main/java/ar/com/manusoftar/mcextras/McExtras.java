package ar.com.manusoftar.mcextras;

import org.bukkit.plugin.java.JavaPlugin;
import ar.com.manusoftar.mcextras.commands.MainCommand;
import ar.com.manusoftar.mcextras.utils.LanguageManager;

public class McExtras extends JavaPlugin {

    public static LanguageManager langManager;
    // Variables para almacenar la configuración
    public static String SURVIVAL_WORLD_NAME;
    public static String EXPLORATION_MAP_NAME;
    public static String TREASURE_MAP_NAME;

    @Override
    public void onEnable() {
        getLogger().info("McExtras has been enabled!");
        langManager = new LanguageManager(this);
        this.saveDefaultConfig();
        // Guardar archivos de idioma
        saveResource("lang/en_US.yml", false);
        saveResource("lang/es_ES.yml", false);

        // Cargar la configuración
        SURVIVAL_WORLD_NAME = getConfig().getString("survival_world_name", "Principal");
        EXPLORATION_MAP_NAME = getConfig().getString("exploration_map_name", "Mapa de Exploración");
        TREASURE_MAP_NAME = getConfig().getString("treasure_map_name", "Mapa del Tesoro");      

        registerListeners();
        registerCommands();
    }
    
    @Override
    public void onDisable() {
        getLogger().info("McExtras has been disabled!");
    }

    
    public void registerCommands() {
        //getCommand("test").setExecutor(new MainCommand(this));
        getCommand("getTreasureMap").setExecutor(new MainCommand(this));
        getCommand("getTriggerItem").setExecutor(new MainCommand(this));
        getCommand("getStructureMap").setExecutor(new MainCommand(this));
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new ar.com.manusoftar.mcextras.listeners.EventListener(this), this);
    }

}
