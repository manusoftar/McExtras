package ar.com.manusoftar.mcextras.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.generator.structure.StructureType;

@SuppressWarnings({"deprecation"})
public class Utils {
    private final static HashMap<StructureType, org.bukkit.StructureType> diccionarioEstructuras = new HashMap<StructureType, org.bukkit.StructureType>();

                // org.bukkit.generator.structure.StructureType.BURIED_TREASURE,
                // org.bukkit.generator.structure.StructureType.MINESHAFT,
                // org.bukkit.generator.structure.StructureType.SHIPWRECK,
                // org.bukkit.generator.structure.StructureType.STRONGHOLD,
                // org.bukkit.generator.structure.StructureType.DESERT_PYRAMID,
                // org.bukkit.generator.structure.StructureType.SWAMP_HUT,
                // org.bukkit.generator.structure.StructureType.IGLOO,
                // org.bukkit.generator.structure.StructureType.OCEAN_RUIN,                
                // org.bukkit.generator.structure.StructureType.WOODLAND_MANSION,               
                // org.bukkit.generator.structure.StructureType.RUINED_PORTAL



    static {
           
            diccionarioEstructuras.clear();
            diccionarioEstructuras.put(StructureType.BURIED_TREASURE, org.bukkit.StructureType.BURIED_TREASURE);
            diccionarioEstructuras.put(StructureType.MINESHAFT, org.bukkit.StructureType.MINESHAFT);
            diccionarioEstructuras.put(StructureType.SHIPWRECK, org.bukkit.StructureType.SHIPWRECK);
            diccionarioEstructuras.put(StructureType.STRONGHOLD, org.bukkit.StructureType.STRONGHOLD);
            diccionarioEstructuras.put(StructureType.DESERT_PYRAMID, org.bukkit.StructureType.DESERT_PYRAMID);
            diccionarioEstructuras.put(StructureType.SWAMP_HUT, org.bukkit.StructureType.SWAMP_HUT);
            diccionarioEstructuras.put(StructureType.IGLOO, org.bukkit.StructureType.IGLOO);
            diccionarioEstructuras.put(StructureType.OCEAN_RUIN, org.bukkit.StructureType.OCEAN_RUIN);
            diccionarioEstructuras.put(StructureType.OCEAN_MONUMENT, org.bukkit.StructureType.OCEAN_MONUMENT);
            diccionarioEstructuras.put(StructureType.WOODLAND_MANSION, org.bukkit.StructureType.WOODLAND_MANSION);
            diccionarioEstructuras.put(StructureType.RUINED_PORTAL, org.bukkit.StructureType.RUINED_PORTAL);
            diccionarioEstructuras.put(StructureType.JUNGLE_TEMPLE, org.bukkit.StructureType.JUNGLE_PYRAMID);

    }

    private final static List<StructureType> estructurasConocidas = new LinkedList<StructureType>();

    static {
        estructurasConocidas.addAll(
                    java.util.Arrays.asList(
                        StructureType.BURIED_TREASURE,
                        StructureType.MINESHAFT,
                        StructureType.SHIPWRECK,
                        StructureType.STRONGHOLD,
                        StructureType.DESERT_PYRAMID,
                        StructureType.SWAMP_HUT,
                        StructureType.IGLOO,
                        StructureType.OCEAN_RUIN,                
                        StructureType.WOODLAND_MANSION,               
                        StructureType.RUINED_PORTAL,
                        StructureType.JUNGLE_TEMPLE,
                        StructureType.OCEAN_MONUMENT
                        
                    )
             );
    }

    public StructureType getEstructurasConocidas(String structureName) {        
        StructureType structureType = null;
        for (StructureType type : Utils.estructurasConocidas) {
                if (type.getKey().getKey().equalsIgnoreCase(structureName)) {
                    structureType = type;
                    break;
                }
        }
        return structureType;
    }

    public org.bukkit.StructureType traducir(StructureType origen) {
           return diccionarioEstructuras.get(origen);
    }

    public String getTiposConocidos() {
        return String.join(", ", estructurasConocidas.stream().map(t -> t.getKey().getKey()).toArray(String[]::new));
    }
}
