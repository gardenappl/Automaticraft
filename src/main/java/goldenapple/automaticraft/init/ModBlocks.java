package goldenapple.automaticraft.init;

import goldenapple.automaticraft.block.*;
import goldenapple.automaticraft.reference.Names;
import goldenapple.automaticraft.util.Filter;
import net.minecraft.block.Block;

public class ModBlocks {
    public static Block conveyor;
    public static Block conveyor_redstone;
    public static Block conveyor_sticky;
    public static Block conveyor_detector;
    public static Block conveyor_detector_precise;
    public static Block conveyor_detector_fuzzy;
    public static Block conveyor_detector_ore;
    public static Block detector;

    public static void init(){
        conveyor = new BlockConveyor();
        conveyor_redstone = new BlockConveyorPowered();
        conveyor_sticky = new BlockConveyorSitcky();
        conveyor_detector = new BlockConveyorDetector(Names.CONVEYOR_DETECTOR, Filter.NORMAL);
        conveyor_detector_precise = new BlockConveyorDetector(Names.CONVEYOR_DETECTOR_PRECISE, Filter.PRECISE);
        conveyor_detector_fuzzy = new BlockConveyorDetector(Names.CONVEYOR_DETECTOR_FUZZY, Filter.FUZZY);
        conveyor_detector_ore = new BlockConveyorDetector(Names.CONVEYOR_DETECTOR_ORE, Filter.ORE);
        detector = new BlockDetector();
    }
}