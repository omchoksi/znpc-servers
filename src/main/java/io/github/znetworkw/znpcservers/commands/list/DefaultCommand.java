package io.github.znetworkw.znpcservers.commands.list;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.commands.Command;
import io.github.znetworkw.znpcservers.commands.CommandInformation;
import io.github.znetworkw.znpcservers.commands.CommandSender;
import io.github.znetworkw.znpcservers.commands.list.inventory.ConversationGUI;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.FunctionContext.ContextWithValue;
import io.github.znetworkw.znpcservers.npc.FunctionContext.DefaultContext;
import io.github.znetworkw.znpcservers.npc.*;
import io.github.znetworkw.znpcservers.npc.NPCPath.AbstractTypeWriter;
import io.github.znetworkw.znpcservers.npc.NPCPath.AbstractTypeWriter.TypeWriter;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultCommand extends Command {
    @SuppressWarnings("unused")
    private static final String WHITESPACE = " ";
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");
    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    private static final DefaultCommand.SkinFunction DO_APPLY_SKIN = (sender, npc, skin) -> NPCSkin.forName(skin, (skinValues, throwable) -> {
        if (throwable != null) {
            Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.CANT_GET_SKIN, skin);
        } else {
            npc.changeSkin(NPCSkin.forValues(skinValues));
            Configuration.MESSAGES.sendMessage(sender, ConfigurationValue.GET_SKIN);
        }
    });

    public DefaultCommand() {
        super("znpcs");
    }

    @CommandInformation(
            arguments = {},
            name = "",
            permission = ""
    )
    public void defaultCommand(CommandSender sender, Map<String, String> args) {
        sender.sendMessage("&6&m------------------------------------------");
        sender.sendMessage("&b&lZNPCS &8» &7ZNetwork");
        sender.sendMessage("&6https://www.spigotmc.org/resources/znpcs.80940");
        getCommands().forEach(sender::sendMessage);
        sender.sendMessage(ChatColor.DARK_GRAY + "Hover overs the command to see help for the command.");
        sender.sendMessage("&6&m------------------------------------------");
    }

    @CommandInformation(
            arguments = {"id", "type", "name"},
            name = "create",
            permission = "znpcs.cmd.create",
            help = {" &f&l* &e/znpcs create <npc_id> PLAYER Qentin"}
    )
    public void createNPC(CommandSender sender, Map<String, String> args) {
        if (args.size() < 3) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                boolean foundNPC = ConfigurationConstants.NPC_LIST.stream().anyMatch((npc) -> npc.getId() == id);
                if (foundNPC) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_FOUND);
                } else {
                    String name = args.get("name").trim();
                    if (name.length() >= 3 && name.length() <= 16) {
                        NPCType npcType = NPCType.valueOf(args.get("type").toUpperCase());
                        NPC npc = ServersNPC.createNPC(id, npcType, sender.getPlayer().getLocation(), name);
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                        if (npcType == NPCType.PLAYER) {
                            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.FETCHING_SKIN, name);
                            DO_APPLY_SKIN.apply(sender.getPlayer(), npc, name);
                        }

                    } else {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
                    }
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
            }
        }
    }

    @CommandInformation(
            arguments = {"id"},
            name = "delete",
            permission = "znpcs.cmd.delete",
            help = {" &f&l* &e/znpcs delete <npc_id>"}
    )
    public void deleteNPC(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    ServersNPC.deleteNPC(id);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {},
            name = "list",
            permission = "znpcs.cmd.list"
    )
    public void list(CommandSender sender, Map<String, String> args) {
        if (ConfigurationConstants.NPC_LIST.isEmpty()) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_NPC_FOUND);
        } else {

            for (NPCModel npcModel : ConfigurationConstants.NPC_LIST) {
                String message = "&f&l * &a" + npcModel.getId() + " " + npcModel.getHologramLines().toString() + " &7(&e" + npcModel.getLocation().getWorldName() + " " + npcModel.getLocation().getX() + " " + npcModel.getLocation().getY() + " " + npcModel.getLocation().getZ() + "&7)";
                TextComponent textComponent = new TextComponent(Utils.toColor(message));
                textComponent.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, (new Text(ChatColor.LIGHT_PURPLE + "Click to teleport to this npc!"))));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/znpcs teleport " + npcModel.getId()));
                sender.getPlayer().spigot().sendMessage(textComponent);
            }
        }

    }

    @CommandInformation(
            arguments = {"id", "skin"},
            name = "skin",
            permission = "znpcs.cmd.skin",
            help = {" &f&l* &e/znpcs skin <npc_id> Notch"}
    )
    public void setSkin(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    String skin = args.get("skin");
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.FETCHING_SKIN, skin);
                    DO_APPLY_SKIN.apply(sender.getPlayer(), foundNPC, args.get("skin"));
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id", "slot"},
            name = "equip",
            permission = "znpcs.cmd.equip",
            help = {" &f&l* &e/znpcs equip <npc_id> [HAND,OFFHAND,HELMET,CHESTPLATE,LEGGINGS,BOOTS]", "&8(You need to have the item in your hand.)"}
    )
    public void equip(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    foundNPC.getNpcPojo().getNpcEquip().put(ItemSlot.valueOf(args.get("slot").toUpperCase()), sender.getPlayer().getInventory().getItemInMainHand());
                    foundNPC.getPackets().flushCache("equipPackets");
                    foundNPC.getViewers().forEach(foundNPC::sendEquipPackets);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id", "lines"},
            name = "lines",
            permission = "znpcs.cmd.lines",
            help = {" &f&l* &e/znpcs lines <npc_id> First Second Third-Space"}
    )
    public void changeLines(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    foundNPC.getNpcPojo().setHologramLines(Lists.reverse((List<String>) SPACE_SPLITTER.split(args.get("lines"))));
                    foundNPC.getHologram().createHologram();
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id"},
            name = "move",
            permission = "znpcs.cmd.move",
            help = {" &f&l* &e/znpcs move <npc_id>"}
    )
    public void move(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    foundNPC.setLocation(sender.getPlayer().getLocation(), true);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id", "type"},
            name = "type",
            permission = "znpcs.cmd.type",
            help = {" &f&l* &e/znpcs type <npc_id> ZOMBIE"}
    )
    public void changeType(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            try {
                int id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    NPCType npcType = NPCType.valueOf(args.get("type").toUpperCase());
                    if (npcType != NPCType.PLAYER && npcType.getConstructor() == null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.UNSUPPORTED_ENTITY);
                    } else {
                        foundNPC.changeType(npcType);
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                    }
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"add", "remove", "cooldown", "list"},
            name = "action",
            isMultiple = true,
            permission = "znpcs.cmd.action",
            help = {" &f&l* &e/znpcs action add <npc_id> SERVER skywars", " &f&l* &e/znpcs action add <npc_id> CMD spawn", " &f&l* &e/znpcs action remove <npc_id> <action_id>", " &f&l* &e/znpcs action cooldown <npc_id> <action_id> <delay_in_seconds>", " &f&l* &e/znpcs action list <npc_id>"}
    )
    public void action(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            List<String> split;
            int id;
            NPC foundNPC;
            if (args.containsKey("add")) {
                split = (List<String>) SPACE_SPLITTER.split(args.get("add"));
                if (split.size() < 3) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.ACTION_ADD_INCORRECT_USAGE);
                    return;
                }

                try {
                    id = Integer.parseInt(args.get("id"));
                } catch (NumberFormatException e) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                    return;
                }

                foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                    return;
                }

                foundNPC.getNpcPojo().getClickActions().add(new NPCAction(split.get(1).toUpperCase(), SPACE_JOINER.join(Iterables.skip(split, 2))));
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            } else {
                int actionId;
                if (args.containsKey("remove")) {
                    split = (List<String>) SPACE_SPLITTER.split(args.get("remove"));
                    try {
                        id = Integer.parseInt(args.get("id"));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }

                    foundNPC = NPC.find(id);
                    if (foundNPC == null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                        return;
                    }

                    try {
                        actionId = Integer.parseInt(split.get(1));
                        if (actionId >= foundNPC.getNpcPojo().getClickActions().size()) {
                            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
                            return;
                        }

                        foundNPC.getNpcPojo().getClickActions().remove(actionId);
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                    }
                } else if (args.containsKey("cooldown")) {
                    split = (List<String>) SPACE_SPLITTER.split(args.get("cooldown"));
                    if (split.size() < 2) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.ACTION_DELAY_INCORRECT_USAGE);
                        return;
                    }

                    try {
                        id = Integer.parseInt(args.get("id"));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }

                    foundNPC = NPC.find(id);
                    if (foundNPC == null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                        return;
                    }

                    try {
                        actionId = Integer.parseInt(split.get(1));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }
                    int actionDelay;
                    try {
                        actionDelay = Integer.parseInt(split.get(2));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }
                    if (actionId >= foundNPC.getNpcPojo().getClickActions().size()) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
                        return;
                    }

                    foundNPC.getNpcPojo().getClickActions().get(actionId).setDelay(actionDelay);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                } else if (args.containsKey("list")) {
                    try {
                        id = Integer.parseInt(args.get("list"));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }

                    foundNPC = NPC.find(id);
                    if (foundNPC == null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                        return;
                    }

                    if (foundNPC.getNpcPojo().getClickActions().isEmpty()) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_ACTION_FOUND);
                    } else {
                        foundNPC.getNpcPojo().getClickActions().forEach((s) -> sender.sendMessage("&8(&a" + foundNPC.getNpcPojo().getClickActions().indexOf(s) + "&8) &6" + s.toString()));
                    }
                }
            }

        }
    }

    @CommandInformation(
            arguments = {"id", "type", "value"},
            name = "toggle",
            permission = "znpcs.cmd.toggle",
            help = {" &f&l* &e/znpcs toggle <npc_id> look"}
    )
    public void toggle(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            int id;
            try {
                id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    NPCFunction npcFunction = FunctionFactory.findFunctionForName(args.get("type"));
                    if (npcFunction.getName().equalsIgnoreCase("glow")) {
                        npcFunction.doRunFunction(foundNPC, new ContextWithValue(foundNPC, args.get("value")));
                    } else {
                        npcFunction.doRunFunction(foundNPC, new DefaultContext(foundNPC));
                    }

                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id", "customizeValues"},
            name = "customize",
            permission = "znpcs.cmd.customize",
            help = {" &f&l* &e/znpcs customize <npc_id> <customization>"}
    )
    public void customize(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            int id;
            try {
                id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    NPCType npcType = foundNPC.getNpcPojo().getNpcType();
                    List<String> customizeOptions = (List<String>) SPACE_SPLITTER.split(args.get("customizeValues"));
                    String methodName = customizeOptions.get(0);
                    if (npcType.getCustomizationLoader().contains(methodName)) {
                        Method method = npcType.getCustomizationLoader().getMethods().get(methodName);
                        Iterable<String> split = Iterables.skip(customizeOptions, 1);
                        if (Iterables.size(split) < method.getParameterTypes().length) {
                            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.TOO_FEW_ARGUMENTS);
                            return;
                        }

                        String[] values = Iterables.toArray(split, String.class);
                        npcType.updateCustomization(foundNPC, methodName, values);
                        foundNPC.getNpcPojo().getCustomizationMap().put(methodName, values);
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                    } else {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.METHOD_NOT_FOUND);

                        for (Entry<String, Method> stringMethodEntry : npcType.getCustomizationLoader().getMethods().entrySet()) {
                            sender.sendMessage(ChatColor.YELLOW + stringMethodEntry.getKey() + " " + SPACE_JOINER.join(stringMethodEntry.getValue().getParameterTypes()));
                        }
                    }

                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"set", "create", "exit", "path", "list"},
            name = "path",
            isMultiple = true,
            permission = "znpcs.cmd.path",
            help = {" &f&l* &e/znpcs path create name", " &f&l* &e/znpcs path set <npc_id> name"}
    )
    public void path(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            ZUser znpcUser = ZUser.find(sender.getPlayer());
            if (znpcUser != null) {
                if (args.containsKey("set")) {
                    List<String> split = (List<String>) SPACE_SPLITTER.split(args.get("set"));
                    if (split.size() < 2) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_SET_INCORRECT_USAGE);
                        return;
                    }

                    int id;
                    try {
                        id = Integer.parseInt(split.get(0));
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                        return;
                    }

                    NPC foundNPC = NPC.find(id);
                    if (foundNPC == null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                        return;
                    }

                    foundNPC.setPath(AbstractTypeWriter.find(split.get(1)));
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                } else if (args.containsKey("create")) {
                    String pathName = args.get("create");
                    if (pathName.length() < 3 || pathName.length() > 16) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
                        return;
                    }

                    if (AbstractTypeWriter.find(pathName) != null) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_FOUND);
                        return;
                    }

                    if (znpcUser.isHasPath()) {
                        sender.getPlayer().sendMessage(ChatColor.RED + "You already have a path creator active, to remove it use /znpcs path exit.");
                        return;
                    }

                    AbstractTypeWriter.forCreation(pathName, znpcUser, TypeWriter.MOVEMENT);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.PATH_START);
                } else if (args.containsKey("exit")) {
                    znpcUser.setHasPath(false);
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.EXIT_PATH);
                } else if (args.containsKey("list")) {
                    if (AbstractTypeWriter.getPaths().isEmpty()) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_PATH_FOUND);
                    } else {
                        AbstractTypeWriter.getPaths().forEach((path) -> sender.getPlayer().sendMessage(ChatColor.GREEN + path.getName()));
                    }
                }

            }
        }
    }

    @CommandInformation(
            arguments = {"id"},
            name = "teleport",
            permission = "znpcs.cmd.teleport",
            help = {" &f&l* &e/znpcs teleport <npc_id>"}
    )
    public void teleport(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            int id;
            try {
                id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    sender.getPlayer().teleport(foundNPC.getLocation());
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"id", "height"},
            name = "height",
            permission = "znpcs.cmd.height",
            help = {" &f&l* &e/znpcs height <npc_id> 2", "&8Add more height to the hologram of the npc"}
    )
    public void changeHologramHeight(CommandSender sender, Map<String, String> args) {
        if (args.size() < 2) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            int id;
            try {
                id = Integer.parseInt(args.get("id"));
                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                } else {
                    try {
                        double givenHeight = Double.parseDouble(args.get("height"));
                        foundNPC.getNpcPojo().setHologramHeight(givenHeight);
                        foundNPC.getHologram().createHologram();
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
                    } catch (NumberFormatException e) {
                        Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                    }
                }
            } catch (NumberFormatException e) {
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
            }
        }
    }

    @CommandInformation(
            arguments = {"create", "remove", "gui", "set"},
            name = "conversation",
            isMultiple = true,
            permission = "znpcs.cmd.conversation",
            help = {" &f&l* &e/znpcs conversation create first", " &f&l* &e/znpcs conversation remove first", " &f&l* &e/znpcs conversation set <npc_id> first [CLICK:RADIUS]", " &f&l* &e/znpcs conversation gui &8(&7Open a gui to manage the conversations&8)", "&8RADIUS: &7it is activated when the player is near the npc", "&8CLICK: &7it is activated when the player interacts with the npc"}
    )
    public void conversations(CommandSender sender, Map<String, String> args) {
        if (args.size() < 1) {
            Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INCORRECT_USAGE);
        } else {
            String conversationName;
            if (args.containsKey("create")) {
                conversationName = args.get("create");
                if (Conversation.exists(conversationName)) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.CONVERSATION_FOUND);
                    return;
                }

                if (conversationName.length() < 3 || conversationName.length() > 16) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NAME_LENGTH);
                    return;
                }

                ConfigurationConstants.NPC_CONVERSATIONS.add(new Conversation(conversationName));
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            } else if (args.containsKey("remove")) {
                conversationName = args.get("remove");
                if (!Conversation.exists(conversationName)) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NO_CONVERSATION_FOUND);
                    return;
                }

                ConfigurationConstants.NPC_CONVERSATIONS.remove(Conversation.forName(conversationName));
                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            } else if (args.containsKey("gui")) {
                sender.getPlayer().openInventory((new ConversationGUI(sender.getPlayer())).build());
            } else if (args.containsKey("set")) {
                List<String> split = (List<String>) SPACE_SPLITTER.split(args.get("set"));
                if (split.size() < 2) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.CONVERSATION_SET_INCORRECT_USAGE);
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(split.get(0));
                } catch (NumberFormatException e) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.INVALID_NUMBER);
                    return;
                }

                NPC foundNPC = NPC.find(id);
                if (foundNPC == null) {
                    Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.NPC_NOT_FOUND);
                    return;
                }

                conversationName = split.get(1);
                if (Conversation.exists(conversationName)) {
//                    foundNPC.getNpcPojo().setConversation(new ConversationModel(conversationName, split.size() > 1 ? split.get(2) : "CLICK"));
                    foundNPC.getNpcPojo().setConversation(new ConversationModel(conversationName, split.get(2)));
                } else {
                    foundNPC.getNpcPojo().setConversation(null);
                }

                Configuration.MESSAGES.sendMessage(sender.getCommandSender(), ConfigurationValue.SUCCESS);
            }

        }
    }

    interface SkinFunction {
        void apply(Player var1, NPC var2, String var3);
    }
}
