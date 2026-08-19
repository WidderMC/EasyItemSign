package widder.easyitemsign.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class UnSign {

    public static int unsign(CommandSourceStack source) {
        if (source.getPlayer().getMainHandItem().has(DataComponents.CUSTOM_DATA)) {
            CompoundTag savedTag = source.getPlayer().getMainHandItem().get(DataComponents.CUSTOM_DATA).copyTag();
            if (savedTag.contains("easyitemsign_owner")) {
                String name = savedTag.getStringOr("easyitemsign_owner","");
                if (name.equals(source.getPlayer().getName().getString())) {
                    source.sendSuccess(() -> Component.literal("Signature successful removed"), false);
                    source.getPlayer().getMainHandItem().remove(DataComponents.CUSTOM_DATA);
                }else {
                    source.sendFailure(Component.literal("The item is signed by "+name));
                    source.sendFailure(Component.literal("You cant unsign"));
                }
            }
        }
        return 1;
    }
}