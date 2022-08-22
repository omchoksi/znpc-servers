package io.github.znetworkw.znpcservers.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.utility.Utils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandSender {
    static final Joiner LINE_SEPARATOR_JOINER = Joiner.on("\n");
    private static final ImmutableList<String> HELP_PREFIX = ImmutableList.of("&6&lEXAMPLES&r:");
    private final org.bukkit.command.CommandSender commandSender;
    private final SenderType type;

    public CommandSender(org.bukkit.command.CommandSender commandSender) {
        this.commandSender = commandSender;
        this.type = commandSender instanceof Player ? SenderType.PLAYER : SenderType.CONSOLE;
    }

    public void sendMessage(String message) {
        this.sendMessage(message, (Iterable)null);
    }

    public void sendMessage(CommandInformation subCommand) {
        this.sendMessage(" &7» &6/&eznpcs " + subCommand.name() + " " + (String)Arrays.stream(subCommand.arguments()).map((s) -> {
            return "<" + s + ">";
        }).collect(Collectors.joining(" ")), Arrays.asList(subCommand.help()));
    }

    public void sendMessage(String message, Iterable<String> hover) {
        TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(Utils.toColor(message)));
        if (hover != null) {
            textComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, (new Text(Utils.toColor(LINE_SEPARATOR_JOINER.join(Iterables.concat(HELP_PREFIX, hover)))))));
        }

        this.getPlayer().spigot().sendMessage(textComponent);
    }

    public Player getPlayer() {
        if (this.type != SenderType.PLAYER) {
            throw new IllegalStateException("sender is not a player.");
        } else {
            return (Player)this.getCommandSender();
        }
    }

    public org.bukkit.command.CommandSender getCommandSender() {
        return this.commandSender;
    }

    static enum SenderType {
        PLAYER,
        CONSOLE;

        private SenderType() {
        }
    }
}
