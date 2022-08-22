package io.github.znetworkw.znpcservers.commands;

import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class Command extends BukkitCommand {
    private static final String WHITESPACE = " ";
    private static final CommandMap COMMAND_MAP;
    private final Map<CommandInformation, CommandInvoker> subCommands = new HashMap();

    public Command(String name) {
        super(name);
        this.load();
    }

    private void load() {
        COMMAND_MAP.register(this.getName(), this);
        Method[] var1 = this.getClass().getMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            if (method.isAnnotationPresent(CommandInformation.class)) {
                CommandInformation cmdInfo = (CommandInformation)method.getAnnotation(CommandInformation.class);
                this.subCommands.put(cmdInfo, new CommandInvoker(this, method, cmdInfo.permission()));
            }
        }

    }

    private Map<String, String> loadArgs(CommandInformation subCommand, Iterable<String> args) {
        int size = Iterables.size(args);
        int subCommandsSize = subCommand.arguments().length;
        Map<String, String> argsMap = new HashMap();
        if (size > 1) {
            if (subCommand.isMultiple()) {
                argsMap.put((String)Iterables.get(args, 1), String.join(" ", Iterables.skip(args, 2)));
            } else {
                for(int i = 0; i < Math.min(subCommandsSize, size); ++i) {
                    int fixedLength = i + 1;
                    if (size > fixedLength) {
                        String input = (String)Iterables.get(args, fixedLength);
                        if (fixedLength == subCommandsSize) {
                            input = String.join(" ", Iterables.skip(args, subCommandsSize));
                        }

                        argsMap.put(subCommand.arguments()[i], input);
                    }
                }
            }
        }

        return argsMap;
    }

    public Set<CommandInformation> getCommands() {
        return this.subCommands.keySet();
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        Optional<Entry<CommandInformation, CommandInvoker>> subCommandOptional = this.subCommands.entrySet().stream().filter((command) -> {
            return ((CommandInformation)command.getKey()).name().contentEquals(args.length > 0 ? args[0] : "");
        }).findFirst();
        if (!subCommandOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "can't find command: " + commandLabel + ".");
            return false;
        } else {
            try {
                Entry<CommandInformation, CommandInvoker> subCommand = (Entry)subCommandOptional.get();
                ((CommandInvoker)subCommand.getValue()).execute(new io.github.znetworkw.znpcservers.commands.CommandSender(sender), this.loadArgs((CommandInformation)subCommand.getKey(), Arrays.asList(args)));
            } catch (CommandExecuteException var6) {
                sender.sendMessage(ChatColor.RED + "can't execute command.");
                var6.printStackTrace();
            } catch (CommandPermissionException var7) {
                sender.sendMessage(ChatColor.RED + "no permission for run this command.");
            }

            return true;
        }
    }

    static {
        try {
            COMMAND_MAP = (CommandMap)((Field)CacheRegistry.BUKKIT_COMMAND_MAP.load()).get(Bukkit.getServer());
        } catch (IllegalAccessException var1) {
            throw new IllegalStateException("can't access bukkit command map.");
        }
    }
}
