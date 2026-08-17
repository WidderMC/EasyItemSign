package widder.easyitemsign;

import static net.fabricmc.fabric.impl.networking.NetworkingImpl.LOGGER;

public class load {

    public boolean signature = true;
    public boolean date = true;
    public boolean name = true;
    public boolean boldName = true;
    public boolean boldDate = true;
    public String defultColor = "#AAAAAA";
    public String nameColor = "#FFAA00";
    public String dateColor = "#55FFFF";


    public static int load() {

        LOGGER.info("load");

        return 1;
    }
}
