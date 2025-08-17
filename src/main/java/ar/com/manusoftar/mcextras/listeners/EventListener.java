package ar.com.manusoftar.mcextras.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCursor;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StructureSearchResult;

import ar.com.manusoftar.mcextras.McExtras;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class EventListener implements Listener {

    
    // Aquí puedes agregar métodos para manejar eventos específicos de Bukkit
    // Por ejemplo, puedes manejar eventos de jugador, bloques, etc.

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("¡Bienvenido al servidor!");
    }

    // @EventHandler
    // public void onBlockBreak(BlockBreakEvent event) {
    //     Player player = event.getPlayer();
    //     player.sendMessage("Has roto un bloque: " + event.getBlock().getType());
    // }

    // Agrega más métodos de manejo de eventos según sea necesario
    
    //private final JavaPlugin plugin;
    private final NamespacedKey customMapKey;
    //private final String customName = McExtras.EXPLORATION_MAP_NAME;

    public EventListener(JavaPlugin plugin) {
        //this.plugin = plugin;
        this.customMapKey = new NamespacedKey(plugin, "exploration_map_tag");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerLocale = player.locale().toString();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        // Check for right-click action
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Check if the item is a FILLD_MAP with the correct custom name
        if (heldItem.getType() == Material.MAP && heldItem.hasItemMeta() && heldItem.getItemMeta().hasDisplayName()) {
            ItemMeta meta = heldItem.getItemMeta();
            if (meta.getPersistentDataContainer().has(customMapKey, PersistentDataType.STRING)) {
                // Cancel the event to prevent the blank map from opening
                event.setCancelled(true);
                //player.sendMessage("El locale detectado es: " + playerLocale);
                player.sendMessage(McExtras.langManager.getMessage(playerLocale, "searching"));

                // Execute map generation on the main thread to avoid IllegalStateException
                generateTreasureMap(player);
                
            }
        }
    }

    private void generateTreasureMap(Player player) {
            String playerLocale = player.locale().toString();
            World mundoSurvival = Bukkit.getWorld(McExtras.SURVIVAL_WORLD_NAME);
            if (mundoSurvival == null) {
                player.sendMessage(McExtras.langManager.getMessage(playerLocale, "no-survival-world"));
                return;
            }
            
            // Usar una ubicación de inicio del mundo para evitar problemas de chunks no cargados            
            Location startLocation;
            if (player.getWorld().equals(mundoSurvival)) {
                startLocation = player.getLocation();
            } else {
                startLocation = mundoSurvival.getSpawnLocation();
            }

            //player.sendMessage("Voy a buscar un tesoro cerca de: " + startLocation.getBlockX() + ", " + startLocation.getBlockZ());

            StructureSearchResult resultado = mundoSurvival.locateNearestStructure(startLocation, StructureType.BURIED_TREASURE, 300, true);
            if (resultado == null) {
                player.sendMessage(McExtras.langManager.getMessage(playerLocale, "not-found"));
                return;
            }
            // Buscar la estructura del tesoro de forma segura
            Location treasureLocation = resultado.getLocation();
            
            // Volver al hilo principal para crear y dar el mapa
       
                if (treasureLocation != null) {

                    MapCursor.Type mapIcon = MapCursor.Type.RED_X;                    
                    ItemStack treasureMap = Bukkit.getServer().createExplorerMap(mundoSurvival, treasureLocation, StructureType.BURIED_TREASURE, mapIcon, 500, true);
                    MapMeta mapMeta = (MapMeta)treasureMap.getItemMeta();
                    mapMeta.getMapView().setTrackingPosition(true);                    
                 
                    if (treasureMap != null) {
                        //player.getInventory().addItem(treasureMap);                        
                        player.getInventory().setItemInMainHand(treasureMap);
                    }
                    player.sendMessage(McExtras.langManager.getMessage(playerLocale, "found")  + treasureLocation.getBlockX() + ", " + treasureLocation.getBlockZ());
                } else {
                    player.sendMessage(McExtras.langManager.getMessage(playerLocale, "not-found"));
                }
    }

}
