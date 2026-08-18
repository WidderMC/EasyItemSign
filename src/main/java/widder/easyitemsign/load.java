package widder.easyitemsign;

import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.Properties;

import static net.fabricmc.fabric.impl.networking.NetworkingImpl.LOGGER;

public class load {
    public static boolean signature = true;
    public static boolean date = true;
    public static boolean name = true;
    public static boolean boldDate = true;
    public static boolean boldName = true;
    public static String defaultColor = "#AAAAAA";
    public static String nameColor = "#FFAA00";
    public static String dateColor = "#55FFFF";

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("EasyItemSign.properties");
    private static final Properties CONFIG = new Properties();


    public static int load() {
        File file = CONFIG_PATH.toFile();

        //Test if the File exist, if not Create it
        if (!file.exists()) {
            createDefaultConfig(file);
        }

        //Try to Load Configs from the File
        try (FileInputStream in = new FileInputStream(file)) {
            CONFIG.load(in);

            signature = Boolean.parseBoolean(CONFIG.getProperty("Signature"));
            date = Boolean.parseBoolean(CONFIG.getProperty("Date"));
            name = Boolean.parseBoolean(CONFIG.getProperty("Name"));
            boldDate = Boolean.parseBoolean(CONFIG.getProperty("BoldDate"));
            boldName = Boolean.parseBoolean(CONFIG.getProperty("BoldName"));
            defaultColor = CONFIG.getProperty("DefaultColor");
            nameColor = CONFIG.getProperty("NameColor");
            dateColor = CONFIG.getProperty("DateColor");

            EasyItemSign.LOGGER.info("Configuration successfully loaded");
        } catch (Exception e) {
            EasyItemSign.LOGGER.error("Error loading config file!", e);
        }

        return 1;
    }

    private static void createDefaultConfig(File file) {
        try {
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("#Turn on or off the Signature in the Item Lore (default: true)");
                writer.newLine();
                writer.write("Signature=" + signature);
                writer.newLine();
                writer.newLine();

                writer.write("#Turn on or off the Date in the Signature (default: true)");
                writer.newLine();
                writer.write("Date=" + date);
                writer.newLine();
                writer.newLine();

                writer.write("#Turn on or off the Name in the Signature (default: true)");
                writer.newLine();
                writer.write("Name=" + name);
                writer.newLine();
                writer.newLine();

                writer.write("#Turn on or off the BoldDate in the Signature (default: true)");
                writer.newLine();
                writer.write("BoldDate=" + boldDate);
                writer.newLine();
                writer.newLine();

                writer.write("#Turn on or off the BoldName in the Signature (default: true)");
                writer.newLine();
                writer.write("BoldName=" + boldName);
                writer.newLine();
                writer.newLine();

                writer.write("#Set the Default Color (Hex-Code) (default: #AAAAAA)");
                writer.newLine();
                writer.write("DefaultColor=" + defaultColor);
                writer.newLine();
                writer.newLine();

                writer.write("#Set the Name Color (Hex-Code) (default: #FFAA00)");
                writer.newLine();
                writer.write("NameColor=" + nameColor);
                writer.newLine();
                writer.newLine();

                writer.write("#Set the Date Color (Hex-Code) (default: #55FFFF)");
                writer.newLine();
                writer.write("DateColor=" + dateColor);
                writer.newLine();
                writer.newLine();
            }
        } catch (Exception e) {
            EasyItemSign.LOGGER.error("Error creating default config file!", e);
        }
    }
}
