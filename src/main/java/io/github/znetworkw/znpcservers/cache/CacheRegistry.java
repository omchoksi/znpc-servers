package io.github.znetworkw.znpcservers.cache;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache;
import io.github.znetworkw.znpcservers.cache.TypeCache.CacheBuilder;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache.ClazzLoader;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache.ConstructorLoader;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache.FieldLoader;
import io.github.znetworkw.znpcservers.cache.TypeCache.BaseCache.MethodLoader;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.netty.channel.Channel;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

public final class CacheRegistry {
    public static final Class<?> PACKET_PLAY_IN_USE_ENTITY_CLASS;
    public static final Class<?> ENUM_PLAYER_INFO_CLASS;
    public static final Class<?> PACKET_CLASS;
    public static final Class<?> ENTITY_CLASS;
    public static final Class<?> ENTITY_LIVING;
    public static final Class<?> ENTITY_PLAYER_CLASS;
    public static final Class<?> ENTITY_ARMOR_STAND_CLASS;
    public static final Class<?> ENTITY_BAT_CLASS;
    public static final Class<?> ENTITY_BLAZE_CLASS;
    public static final Class<?> ENTITY_CAVE_SPIDER_CLASS;
    public static final Class<?> ENTITY_CHICKEN_CLASS;
    public static final Class<?> ENTITY_COW_CLASS;
    public static final Class<?> ENTITY_CREEPER_CLASS;
    public static final Class<?> ENTITY_ENDER_DRAGON_CLASS;
    public static final Class<?> ENTITY_ENDERMAN_CLASS;
    public static final Class<?> ENTITY_HUMAN_CLASS;
    public static final Class<?> ENTITY_ENDERMITE_CLASS;
    public static final Class<?> ENTITY_GHAST_CLASS;
    public static final Class<?> ENTITY_IRON_GOLEM_CLASS;
    public static final Class<?> ENTITY_GIANT_ZOMBIE_CLASS;
    public static final Class<?> ENTITY_GUARDIAN_CLASS;
    public static final Class<?> ENTITY_HORSE_CLASS;
    public static final Class<?> ENTITY_LLAMA_CLASS;
    public static final Class<?> ENTITY_MAGMA_CUBE_CLASS;
    public static final Class<?> ENTITY_MUSHROOM_COW_CLASS;
    public static final Class<?> ENTITY_OCELOT_CLASS;
    public static final Class<?> ENTITY_PARROT_CLASS;
    public static final Class<?> ENTITY_PIG_CLASS;
    public static final Class<?> ENTITY_RABBIT_CLASS;
    public static final Class<?> ENTITY_POLAR_BEAR_CLASS;
    public static final Class<?> ENTITY_PANDA_CLASS;
    public static final Class<?> ENTITY_SHEEP_CLASS;
    public static final Class<?> ENTITY_SNOWMAN_CLASS;
    public static final Class<?> ENTITY_SHULKER_CLASS;
    public static final Class<?> ENTITY_SILVERFISH_CLASS;
    public static final Class<?> ENTITY_SKELETON_CLASS;
    public static final Class<?> ENTITY_SLIME_CLASS;
    public static final Class<?> ENTITY_SPIDER_CLASS;
    public static final Class<?> ENTITY_SQUID_CLASS;
    public static final Class<?> ENTITY_VILLAGER_CLASS;
    public static final Class<?> ENTITY_WITCH_CLASS;
    public static final Class<?> ENTITY_WITHER_CLASS;
    public static final Class<?> ENTITY_ZOMBIE_CLASS;
    public static final Class<?> ENTITY_WOLF_CLASS;
    public static final Class<?> ENTITY_AXOLOTL_CLASS;
    public static final Class<?> ENTITY_GOAT_CLASS;
    public static final Class<?> ENTITY_FOX_CLASS;
    public static final Class<?> ENTITY_TYPES_CLASS;
    public static final Class<?> ENUM_CHAT_CLASS;
    public static final Class<?> ENUM_ITEM_SLOT;
    public static final Class<?> I_CHAT_BASE_COMPONENT;
    public static final Class<?> ITEM_STACK_CLASS;
    public static final Class<?> DATA_WATCHER_CLASS;
    public static final Class<?> DATA_WATCHER_OBJECT;
    public static final Class<?> DATA_WATCHER_REGISTRY;
    public static final Class<?> DATA_WATCHER_SERIALIZER;
    public static final Class<?> WORLD_CLASS;
    public static final Class<?> CRAFT_ITEM_STACK_CLASS;
    public static final Class<?> WORLD_SERVER_CLASS;
    public static final Class<?> MINECRAFT_SERVER_CLASS;
    public static final Class<?> PLAYER_INTERACT_MANAGER_CLASS;
    public static final Class<?> PLAYER_CONNECTION_CLASS;
    public static final Class<?> NETWORK_MANAGER_CLASS;
    public static final Class<?> PACKET_PLAY_OUT_PLAYER_INFO_CLASS;
    public static final Class<?> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS;
    public static final Class<?> PACKET_PLAY_OUT_ENTITY_DESTROY_CLASS;
    public static final Class<?> SCOREBOARD_CLASS;
    public static final Class<?> SCOREBOARD_TEAM_CLASS;
    public static final Class<?> ENUM_TAG_VISIBILITY;
    public static final Class<?> CRAFT_CHAT_MESSAGE_CLASS;
    public static final Class<?> PROFILE_PUBLIC_KEY_CLASS;
    public static final BaseCache<Constructor<?>> SCOREBOARD_TEAM_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_OLD;
    public static final BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_NEW;
    public static final BaseCache<Constructor<?>> PLAYER_CONSTRUCTOR_NEW_1;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_NAMED_ENTITY_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PLAYER_INTERACT_MANAGER_OLD_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PLAYER_INTERACT_MANAGER_NEW_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_OLD;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD;
    public static final BaseCache<Constructor<?>> PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_V1;
    public static final BaseCache<Constructor<?>> I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> ENTITY_CONSTRUCTOR;
    public static final BaseCache<Constructor<?>> DATA_WATCHER_OBJECT_CONSTRUCTOR;
    public static final BaseCache<Method> AS_NMS_COPY_METHOD;
    public static final BaseCache<Method> GET_PROFILE_METHOD;
    public static final BaseCache<Method> GET_ENTITY_ID;
    public static final BaseCache<Method> GET_HANDLE_PLAYER_METHOD;
    public static final BaseCache<Method> GET_HANDLE_WORLD_METHOD;
    public static final BaseCache<Method> GET_SERVER_METHOD;
    public static final BaseCache<Method> SEND_PACKET_METHOD;
    public static final BaseCache<Method> SET_CUSTOM_NAME_OLD_METHOD;
    public static final BaseCache<Method> SET_CUSTOM_NAME_NEW_METHOD;
    public static final BaseCache<Method> SET_CUSTOM_NAME_VISIBLE_METHOD;
    public static final BaseCache<Method> SET_INVISIBLE_METHOD;
    public static final BaseCache<Method> SET_LOCATION_METHOD;
    public static final BaseCache<Method> SET_DATA_WATCHER_METHOD;
    public static final BaseCache<Method> WATCH_DATA_WATCHER_METHOD;
    public static final BaseCache<Method> GET_DATA_WATCHER_METHOD;
    public static final BaseCache<Method> GET_BUKKIT_ENTITY_METHOD;
    public static final BaseCache<Method> GET_ENUM_CHAT_ID_METHOD;
    public static final BaseCache<Method> ENUM_CHAT_TO_STRING_METHOD;
    public static final BaseCache<Method> ENTITY_TYPES_A_METHOD;
    public static final BaseCache<Method> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1;
    public static final BaseCache<Method> PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE;
    public static final BaseCache<Method> SCOREBOARD_PLAYER_LIST;
    public static final BaseCache<Method> ENUM_CHAT_FORMAT_FIND;
    public static final BaseCache<Method> CRAFT_CHAT_MESSAGE_METHOD;
    public static final BaseCache<Method> GET_UNIQUE_ID_METHOD;
    public static final BaseCache<Field> PLAYER_CONNECTION_FIELD;
    public static final BaseCache<Field> NETWORK_MANAGER_FIELD;
    public static final BaseCache<Field> CHANNEL_FIELD;
    public static final BaseCache<Field> PACKET_IN_USE_ENTITY_ID_FIELD;
    public static final BaseCache<Field> BUKKIT_COMMAND_MAP;
    public static final BaseCache<Object> ADD_PLAYER_FIELD;
    public static final BaseCache<Object> REMOVE_PLAYER_FIELD;
    public static final BaseCache<Object> DATA_WATCHER_REGISTER_FIELD;
    public static final BaseCache<Object> ENUM_TAG_VISIBILITY_NEVER_FIELD;

    private CacheRegistry() {
    }

    static {
        PACKET_PLAY_IN_USE_ENTITY_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayInUseEntity"))).load();
        ENUM_PLAYER_INFO_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"))).load();
        PACKET_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PROTOCOL).withClassName("Packet"))).load();
        ENTITY_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName("Entity"))).load();
        ENTITY_LIVING = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName("EntityLiving"))).load();
        ENTITY_PLAYER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_LEVEL).withClassName("EntityPlayer"))).load();
        ENTITY_ARMOR_STAND_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("decoration").withClassName("EntityArmorStand"))).load();
        ENTITY_BAT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("ambient").withClassName("EntityBat"))).load();
        ENTITY_BLAZE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityBlaze"))).load();
        ENTITY_CAVE_SPIDER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityCaveSpider"))).load();
        ENTITY_CHICKEN_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityChicken"))).load();
        ENTITY_COW_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityCow"))).load();
        ENTITY_CREEPER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityCreeper"))).load();
        ENTITY_ENDER_DRAGON_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("boss.enderdragon").withClassName("EntityEnderDragon"))).load();
        ENTITY_ENDERMAN_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityEnderman"))).load();
        ENTITY_HUMAN_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("player").withClassName("EntityHuman"))).load();
        ENTITY_ENDERMITE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityEndermite"))).load();
        ENTITY_GHAST_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityGhast"))).load();
        ENTITY_IRON_GOLEM_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityIronGolem"))).load();
        ENTITY_GIANT_ZOMBIE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityGiantZombie"))).load();
        ENTITY_GUARDIAN_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityGuardian"))).load();
        ENTITY_HORSE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal.horse").withClassName("EntityHorse"))).load();
        ENTITY_LLAMA_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal.horse").withClassName("EntityLlama"))).load();
        ENTITY_MAGMA_CUBE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityMagmaCube"))).load();
        ENTITY_MUSHROOM_COW_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityMushroomCow"))).load();
        ENTITY_OCELOT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityOcelot"))).load();
        ENTITY_PARROT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityParrot"))).load();
        ENTITY_PIG_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityPig"))).load();
        ENTITY_RABBIT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityRabbit"))).load();
        ENTITY_POLAR_BEAR_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityPolarBear"))).load();
        ENTITY_PANDA_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityPanda"))).load();
        ENTITY_SHEEP_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntitySheep"))).load();
        ENTITY_SNOWMAN_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntitySnowman"))).load();
        ENTITY_SHULKER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityShulker"))).load();
        ENTITY_SILVERFISH_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntitySilverfish"))).load();
        ENTITY_SKELETON_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntitySkeleton"))).load();
        ENTITY_SLIME_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntitySlime"))).load();
        ENTITY_SPIDER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntitySpider"))).load();
        ENTITY_SQUID_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntitySquid"))).load();
        ENTITY_VILLAGER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("npc").withClassName("EntityVillager"))).load();
        ENTITY_WITCH_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityWitch"))).load();
        ENTITY_WITHER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("boss.wither").withClassName("EntityWither"))).load();
        ENTITY_ZOMBIE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("monster").withClassName("EntityZombie"))).load();
        ENTITY_WOLF_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityWolf"))).load();
        ENTITY_AXOLOTL_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal.axolotl").withClassName("Axolotl"))).load();
        ENTITY_GOAT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal.goat").withClassName("Goat"))).load();
        ENTITY_FOX_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withAdditionalData("animal").withClassName("EntityFox"))).load();
        ENTITY_TYPES_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName("EntityTypes"))).load();
        ENUM_CHAT_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withClassName("EnumChatFormat"))).load();
        ENUM_ITEM_SLOT = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName("EnumItemSlot"))).load();
        I_CHAT_BASE_COMPONENT = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.CHAT).withClassName("IChatBaseComponent"))).load();
        ITEM_STACK_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ITEM).withClassName("ItemStack"))).load();
        DATA_WATCHER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SYNCHER).withClassName("DataWatcher"))).load();
        DATA_WATCHER_OBJECT = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SYNCHER).withClassName("DataWatcherObject"))).load();
        DATA_WATCHER_REGISTRY = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SYNCHER).withClassName("DataWatcherRegistry"))).load();
        DATA_WATCHER_SERIALIZER = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SYNCHER).withClassName("DataWatcherSerializer"))).load();
        WORLD_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.WORLD_LEVEL).withClassName("World"))).load();
        CRAFT_ITEM_STACK_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("inventory.CraftItemStack"))).load();
        WORLD_SERVER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_LEVEL).withClassName("WorldServer"))).load();
        MINECRAFT_SERVER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER).withClassName("MinecraftServer"))).load();
        PLAYER_INTERACT_MANAGER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_LEVEL).withClassName("PlayerInteractManager"))).load();
        PLAYER_CONNECTION_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_NETWORK).withClassName("PlayerConnection"))).load();
        NETWORK_MANAGER_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.NETWORK).withClassName("NetworkManager"))).load();
        PACKET_PLAY_OUT_PLAYER_INFO_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutPlayerInfo"))).load();
        PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutScoreboardTeam"))).load();
        PACKET_PLAY_OUT_ENTITY_DESTROY_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityDestroy"))).load();
        SCOREBOARD_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.WORLD_SCORES).withClassName("Scoreboard"))).load();
        SCOREBOARD_TEAM_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.WORLD_SCORES).withClassName("ScoreboardTeam"))).load();
        ENUM_TAG_VISIBILITY = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.WORLD_SCORES).withClassName("ScoreboardTeamBase$EnumNameTagVisibility"))).load();
        CRAFT_CHAT_MESSAGE_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("util.CraftChatMessage"))).load();
        PROFILE_PUBLIC_KEY_CLASS = (Class)(new ClazzLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.WORLD_ENTITY_PLAYER).withClassName("ProfilePublicKey"))).load();
        SCOREBOARD_TEAM_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(SCOREBOARD_TEAM_CLASS).withParameterTypes(new Class[]{SCOREBOARD_CLASS, String.class}));
        PLAYER_CONSTRUCTOR_OLD = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_PLAYER_CLASS).withParameterTypes(new Class[]{MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class, PLAYER_INTERACT_MANAGER_CLASS}));
        PLAYER_CONSTRUCTOR_NEW = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_PLAYER_CLASS).withParameterTypes(new Class[]{MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class}));
        PLAYER_CONSTRUCTOR_NEW_1 = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_PLAYER_CLASS).withParameterTypes(new Class[]{MINECRAFT_SERVER_CLASS, WORLD_SERVER_CLASS, GameProfile.class, PROFILE_PUBLIC_KEY_CLASS}));
        PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PACKET_PLAY_OUT_PLAYER_INFO_CLASS).withParameterTypes(new Class[]{ENUM_PLAYER_INFO_CLASS, Utils.BUKKIT_VERSION > 16 ? Collection.class : Iterable.class}));
        PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntity$PacketPlayOutEntityLook").withParameterTypes(new Class[]{Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE}));
        PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityHeadRotation").withParameterTypes(new Class[]{ENTITY_CLASS, Byte.TYPE}));
        PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityTeleport").withParameterTypes(new Class[]{ENTITY_CLASS}));
        PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityMetadata").withParameterTypes(new Class[]{Integer.TYPE, DATA_WATCHER_CLASS, Boolean.TYPE}));
        PACKET_PLAY_OUT_NAMED_ENTITY_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutNamedEntitySpawn").withParameterTypes(new Class[]{ENTITY_HUMAN_CLASS}));
        PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PACKET_PLAY_OUT_ENTITY_DESTROY_CLASS).withParameterTypes(new Class[]{Integer.TYPE}).withParameterTypes(new Class[]{int[].class}));
        PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutSpawnEntity").withClassName("PacketPlayOutSpawnEntityLiving").withParameterTypes(new Class[]{ENTITY_LIVING}));
        PLAYER_INTERACT_MANAGER_OLD_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PlayerInteractManager").withParameterTypes(new Class[]{WORLD_CLASS}));
        PLAYER_INTERACT_MANAGER_NEW_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PlayerInteractManager").withParameterTypes(new Class[]{WORLD_SERVER_CLASS}));
        PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS));
        PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_OLD = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityEquipment").withParameterTypes(new Class[]{Integer.TYPE, Integer.TYPE, ITEM_STACK_CLASS}));
        PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityEquipment").withParameterTypes(new Class[]{Integer.TYPE, ENUM_ITEM_SLOT, ITEM_STACK_CLASS}));
        PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_V1 = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutEntityEquipment").withParameterTypes(new Class[]{Integer.TYPE, List.class}));
        I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.CHAT).withClassName("ChatComponentText").withParameterTypes(new Class[]{String.class}));
        ENTITY_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_ARMOR_STAND_CLASS).withParameterTypes(new Class[]{WORLD_CLASS, Double.TYPE, Double.TYPE, Double.TYPE}));
        DATA_WATCHER_OBJECT_CONSTRUCTOR = new ConstructorLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(DATA_WATCHER_OBJECT).withParameterTypes(new Class[]{Integer.TYPE, DATA_WATCHER_SERIALIZER}));
        AS_NMS_COPY_METHOD = new MethodLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("inventory.CraftItemStack").withMethodName("asNMSCopy").withParameterTypes(new Class[]{ItemStack.class}));
        GET_PROFILE_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName(ENTITY_HUMAN_CLASS).withExpectResult(GameProfile.class));
        GET_ENTITY_ID = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("getId").withMethodName("ae").withExpectResult(Integer.TYPE));
        GET_HANDLE_PLAYER_METHOD = new MethodLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("entity.CraftPlayer").withClassName("entity.CraftHumanEntity").withMethodName("getHandle"));
        GET_HANDLE_WORLD_METHOD = new MethodLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("CraftWorld").withMethodName("getHandle"));
        GET_SERVER_METHOD = new MethodLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("CraftServer").withMethodName("getServer"));
        SEND_PACKET_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PLAYER_CONNECTION_CLASS).withMethodName("sendPacket").withMethodName("a").withParameterTypes(new Class[]{PACKET_CLASS}));
        SET_CUSTOM_NAME_OLD_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("setCustomName").withParameterTypes(new Class[]{String.class}));
        SET_CUSTOM_NAME_NEW_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("setCustomName").withMethodName("a").withMethodName("b").withParameterTypes(new Class[]{I_CHAT_BASE_COMPONENT}).withExpectResult(Void.TYPE));
        SET_CUSTOM_NAME_VISIBLE_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("setCustomNameVisible").withMethodName("n").withParameterTypes(new Class[]{Boolean.TYPE}));
        SET_INVISIBLE_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_ARMOR_STAND_CLASS).withMethodName("setInvisible").withMethodName("j").withParameterTypes(new Class[]{Boolean.TYPE}));
        SET_LOCATION_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("setPositionRotation").withMethodName("b").withParameterTypes(new Class[]{Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE}));
        SET_DATA_WATCHER_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(DATA_WATCHER_CLASS).withMethodName("set").withMethodName("b").withParameterTypes(new Class[]{DATA_WATCHER_OBJECT, Object.class}));
        WATCH_DATA_WATCHER_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(DATA_WATCHER_CLASS).withMethodName("watch").withParameterTypes(new Class[]{Integer.TYPE, Object.class}));
        GET_DATA_WATCHER_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("getDataWatcher")); //.withMethodName("ai").withExpectResult(DATA_WATCHER_CLASS)
        GET_BUKKIT_ENTITY_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENTITY_CLASS).withMethodName("getBukkitEntity"));
        GET_ENUM_CHAT_ID_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENUM_CHAT_CLASS).withMethodName("b"));
        ENUM_CHAT_TO_STRING_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENUM_CHAT_CLASS).withMethodName("toString"));
        ENTITY_TYPES_A_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.ENTITY).withClassName(ENTITY_TYPES_CLASS).withMethodName("a").withParameterTypes(new Class[]{String.class}));
        PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1 = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS).withMethodName("a").withParameterTypes(new Class[]{SCOREBOARD_TEAM_CLASS}));
        PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PACKET_PLAY_OUT_SCOREBOARD_TEAM_CLASS).withMethodName("a").withParameterTypes(new Class[]{SCOREBOARD_TEAM_CLASS, Boolean.TYPE}));
        SCOREBOARD_PLAYER_LIST = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(SCOREBOARD_TEAM_CLASS).withMethodName("getPlayerNameSet").withMethodName("g"));
        ENUM_CHAT_FORMAT_FIND = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withClassName(ENUM_CHAT_CLASS).withMethodName("b").withParameterTypes(new Class[]{String.class}));
        CRAFT_CHAT_MESSAGE_METHOD = new MethodLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName(CRAFT_CHAT_MESSAGE_CLASS).withMethodName("fromStringOrNull").withParameterTypes(new Class[]{String.class}));
        GET_UNIQUE_ID_METHOD = new MethodLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withClassName(ENTITY_CLASS).withExpectResult(UUID.class));
        PLAYER_CONNECTION_FIELD = new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_LEVEL).withClassName(ENTITY_PLAYER_CLASS).withFieldName(Utils.BUKKIT_VERSION > 16 ? "b" : "playerConnection"));
        NETWORK_MANAGER_FIELD = new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(PLAYER_CONNECTION_CLASS).withFieldName(Utils.BUKKIT_VERSION > 16 ? "a" : "networkManager").withExpectResult(NETWORK_MANAGER_CLASS));
        CHANNEL_FIELD = new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.SERVER_NETWORK).withClassName(NETWORK_MANAGER_CLASS).withExpectResult(Channel.class));
        PACKET_IN_USE_ENTITY_ID_FIELD = new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayInUseEntity").withFieldName("a"));
        BUKKIT_COMMAND_MAP = new FieldLoader((new CacheBuilder(CachePackage.CRAFT_BUKKIT)).withClassName("CraftServer").withFieldName("commandMap"));
        ADD_PLAYER_FIELD = (new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction").withFieldName(Utils.BUKKIT_VERSION > 16 ? "a" : "ADD_PLAYER"))).asValueField();
        REMOVE_PLAYER_FIELD = (new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName("PacketPlayOutPlayerInfo$EnumPlayerInfoAction").withFieldName(Utils.BUKKIT_VERSION > 16 ? "e" : "REMOVE_PLAYER"))).asValueField();
        DATA_WATCHER_REGISTER_FIELD = (new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(DATA_WATCHER_REGISTRY).withFieldName("a"))).asValueField();
        ENUM_TAG_VISIBILITY_NEVER_FIELD = (new FieldLoader((new CacheBuilder(CachePackage.MINECRAFT_SERVER)).withCategory(CacheCategory.PACKET).withClassName(ENUM_TAG_VISIBILITY).withFieldName("b"))).asValueField();
    }
}
