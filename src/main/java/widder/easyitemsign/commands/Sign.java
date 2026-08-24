package widder.easyitemsign.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import widder.easyitemsign.EasyItemSign;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static widder.easyitemsign.load.*;

public class Sign {
    public static int sign(CommandSourceStack source, String text, boolean add) {

        if (!CanSign(source, add)) {
            return 0;
        }

        ItemStack signItem = source.getPlayer().getMainHandItem();
        Component signText = ApplyStyle(text);

        //Build Lore
        List<Component> loreList = buildLoreList(source, signText);

        //Add/Create Lore on Item
        applyLore(signItem, loreList, add);

        //Add Unsign Protection
        applyUnsignProtection(source, signItem);

        return 1;
    }

    private static void applyUnsignProtection(CommandSourceStack source, ItemStack signItem) {
        CompoundTag tag = new CompoundTag();
        tag.putString("easyitemsign_owner", source.getPlayer().getName().getString());
        signItem.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static void applyLore(ItemStack signItem, List<Component> loreList, boolean add) {
        if (add) {
            ItemLore existingLore = signItem.get(DataComponents.LORE);
            if (existingLore != null && existingLore.lines().size() >= 2) {
                Component combined = Component.empty()
                        .append(existingLore.lines().get(1))
                        .append(loreList.get(1));
                loreList.set(1, combined);
                signItem.set(DataComponents.LORE, new ItemLore(loreList));
            }
        } else {
            signItem.set(DataComponents.LORE, new ItemLore(loreList));
        }
    }

    private static List<Component> buildLoreList(CommandSourceStack source, Component signText) {
        List<Component> loreList = new ArrayList<>();
        loreList.add(Component.literal(""));
        loreList.add(signText);

        if (signature) {
            loreList.add(Component.literal(""));
            loreList.add(SignatureCreate(source));
        }

        return loreList;
    }

    private static Component SignatureCreate(CommandSourceStack source) {
        //Create MutableComponent
        MutableComponent returnText = Component.empty();
        returnText.append(Component.literal("Signed").withStyle(style ->
                Style.EMPTY.withColor(TextColor.parseColor(defaultColor).getOrThrow())));

        //Add Name
        if (name) {
            returnText.append(Component.literal(" from ").withStyle(style ->
                    Style.EMPTY.withColor(TextColor.parseColor(defaultColor).getOrThrow())));
            returnText.append(Component.literal(
                    source.getPlayer().getPlainTextName()).withStyle(style ->
                        Style.EMPTY.withColor(
                                TextColor.parseColor(nameColor).getOrThrow())
                                .withBold(boldName)));
        }

        //Add Date
        if (date) {
            String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            returnText.append(Component.literal(" on the ").withStyle(style ->
                    Style.EMPTY.withColor(TextColor.parseColor(defaultColor).getOrThrow())));
            returnText.append(Component.literal(
                    dateString).withStyle(style ->
                        Style.EMPTY.withColor(
                            TextColor.parseColor(dateColor).getOrThrow())
                            .withBold(boldDate)));
        }

        return returnText;
    }

    private static boolean CanSign(CommandSourceStack source, boolean add) {
        //Player run the Command
        if (!(source.getEntity() instanceof ServerPlayer)) {
            source.sendFailure(Component.literal("You need to be a player to use this command!"));
            return false;
        }

        //Player hold am Item
        if (source.getPlayer().getMainHandItem().isEmpty()) {
            source.sendFailure(Component.literal("You need to hold an item!"));
            return false;
        }

        //Item isn't Sign
        if (source.getPlayer().getMainHandItem().has(DataComponents.CUSTOM_DATA)) {
            CompoundTag savedTag = source.getPlayer().getMainHandItem().get(DataComponents.CUSTOM_DATA).copyTag();
            if (savedTag.contains("easyitemsign_owner")) {
                String name = savedTag.getStringOr("easyitemsign_owner","");
                if (name.equals(source.getPlayer().getName().getString())) {
                    if (add) {
                        return true;
                    } else {
                        source.sendFailure(Component.literal("The item is already singed"));
                        return false;
                    }
                }else {
                    source.sendFailure(Component.literal("The item is already singed"));
                    return false;
                }
            }
        }

        return true;
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