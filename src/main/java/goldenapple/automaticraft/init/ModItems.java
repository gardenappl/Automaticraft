package goldenapple.automaticraft.init;

import goldenapple.automaticraft.item.ItemDebug;
import goldenapple.automaticraft.item.ItemMultiMetadata;
import goldenapple.automaticraft.reference.Names;
import net.minecraft.item.Item;

public class ModItems {
    public static Item debug;
    public static Item material;

    public static void init() {
        debug = new ItemDebug();
        material = new ItemMultiMetadata(Names.MATERIAL, Names.MATERIALS);
    }
}
