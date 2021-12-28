package com.github.simulatan.meteornotificationsaddon.commands;

import com.github.simulatan.meteornotificationsaddon.notifications.Notification;
import com.github.simulatan.meteornotificationsaddon.notifications.NotificationsManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class NotificationsCommand extends Command {

    public NotificationsCommand() {
        super("notifications", "Sends a dummy notification");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("mode", new NotificationsArgumentType()).executes(context -> {
            NotificationCommandType arg = context.getArgument("mode", NotificationCommandType.class);
            if (arg == NotificationCommandType.SEND) {
                NotificationsManager.add(new Notification("hey, this is a test!", "very cool indeed yes", new Color((int) (Math.random() * 0x1000000))));
                ChatUtils.info("Notifications", "Successfully triggered notification!");
            } else if (arg == NotificationCommandType.CLEAR) {
                NotificationsManager.clearNotifications();
                ChatUtils.warning("Notifications", "Successfully cleared notifications!");
            }
            return SINGLE_SUCCESS;
        }));
    }

    private enum NotificationCommandType {
        SEND,
        CLEAR;
    }

    private static class NotificationsArgumentType implements ArgumentType<NotificationCommandType> {

        private static final DynamicCommandExceptionType NO_SUCH_MODULE = new DynamicCommandExceptionType(o -> new LiteralText("Type  " + o + " doesn't exist."));

        @Override
        public NotificationCommandType parse(StringReader reader) throws CommandSyntaxException {
            String argument = reader.readString();
            try {
                return NotificationCommandType.valueOf(argument.toUpperCase());
            } catch (Exception ignored){
                throw NO_SUCH_MODULE.create(argument);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(Arrays.stream(NotificationCommandType.values()).map(NotificationCommandType::name), builder);
        }

        private static final List<String> EXAMPLES = Arrays.stream(NotificationCommandType.values()).map(Enum::name).collect(Collectors.toList());

        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }
    }
}
