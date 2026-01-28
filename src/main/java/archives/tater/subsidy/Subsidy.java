package archives.tater.subsidy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
public class Subsidy implements ModInitializer {
	public static final String MOD_ID = "subsidy";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AttachmentType<Unit> CREATIVE_INVENTORY = AttachmentRegistry.create(id("creative_inventory"), builder -> builder
            .persistent(Unit.CODEC)
            .syncWith(Unit.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .copyOnDeath());

    public static boolean hasCreativeInventory(Player player) {
        return player.hasAttached(CREATIVE_INVENTORY) && !player.isSpectator();
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("creative_inv")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                    .then(literal("enable")
                            .executes(command -> {
                                command.getSource().getPlayerOrException().setAttached(CREATIVE_INVENTORY, Unit.INSTANCE);
                                command.getSource().sendSuccess(() -> Component.translatable("commands.subsidy.enable.self"), true);
                                return 1;
                            })
                            .then(argument("target", EntityArgument.player())
                                    .executes(command -> {
                                        var target = EntityArgument.getPlayer(command, "target");
                                        target.setAttached(CREATIVE_INVENTORY, Unit.INSTANCE);
                                        command.getSource().sendSuccess(() -> Component.translatable("commands.subsidy.enable", target.getDisplayName()), true);
                                        return 1;
                                    })
                            )
                    )
                    .then(literal("disable")
                            .executes(command -> {
                                command.getSource().getPlayerOrException().removeAttached(CREATIVE_INVENTORY);
                                command.getSource().sendSuccess(() -> Component.translatable("commands.subsidy.disable.self"), true);
                                return 1;
                            })
                            .then(argument("target", EntityArgument.player())
                                    .executes(command -> {
                                        var target = EntityArgument.getPlayer(command, "target");
                                        target.removeAttached(CREATIVE_INVENTORY);
                                        command.getSource().sendSuccess(() -> Component.translatable("commands.subsidy.disable", target.getDisplayName()), true);
                                        return 1;
                                    })
                            )
                    )
            );
        });
	}
}
