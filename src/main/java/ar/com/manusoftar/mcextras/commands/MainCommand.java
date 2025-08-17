package ar.com.manusoftar.mcextras.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StructureSearchResult;

import ar.com.manusoftar.mcextras.utils.Utils;

@SuppressWarnings({ "deprecation", "unused" })
public class MainCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final NamespacedKey customMapKey;
    private final Utils utils;

    public MainCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.customMapKey = new NamespacedKey(plugin, "exploration_map_tag");
        this.utils = new Utils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        World mundoSurvival = Bukkit.getWorld("Principal");
        switch (command.getName().toLowerCase()) {
            case "gettreasuremap":
                if (!(sender instanceof org.bukkit.entity.Player)) {
                    sender.sendMessage("Este comando solo puede ser ejecutado por jugadores!");
                    return true;
                }

                //sender.sendMessage("Este es un comando de getTreasureMap!");

                //sender.sendMessage("§aBuscando un tesoro, por favor espera...");

                // Ejecutar la tarea de búsqueda en un hilo asíncrono

                if (mundoSurvival == null) {
                    sender.sendMessage("§cEl mundo de supervivencia 'Principal' no existe.");
                    return true;
                }

                // Usar una ubicación de inicio del mundo para evitar problemas de chunks no
                // cargados
                Location startLocation = player.getLocation();

                sender.sendMessage("Voy a buscar un tesoro cerca de: " + startLocation.getBlockX() + ", "
                        + startLocation.getBlockZ());

                StructureSearchResult resultado = mundoSurvival.locateNearestStructure(startLocation,
                        StructureType.BURIED_TREASURE, 300, true);
                if (resultado == null) {
                    sender.sendMessage("§cNo se encontró ningún tesoro cercano.");
                    return true;
                }
                // Buscar la estructura del tesoro de forma segura
                Location treasureLocation = resultado.getLocation();

                // Volver al hilo principal para crear y dar el mapa

                if (treasureLocation != null) {
                    ItemStack treasureMap = Bukkit.createExplorerMap(mundoSurvival, treasureLocation,
                            org.bukkit.StructureType.BURIED_TREASURE);
                    MapMeta mapMeta = (MapMeta) treasureMap.getItemMeta();
                    mapMeta.getMapView().setTrackingPosition(true);

                    if (treasureMap != null) {
                        player.getInventory().addItem(treasureMap);
                    }
                    sender.sendMessage("§a¡Mapa del tesoro obtenido! Ubicación: " + treasureLocation.getBlockX() + ", "
                            + treasureLocation.getBlockZ());
                } else {
                    sender.sendMessage("§cNo se pudo encontrar el tesoro en el radio especificado.");
                }

                return true;
            // break;

            case "gettriggeritem":
                
                String structureName = args[0].toUpperCase();
                if (!player.isOp()) { // O puedes usar un permiso: if (!player.hasPermission("mcextras.givemap"))
                    player.sendMessage("No tienes permiso para usar este comando.");
                    return true;
                }

                // Creación del mapa en blanco
                ItemStack blankMap = new ItemStack(Material.MAP);
                ItemMeta meta = blankMap.getItemMeta();

                // Añadimos el NBT tag personalizado al ítem
                meta.getPersistentDataContainer().set(customMapKey, PersistentDataType.STRING, "true");
                meta.setDisplayName("§f" + "Mapa de Exploración"); // Sigue siendo útil para la visualización

                blankMap.setItemMeta(meta);

                player.getInventory().addItem(blankMap);
                player.sendMessage("¡Has recibido un mapa de exploración!");
                return true;
            // break;

            case "getstructuremap":
                if (!sender.hasPermission("mcextras.getstructuremap")) {
                    sender.sendMessage("§cNo tienes permiso para usar este comando.");
                    return true;
                }

                if (args.length < 1) {
                    sender.sendMessage("§cUso: /getstructuremap <estructura>");
                    return true;
                }
                String structureName = args[0].toUpperCase();
                StructureType structureType = null;

                org.bukkit.StructureType estructura = null;

                structureType = utils.getEstructurasConocidas(structureName);
                estructura = utils.traducir(structureType);

                player.sendMessage("Tipo de estructura encontrada -> " + estructura.getName());

                if (structureType == null) {
                    String available = utils.getTiposConocidos();
                    player.sendMessage("§cTipo de estructura no válido. Usa nombres como: " + available);
                    return true;
                }
                World world = player.getWorld();
                player.sendMessage("§aBuscando la estructura " + structureType.getKey().getKey() + "...");
                org.bukkit.util.StructureSearchResult result = world.locateNearestStructure(player.getLocation(),
                        structureType, 1000, false);
                if (result != null && result.getLocation() != null) {
                    Location structureLocation = result.getLocation();
                    // Generar mapa a partir de la ubicación encontrada
                    ItemStack treasureMap = Bukkit.createExplorerMap(mundoSurvival, structureLocation, estructura);
                    MapMeta mapMeta = (MapMeta) treasureMap.getItemMeta();
                    mapMeta.getMapView().setTrackingPosition(true);
                    mapMeta.getMapView().setLocked(true);
                    if (treasureMap != null) {
                        player.getInventory().addItem(treasureMap);
                    }
                    player.sendMessage("§a¡Estructura encontrada! Coordenadas: §eX: " + structureLocation.getBlockX() +
                            ", Y: " + structureLocation.getBlockY() +
                            ", Z: " + structureLocation.getBlockZ());
                } else {
                    player.sendMessage(
                            "§cNo se pudo encontrar ninguna estructura " + structureType.getKey().getKey() + " cerca.");
                }
                return true;
        }
        return true;
    }

}