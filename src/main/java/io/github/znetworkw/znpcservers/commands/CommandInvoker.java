package io.github.znetworkw.znpcservers.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandInvoker {
    private final Command command;
    private final Method commandMethod;
    private final String permission;

    public CommandInvoker(Command command, Method commandMethod, String permission) {
        this.command = command;
        this.commandMethod = commandMethod;
        this.permission = permission;
    }

    public void execute(CommandSender sender, Object command) throws CommandPermissionException, CommandExecuteException {
        if (this.permission.length() > 0 && !sender.getCommandSender().hasPermission(this.permission)) {
            throw new CommandPermissionException("Insufficient permission.");
        } else {
            try {
                this.commandMethod.invoke(this.command, sender, command);
            } catch (InvocationTargetException | IllegalAccessException var4) {
                throw new CommandExecuteException(var4.getMessage(), var4.getCause());
            }
        }
    }
}
