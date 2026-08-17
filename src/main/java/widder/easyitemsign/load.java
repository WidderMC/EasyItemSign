package widder.easyitemsign;

import static net.fabricmc.fabric.impl.networking.NetworkingImpl.LOGGER;

public class load {

    public static boolean signature = true;
    public static boolean date = true;
    public static boolean name = true;
    public static boolean boldName = true;
    public static boolean boldDate = true;
    public static String defultColor = "#AAAAAA";
    public static String nameColor = "#FFAA00";
    public static String dateColor = "#55FFFF";


    public static int load() {

        LOGGER.info("load");

        return 1;
    }
}
