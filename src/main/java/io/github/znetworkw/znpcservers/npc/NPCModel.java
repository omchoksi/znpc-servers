package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.utils.location.ZLocation;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class NPCModel {
    private static final String EMPTY_STRING = "";
    private int id;
    private double hologramHeight;
    private String skin;
    private String signature;
    private String pathName;
    private String glowName;
    private ConversationModel conversation;
    private ZLocation location;
    private NPCType npcType;
    private List<String> hologramLines;
    private List<NPCAction> clickActions;
    private Map<ItemSlot, ItemStack> npcEquip;
    private Map<String, Boolean> npcFunctions;
    private Map<String, String[]> customizationMap;

    public NPCModel(int id) {
        this();
        this.id = id;
        this.skin = "";
        this.signature = "";
        this.npcType = NPCType.PLAYER;
    }

    private NPCModel() {
        this.signature = "";
        this.hologramLines = Collections.singletonList("/znpcs lines");
        this.clickActions = new ArrayList();
        this.npcEquip = new HashMap();
        this.customizationMap = new HashMap();
        this.npcFunctions = new HashMap();
        this.npcFunctions.put("holo", true);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NPCModel withId(int id) {
        this.setId(id);
        return this;
    }

    public double getHologramHeight() {
        return this.hologramHeight;
    }

    public void setHologramHeight(double hologramHeight) {
        this.hologramHeight = hologramHeight;
    }

    public NPCModel withHologramHeight(double hologramHeight) {
        this.setHologramHeight(hologramHeight);
        return this;
    }

    public String getSkin() {
        return this.skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public NPCModel withSkin(String skin) {
        this.setSkin(skin);
        return this;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public NPCModel withSignature(String signature) {
        this.setSignature(signature);
        return this;
    }

    public String getPathName() {
        return this.pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public NPCModel withPathName(String pathName) {
        this.setPathName(pathName);
        return this;
    }

    public String getGlowName() {
        return this.glowName;
    }

    public void setGlowName(String glowName) {
        this.glowName = glowName;
    }

    public NPCModel withGlowName(String glowName) {
        this.setGlowName(this.pathName);
        return this;
    }

    public ConversationModel getConversation() {
        return this.conversation;
    }

    public void setConversation(ConversationModel conversation) {
        this.conversation = conversation;
    }

    public NPCModel withConversation(ConversationModel conversation) {
        this.setConversation(conversation);
        return this;
    }

    public List<String> getHologramLines() {
        return this.hologramLines;
    }

    public void setHologramLines(List<String> hologramLines) {
        this.hologramLines = hologramLines;
    }

    public NPCModel withHologramLines(List<String> hologramLines) {
        this.setHologramLines(hologramLines);
        return this;
    }

    public ZLocation getLocation() {
        return this.location;
    }

    public void setLocation(ZLocation location) {
        this.location = location;
    }

    public NPCModel withLocation(ZLocation location) {
        this.setLocation(location);
        return this;
    }

    public NPCType getNpcType() {
        return this.npcType;
    }

    public void setNpcType(NPCType npcType) {
        this.npcType = npcType;
    }

    public NPCModel withNpcType(NPCType npcType) {
        this.setNpcType(npcType);
        return this;
    }

    public List<NPCAction> getClickActions() {
        return this.clickActions;
    }

    public void setClickActions(List<NPCAction> clickActions) {
        this.clickActions = clickActions;
    }

    public NPCModel withClickActions(List<NPCAction> clickActions) {
        this.setClickActions(clickActions);
        return this;
    }

    public Map<ItemSlot, ItemStack> getNpcEquip() {
        return this.npcEquip;
    }

    public void setNpcEquip(Map<ItemSlot, ItemStack> npcEquip) {
        this.npcEquip = npcEquip;
    }

    public NPCModel withNpcEquip(Map<ItemSlot, ItemStack> npcEquip) {
        this.setNpcEquip(npcEquip);
        return this;
    }

    public Map<String, String[]> getCustomizationMap() {
        return this.customizationMap;
    }

    public void setCustomizationMap(Map<String, String[]> customizationMap) {
        this.customizationMap = customizationMap;
    }

    public NPCModel withCustomizationMap(Map<String, String[]> customizationMap) {
        this.setCustomizationMap(customizationMap);
        return this;
    }

    public Map<String, Boolean> getFunctions() {
        return this.npcFunctions;
    }

    public void setFunctions(Map<String, Boolean> npcFunctions) {
        this.npcFunctions = npcFunctions;
    }

    public NPCModel withFunctionValues(Map<String, Boolean> npcFunctions) {
        this.setFunctions(npcFunctions);
        return this;
    }
}
