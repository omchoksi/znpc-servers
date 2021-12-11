package io.github.znetworkw.znpcservers.entity;

import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.entity.internal.DefaultPluginEntityTypeData;
import org.bukkit.entity.EntityType;

public enum PluginEntityTypes implements PluginEntityType {
    ARMOR_STAND(EntityType.ARMOR_STAND, CacheRegistry.ENTITY_ARMOR_STAND_CLASS),
    AXOLOTL(EntityType.AXOLOTL, CacheRegistry.ENTITY_AXOLOTL_CLASS),
    BAT(EntityType.BAT, CacheRegistry.ENTITY_BAT_CLASS),
    BEE(EntityType.BEE, CacheRegistry.ENTITY_BEE_CLASS),
    BLAZE(EntityType.BLAZE, CacheRegistry.ENTITY_BLAZE_CLASS),
    CAVE_SPIDER(EntityType.CAVE_SPIDER, CacheRegistry.ENTITY_CAVE_SPIDER_CLASS),
    CHICKEN(EntityType.CHICKEN, CacheRegistry.ENTITY_CHICKEN_CLASS),
    COD(EntityType.COD, CacheRegistry.ENTITY_COD_CLASS),
    COW(EntityType.COW, CacheRegistry.ENTITY_COW_CLASS),
    CREEPER(EntityType.CREEPER, CacheRegistry.ENTITY_CREEPER_CLASS),
    DOLPHIN(EntityType.DOLPHIN, CacheRegistry.ENTITY_DOLPHIN_CLASS),
    DONKEY(EntityType.DONKEY, CacheRegistry.ENTITY_DONKEY_CLASS),
    DROWNED(EntityType.DROWNED, CacheRegistry.ENTITY_DROWNED_CLASS),
    ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, CacheRegistry.ENTITY_ELDER_GUARDIAN_CLASS),
    ENDER_DRAGON(EntityType.ENDER_DRAGON, CacheRegistry.ENTITY_ENDER_DRAGON_CLASS),
    ENDERMAN(EntityType.ENDERMAN, CacheRegistry.ENTITY_ENDERMAN_CLASS),
    ENDERMITE(EntityType.ENDERMITE, CacheRegistry.ENTITY_ENDERMITE_CLASS),
    EVOKER(EntityType.EVOKER, CacheRegistry.ENTITY_EVOKER_CLASS),
    FOX(EntityType.FOX, CacheRegistry.ENTITY_FOX_CLASS),
    GHAST(EntityType.GHAST, CacheRegistry.ENTITY_GHAST_CLASS),
    GIANT(EntityType.GIANT, CacheRegistry.ENTITY_GIANT_ZOMBIE_CLASS),
    GLOW_SQUID(EntityType.GLOW_SQUID, CacheRegistry.ENTITY_GLOW_SQUID_CLASS),
    GOAT(EntityType.GOAT, CacheRegistry.ENTITY_GOAT_CLASS),
    GUARDIAN(EntityType.GUARDIAN, CacheRegistry.ENTITY_GUARDIAN_CLASS),
    HOGLIN(EntityType.HOGLIN, CacheRegistry.ENTITY_HOGLIN_CLASS),
    HORSE(EntityType.HORSE, CacheRegistry.ENTITY_HORSE_CLASS),
    HUSK(EntityType.HUSK, CacheRegistry.ENTITY_HUSK_CLASS),
    ILLUSIONER(EntityType.ILLUSIONER, CacheRegistry.ENTITY_ILLUSIONER_CLASS),
    IRON_GOLEM(EntityType.IRON_GOLEM, CacheRegistry.ENTITY_IRON_GOLEM_CLASS),
    LLAMA(EntityType.LLAMA, CacheRegistry.ENTITY_LLAMA_CLASS),
    MAGMA_CUBE(EntityType.MAGMA_CUBE, CacheRegistry.ENTITY_MAGMA_CUBE_CLASS),
    MULE(EntityType.MULE, CacheRegistry.ENTITY_MULE_CLASS),
    MUSHROOM_COW(EntityType.MUSHROOM_COW, CacheRegistry.ENTITY_MUSHROOM_COW_CLASS),
    OCELOT(EntityType.OCELOT, CacheRegistry.ENTITY_OCELOT_CLASS),
    PANDA(EntityType.PANDA, CacheRegistry.ENTITY_PANDA_CLASS),
    PARROT(EntityType.PARROT, CacheRegistry.ENTITY_PARROT_CLASS),
    PHANTOM(EntityType.PHANTOM, CacheRegistry.ENTITY_PHANTOM_CLASS),
    PIG(EntityType.PIG, CacheRegistry.ENTITY_PIG_CLASS),
    PIGLIN(EntityType.PIGLIN, CacheRegistry.ENTITY_PIGLIN_CLASS),
    PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, CacheRegistry.ENTITY_PIGLIN_BRUTE_CLASS),
    PILLAGER(EntityType.PILLAGER, CacheRegistry.ENTITY_PILLAGER_CLASS),
    POLAR_BEAR(EntityType.POLAR_BEAR, CacheRegistry.ENTITY_POLAR_BEAR_CLASS),
    PLAYER(EntityType.PLAYER, CacheRegistry.ENTITY_PLAYER_CLASS, CacheRegistry.PLAYER_CONSTRUCTOR_OLD.getParameterTypes()),
    PUFFERFISH(EntityType.PUFFERFISH, CacheRegistry.ENTITY_PUFFERFISH_CLASS),
    RABBIT(EntityType.RABBIT, CacheRegistry.ENTITY_RABBIT_CLASS),
    SALMON(EntityType.SALMON, CacheRegistry.ENTITY_SALMON_CLASS),
    SHEEP(EntityType.SHEEP, CacheRegistry.ENTITY_SHEEP_CLASS),
    SHULKER(EntityType.SHULKER, CacheRegistry.ENTITY_SHULKER_CLASS),
    SILVERFISH(EntityType.SILVERFISH, CacheRegistry.ENTITY_SILVERFISH_CLASS),
    SKELETON(EntityType.SKELETON, CacheRegistry.ENTITY_SKELETON_CLASS),
    SLIME(EntityType.SLIME, CacheRegistry.ENTITY_SLIME_CLASS),
    SNOWMAN(EntityType.SNOWMAN, CacheRegistry.ENTITY_SNOWMAN_CLASS),
    SPIDER(EntityType.SPIDER, CacheRegistry.ENTITY_SPIDER_CLASS),
    SQUID(EntityType.SQUID, CacheRegistry.ENTITY_SQUID_CLASS),
    STRAY(EntityType.STRAY, CacheRegistry.ENTITY_STRAY_CLASS),
    STRIDER(EntityType.STRIDER, CacheRegistry.ENTITY_STRIDER_CLASS),
    TROPICAL_FISH(EntityType.TROPICAL_FISH, CacheRegistry.ENTITY_TROPICAL_FISH_CLASS),
    TURTLE(EntityType.TURTLE, CacheRegistry.ENTITY_TURTLE_CLASS),
    VEX(EntityType.VEX, CacheRegistry.ENTITY_VEX_CLASS),
    VILLAGER(EntityType.VILLAGER, CacheRegistry.ENTITY_VILLAGER_CLASS),
    VINDICATOR(EntityType.VINDICATOR, CacheRegistry.ENTITY_VINDICATOR_CLASS),
    WANDERING_TRADER(EntityType.WANDERING_TRADER, CacheRegistry.ENTITY_WANDERING_TRADER_CLASS),
    WITCH(EntityType.WITCH, CacheRegistry.ENTITY_WITCH_CLASS),
    WITHER(EntityType.WITHER, CacheRegistry.ENTITY_WITHER_CLASS),
    WITHER_SKELETON(EntityType.WITHER_SKELETON, CacheRegistry.ENTITY_WITHER_SKELETON_CLASS),
    WOLF(EntityType.WOLF, CacheRegistry.ENTITY_WOLF_CLASS),
    ZOGLIN(EntityType.ZOGLIN, CacheRegistry.ENTITY_ZOGLIN_CLASS),
    ZOMBIE(EntityType.ZOMBIE, CacheRegistry.ENTITY_ZOMBIE_CLASS),
    ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, CacheRegistry.ENTITY_ZOMBIE_VILLAGER_CLASS),
    ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, CacheRegistry.ENTITY_ZOMBIE_HORSE_CLASS),
    ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, CacheRegistry.ENTITY_ZOMBIFIED_PIGLIN_CLASS);

    private final EntityType entityType;
    private final Class<?> entityClass;
    private final Class<?>[] expectedConstructorTypes;

    PluginEntityTypes(
        EntityType entityType, Class<?> entityClass) {
        this(entityType, entityClass, new Class[]{CacheRegistry.WORLD_CLASS});
    }

    PluginEntityTypes(
        EntityType entityType, Class<?> entityClass,
        Class<?>[] expectedConstructorTypes) {
        this.entityType = entityType;
        this.entityClass = entityClass;
        this.expectedConstructorTypes = expectedConstructorTypes;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public PluginEntityTypeData getData() {
        return new DefaultPluginEntityTypeData(this, entityClass, expectedConstructorTypes);
    }

    @Override
    public EntityType getBukkitEntityType() {
        return entityType;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
