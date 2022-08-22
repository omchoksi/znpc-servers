package io.github.znetworkw.znpcservers.commands.list.inventory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.NPCAction;
import io.github.znetworkw.znpcservers.npc.conversation.Conversation;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationKey;
import io.github.znetworkw.znpcservers.user.EventService;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventory;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryPage;
import io.github.znetworkw.znpcservers.utility.itemstack.ItemStackBuilder;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ConversationGUI extends ZInventory {
    private static final String WHITESPACE = " ";
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");
    private static final Joiner SPACE_JOINER = Joiner.on(" ");

    public ConversationGUI(Player player) {
        super(player);
        this.setCurrentPage(new ConversationGUI.MainPage(this));
    }

    static class ActionManagementPage extends ZInventoryPage {
        private final Conversation conversation;
        private final ConversationKey conversationKey;

        public ActionManagementPage(ZInventory inventory, Conversation conversation, ConversationKey conversationKey) {
            super(inventory, "Editing " + conversationKey.getTextFormatted(), 5);
            this.conversation = conversation;
            this.conversationKey = conversationKey;
        }

        public void update() {
            for(int i = 0; i < this.conversationKey.getActions().size(); ++i) {
                NPCAction znpcAction = (NPCAction)this.conversationKey.getActions().get(i);
                this.addItem(ItemStackBuilder.forMaterial(Material.ANVIL).setName(ChatColor.AQUA + znpcAction.getAction().substring(0, Math.min(znpcAction.getAction().length(), 24)) + "....").setLore(new String[]{"&7this action type is &b" + znpcAction.getActionType(), "&f&lUSES", " &bRight-click &7to remove text."}).build(), i, (clickEvent) -> {
                    if (clickEvent.isRightClick()) {
                        this.conversationKey.getActions().remove(znpcAction);
                        Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                        this.openInventory();
                    }

                });
            }

            this.addItem(ItemStackBuilder.forMaterial(Material.EMERALD).setName(ChatColor.AQUA + "ADD A NEW ACTION").setLore(new String[]{"&7click here..."}).build(), this.getRows() - 5, (clickEvent) -> {
                Utils.sendTitle(this.getPlayer(), "&d&lADD ACTION", "&7Type the action...");
                EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                    if (ConfigurationConstants.NPC_CONVERSATIONS.contains(this.conversation) && this.conversation.getTexts().contains(this.conversationKey)) {
                        List<String> stringList = ConversationGUI.SPACE_SPLITTER.splitToList(event.getMessage());
                        if (stringList.size() < 2) {
                            Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INCORRECT_USAGE, new Object[0]);
                        } else {
                            this.conversationKey.getActions().add(new NPCAction(((String)stringList.get(0)).toUpperCase(), ConversationGUI.SPACE_JOINER.join(Iterables.skip(stringList, 1))));
                            Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                        }
                    } else {
                        Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                    }
                }).addConsumer((event) -> {
                    this.openInventory();
                });
            });
        }
    }

    static class EditConversationPage extends ZInventoryPage {
        private final Conversation conversation;

        public EditConversationPage(ZInventory inventory, Conversation conversation) {
            super(inventory, "Editing conversation " + conversation.getName(), 5);
            this.conversation = conversation;
        }

        public void update() {
            for(int i = 0; i < this.conversation.getTexts().size(); ++i) {
                ConversationKey conversationKey = (ConversationKey)this.conversation.getTexts().get(i);
                this.addItem(ItemStackBuilder.forMaterial(Material.NAME_TAG).setName(ChatColor.AQUA + conversationKey.getTextFormatted() + "....").setLore(new String[]{"&7this conversation text has a delay of &b" + conversationKey.getDelay() + "s &7to be executed,", "&7the sound for the text is &b" + (conversationKey.getSoundName() == null ? "NONE" : conversationKey.getSoundName()) + "&7,", "&7before sending the text there is a delay of &b" + conversationKey.getDelay() + "s", "&7the index for the text is &b" + i + "&7,", "&7and the conversation has currently &b" + conversationKey.getActions().size() + " actions&7.", "&f&lUSES", " &bLeft-click &7to change the position.", " &bRight-click &7to remove text.", " &bLeft-Shift-click &7to change the sound.", " &bMiddle-click &7to change the delay.", " &bRight-Shift-click &7to edit the text.", " &bQ &7to manage actions."}).build(), i, (clickEvent) -> {
                    if (clickEvent.getClick() == ClickType.SHIFT_LEFT) {
                        Utils.sendTitle(this.getPlayer(), "&c&lCHANGE SOUND", "&7Type the new sound...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (ConfigurationConstants.NPC_CONVERSATIONS.contains(this.conversation) && this.conversation.getTexts().contains(conversationKey)) {
                                String sound = event.getMessage().trim();
                                conversationKey.setSoundName(sound);
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                            } else {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.getClick() == ClickType.SHIFT_RIGHT) {
                        Utils.sendTitle(this.getPlayer(), "&a&lEDIT TEXT", "&7Type the new text...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (ConfigurationConstants.NPC_CONVERSATIONS.contains(this.conversation) && this.conversation.getTexts().contains(conversationKey)) {
                                conversationKey.getLines().clear();
                                conversationKey.getLines().addAll(ConversationGUI.SPACE_SPLITTER.splitToList(event.getMessage()));
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                            } else {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.isLeftClick()) {
                        Utils.sendTitle(this.getPlayer(), "&e&lCHANGE POSITION &a>=0&c<=" + this.conversation.getTexts().size(), "&7Type the new position...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (ConfigurationConstants.NPC_CONVERSATIONS.contains(this.conversation) && this.conversation.getTexts().contains(conversationKey)) {
                                Integer position = Ints.tryParse(event.getMessage());
                                if (position == null) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else if (position >= 0 && position <= this.conversation.getTexts().size() - 1) {
                                    Collections.swap(this.conversation.getTexts(), this.conversation.getTexts().indexOf(conversationKey), position);
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                                } else {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_SIZE, new Object[0]);
                                }
                            } else {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.isRightClick()) {
                        this.conversation.getTexts().remove(conversationKey);
                        Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                        this.openInventory();
                    } else if (clickEvent.getClick() == ClickType.MIDDLE) {
                        Utils.sendTitle(this.getPlayer(), "&d&lCHANGE DELAY", "&7Type the new delay...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (ConfigurationConstants.NPC_CONVERSATIONS.contains(this.conversation) && this.conversation.getTexts().contains(conversationKey)) {
                                Integer delay = Ints.tryParse(event.getMessage());
                                if (delay == null) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else if (delay < 0) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else {
                                    conversationKey.setDelay(delay);
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                                }
                            } else {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.getClick() == ClickType.DROP) {
                        (new ConversationGUI.ActionManagementPage(this.getInventory(), this.conversation, conversationKey)).openInventory();
                    }

                });
            }

        }
    }

    static class MainPage extends ZInventoryPage {
        public MainPage(ZInventory inventory) {
            super(inventory, "Conversations", 5);
        }

        public void update() {
            for(int i = 0; i < ConfigurationConstants.NPC_CONVERSATIONS.size(); ++i) {
                Conversation conversation = (Conversation)ConfigurationConstants.NPC_CONVERSATIONS.get(i);
                this.addItem(ItemStackBuilder.forMaterial(Material.PAPER).setName(ChatColor.GREEN + conversation.getName()).setLore(new String[]{"&7this conversation has &b" + conversation.getTexts().size() + " &7texts,", "&7it will activate when a player is on a &b" + conversation.getRadius() + "x" + conversation.getRadius() + " &7radius,", "&7or when a player interacts with an npc.", "&7when the conversation is finish, there is a &b" + conversation.getDelay() + "s &7delay to start again.", "&f&lUSES", " &bLeft-click &7to manage texts.", " &bRight-click &7to add a new text.", " &bQ &7to change the radius.", " &bMiddle-click &7to change the cooldown."}).build(), i, (clickEvent) -> {
                    if (clickEvent.getClick() == ClickType.DROP) {
                        Utils.sendTitle(this.getPlayer(), "&b&lCHANGE RADIUS", "&7Type the new radius...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (!ConfigurationConstants.NPC_CONVERSATIONS.contains(conversation)) {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            } else {
                                Integer radius = Ints.tryParse(event.getMessage());
                                if (radius == null) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else if (radius < 0) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else {
                                    conversation.setRadius(radius);
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                                }
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.isRightClick()) {
                        Utils.sendTitle(this.getPlayer(), "&e&lADD LINE", "&7Type the new line...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (!ConfigurationConstants.NPC_CONVERSATIONS.contains(conversation)) {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            } else {
                                conversation.getTexts().add(new ConversationKey(event.getMessage()));
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    } else if (clickEvent.isLeftClick()) {
                        (new ConversationGUI.EditConversationPage(this.getInventory(), conversation)).openInventory();
                    } else if (clickEvent.getClick() == ClickType.MIDDLE) {
                        Utils.sendTitle(this.getPlayer(), "&6&lCHANGE COOLDOWN", "&7Type the new cooldown...");
                        EventService.addService(ZUser.find(this.getPlayer()), AsyncPlayerChatEvent.class).addConsumer((event) -> {
                            if (!ConfigurationConstants.NPC_CONVERSATIONS.contains(conversation)) {
                                Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.NO_CONVERSATION_FOUND, new Object[0]);
                            } else {
                                Integer cooldown = Ints.tryParse(event.getMessage());
                                if (cooldown == null) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else if (cooldown < 0) {
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.INVALID_NUMBER, new Object[0]);
                                } else {
                                    conversation.setDelay(cooldown);
                                    Configuration.MESSAGES.sendMessage(this.getPlayer(), ConfigurationValue.SUCCESS, new Object[0]);
                                }
                            }
                        }).addConsumer((event) -> {
                            this.openInventory();
                        });
                    }

                });
            }

        }
    }
}
