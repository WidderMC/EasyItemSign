package widder.easyitemsign.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.jspecify.annotations.Nullable;
import widder.easyitemsign.EasyItemSign;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sign {
    public static int sign(CommandSourceStack source, String text) {

        @Nullable Entity entity = source.getEntity();
        if (!(entity instanceof ServerPlayer player)) {
            source.sendSuccess(() -> Component.literal("Command must be executed by a Player"),false);
            return 0;
        }

        ItemStack item = player.getMainHandItem();
        if (item.isEmpty()) {
            source.sendSuccess(() -> Component.literal("You has to hold an item in their Main Hand"),false);
            return 0;
        }

        LocalDate currentDate = LocalDate.now();
        String playername = player.getName().getString();
        String dateString = currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));




        Component finalText = ApplyStyle(text);

        //MutableComponent ****************************************************
        Component Meta = Component.literal("Sign from "+ playername + " on the " + dateString).withStyle(style -> Style.EMPTY.withItalic(false));

        /*
    public boolean credits = true;
    public boolean date = true;
    public boolean name = true;
    public boolean boldName = true;
    public boolean boldDate = true;
    public String defultColor = "#AAAAAA";
    public String nameColor = "#FFAA00";
    public String dateColor = "#55FFFF";
         */







        item.set(DataComponents.LORE, new ItemLore(List.of(
                Component.literal(""),
                finalText,
                Component.literal(""),
                Meta)));
        return 1;
    }
    private static Component ApplyStyle(String text) {
        MutableComponent finaltext = Component.empty();
        TextColor currentColor = null;
        boolean withItalic = false;
        boolean withObfuscated = false;
        boolean withBold = false;
        boolean withStrikethrough = false;
        boolean withUnderlined = false;

        while (!text.isEmpty()) {

            //Color
            if (text.startsWith("#") && text.length() >= 7) {
                try {
                    currentColor = TextColor.parseColor(text.substring(0, 7)).getOrThrow();
                    text = text.substring(7);
                    continue;
                } catch (Exception e) {}
            }

            //Italic
            if (text.startsWith("<")) {
                int NextOpen = text.indexOf("<",1);
                int NextClose = text.indexOf(">");

                if (NextClose != -1 && (NextOpen == -1 || NextClose < NextOpen)) {
                    text = text.substring(1);
                    withItalic = true;
                    continue;
                }
            } else if (text.startsWith(">") && withItalic == true) {
                text = text.substring(1);
                withItalic = false;
                continue;
            }

            //Obfuscated
            if (text.startsWith("{")) {
                int NextOpen = text.indexOf("{",1);
                int NextClose = text.indexOf("}");

                if (NextClose != -1 && (NextOpen == -1 || NextClose < NextOpen)) {
                    text = text.substring(1);
                    withObfuscated = true;
                    continue;
                }
            } else if (text.startsWith("}") && withObfuscated == true) {
                text = text.substring(1);
                withObfuscated = false;
                continue;
            }

            //Bold
            if (text.startsWith("[")) {
                int NextOpen = text.indexOf("[",1);
                int NextClose = text.indexOf("]");

                if (NextClose != -1 && (NextOpen == -1 || NextClose < NextOpen)) {
                    text = text.substring(1);
                    withBold = true;
                    continue;
                }
            } else if (text.startsWith("]") && withBold == true) {
                text = text.substring(1);
                withBold = false;
                continue;
            }

            //Strikethrough
            if (text.startsWith("(")) {
                int NextOpen = text.indexOf("(",1);
                int NextClose = text.indexOf(")");

                if (NextClose != -1 && (NextOpen == -1 || NextClose < NextOpen)) {
                    text = text.substring(1);
                    withStrikethrough = true;
                    continue;
                }
            } else if (text.startsWith(")") && withStrikethrough == true) {
                text = text.substring(1);
                withStrikethrough = false;
                continue;
            }

            //Underlined
            if (text.startsWith("/")) {
                int NextOpen = text.indexOf("/",1);
                int NextClose = text.indexOf("|");

                if (NextClose != -1 && (NextOpen == -1 || NextClose < NextOpen)) {
                    text = text.substring(1);
                    withUnderlined = true;
                    continue;
                }
            } else if (text.startsWith("|") && withUnderlined == true) {
                text = text.substring(1);
                withUnderlined = false;
                continue;
            }

            //Apply Style
            if (text.isEmpty()) break;
            Style currentstyle = Style.EMPTY
                    .withItalic(withItalic)
                    .withObfuscated(withObfuscated)
                    .withBold(withBold)
                    .withStrikethrough(withStrikethrough)
                    .withUnderlined(withUnderlined)
                    .withColor(currentColor);
            finaltext.append(Component.literal(text.substring(0, 1)).withStyle(currentstyle));
            text = text.substring(1);
        }
        return finaltext;
    }
}