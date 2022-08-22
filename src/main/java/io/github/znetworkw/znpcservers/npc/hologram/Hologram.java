package io.github.znetworkw.znpcservers.npc.hologram;

import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.hologram.replacer.LineReplacer;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.Location;

public class Hologram {
    private static final String WHITESPACE = " ";
    private static final boolean NEW_METHOD;
    private static final double LINE_SPACING;
    private final List<Hologram.HologramLine> hologramLines = new ArrayList();
    private final NPC npc;

    public Hologram(NPC npc) {
        this.npc = npc;
    }

    public void createHologram() {
        this.npc.getViewers().forEach(this::delete);

        try {
            this.hologramLines.clear();
            double y = 0.0D;
            Location location = this.npc.getLocation();

            for(Iterator var4 = this.npc.getNpcPojo().getHologramLines().iterator(); var4.hasNext(); y += LINE_SPACING) {
                String line = (String)var4.next();
                boolean visible = !line.equalsIgnoreCase("%space%");
                Object armorStand = ((Constructor)CacheRegistry.ENTITY_CONSTRUCTOR.load()).newInstance(((Method)CacheRegistry.GET_HANDLE_WORLD_METHOD.load()).invoke(location.getWorld()), location.getX(), location.getY() - 0.15D + y, location.getZ());
                if (visible) {
                    ((Method)CacheRegistry.SET_CUSTOM_NAME_VISIBLE_METHOD.load()).invoke(armorStand, true);
                    this.updateLine(line, armorStand, (ZUser)null);
                }

                ((Method)CacheRegistry.SET_INVISIBLE_METHOD.load()).invoke(armorStand, true);
                this.hologramLines.add(new Hologram.HologramLine(line.replace(ConfigurationConstants.SPACE_SYMBOL, " "), armorStand, (Integer)((Method)CacheRegistry.GET_ENTITY_ID.load()).invoke(armorStand)));
            }

            this.setLocation(location, 0.0D);
            this.npc.getPackets().flushCache(new String[]{"getHologramSpawnPacket"});
            this.npc.getViewers().forEach(this::spawn);
        } catch (ReflectiveOperationException var8) {
            throw new UnexpectedCallException(var8);
        }
    }

    public void spawn(ZUser user) {
        this.hologramLines.forEach((hologramLine) -> {
            try {
                Object entityPlayerPacketSpawn = this.npc.getPackets().getProxyInstance().getHologramSpawnPacket(hologramLine.armorStand);
                Utils.sendPackets(user, new Object[]{entityPlayerPacketSpawn});
            } catch (ReflectiveOperationException var4) {
                this.delete(user);
            }

        });
    }

    public void delete(ZUser user) {
        this.hologramLines.forEach((hologramLine) -> {
            try {
                Utils.sendPackets(user, new Object[]{this.npc.getPackets().getProxyInstance().getDestroyPacket(hologramLine.id)});
            } catch (ReflectiveOperationException var4) {
                throw new UnexpectedCallException(var4);
            }
        });
    }

    public void updateNames(ZUser user) {
        Iterator var2 = this.hologramLines.iterator();

        while(var2.hasNext()) {
            Hologram.HologramLine hologramLine = (Hologram.HologramLine)var2.next();

            try {
                this.updateLine(hologramLine.line, hologramLine.armorStand, user);
                Utils.sendPackets(user, new Object[]{((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR.load()).newInstance(hologramLine.id, ((Method)CacheRegistry.GET_DATA_WATCHER_METHOD.load()).invoke(hologramLine.armorStand), true)});
            } catch (ReflectiveOperationException var5) {
                throw new UnexpectedCallException(var5);
            }
        }

    }

    public void updateLocation() {
        this.hologramLines.forEach((hologramLine) -> {
            try {
                Object packet = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR.load()).newInstance(hologramLine.armorStand);
                this.npc.getViewers().forEach((player) -> {
                    Utils.sendPackets(player, new Object[]{packet});
                });
            } catch (ReflectiveOperationException var3) {
                throw new UnexpectedCallException(var3);
            }
        });
    }

    public void setLocation(Location location, double height) {
        location = location.clone().add(0.0D, height, 0.0D);

        try {
            double y = this.npc.getNpcPojo().getHologramHeight();

            for(Iterator var6 = this.hologramLines.iterator(); var6.hasNext(); y += LINE_SPACING) {
                Hologram.HologramLine hologramLine = (Hologram.HologramLine)var6.next();
                ((Method)CacheRegistry.SET_LOCATION_METHOD.load()).invoke(hologramLine.armorStand, location.getX(), location.getY() - 0.15D + y, location.getZ(), location.getYaw(), location.getPitch());
            }

            this.updateLocation();
        } catch (ReflectiveOperationException var8) {
            throw new UnexpectedCallException(var8);
        }
    }

    private void updateLine(String line, Object armorStand, @Nullable ZUser user) throws InvocationTargetException, IllegalAccessException {
        if (NEW_METHOD) {
            ((Method)CacheRegistry.SET_CUSTOM_NAME_NEW_METHOD.load()).invoke(armorStand, ((Method)CacheRegistry.CRAFT_CHAT_MESSAGE_METHOD.load()).invoke((Object)null, LineReplacer.makeAll(user, line)));
        } else {
            ((Method)CacheRegistry.SET_CUSTOM_NAME_OLD_METHOD.load()).invoke(armorStand, LineReplacer.makeAll(user, line));
        }

    }

    static {
        NEW_METHOD = Utils.BUKKIT_VERSION > 12;
        LINE_SPACING = (Double)Configuration.CONFIGURATION.getValue(ConfigurationValue.LINE_SPACING);
    }

    private static class HologramLine {
        private final String line;
        private final Object armorStand;
        private final int id;

        protected HologramLine(String line, Object armorStand, int id) {
            this.line = line;
            this.armorStand = armorStand;
            this.id = id;
        }
    }
}
