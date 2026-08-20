package widder.easyitemsign.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

public class command {

    public static void RegisterCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            dispatcher.register(Commands.literal("sign")
                    .then(Commands.argument("text", StringArgumentType.greedyString())
                            .executes(context -> {
                                return Sign.sign(context.getSource(), StringArgumentType.getString(context, "text"), false);
                            }))
                    .then(Commands.literal("add")
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                            .executes(context -> {
                                return Sign.sign(context.getSource(), StringArgumentType.getString(context, "text"), treu);
                            }))));
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            dispatcher.register(Commands.literal("unsign")
                    .executes(context -> {
                        return UnSign.unsign(context.getSource());
                    }));
        });
    }
}