package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface NPCPath {
    void initialize(DataInputStream var1) throws IOException;

    void write(DataOutputStream var1) throws IOException;

    void start();

    PathInitializer getPath(NPC var1);

    public abstract static class AbstractTypeWriter implements NPCPath {
        private static final Logger LOGGER = Logger.getLogger(AbstractTypeWriter.class.getName());
        private static final ConcurrentMap<String, AbstractTypeWriter> PATH_TYPES = new ConcurrentHashMap();
        private static final int PATH_DELAY = 1;
        private final TypeWriter typeWriter;
        private final File file;
        private final List<ZLocation> locationList;

        public AbstractTypeWriter(TypeWriter typeWriter, File file) {
            this.typeWriter = typeWriter;
            this.file = file;
            this.locationList = new ArrayList();
        }

        public AbstractTypeWriter(TypeWriter typeWriter, String pathName) {
            this(typeWriter, new File(ServersNPC.PATH_FOLDER, pathName + ".path"));
        }

        public void load() {
            try {
                DataInputStream reader = ZNPCPathDelegator.forFile(this.file).getInputStream();

                try {
                    this.initialize(reader);
                    register(this);
                } catch (Throwable var5) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }

                    throw var5;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (IOException var6) {
                LOGGER.log(Level.WARNING, String.format("The path %s could not be loaded", this.file.getName()));
            }

        }

        public void write() {
            try {
                DataOutputStream writer = ZNPCPathDelegator.forFile(this.getFile()).getOutputStream();

                try {
                    this.write(writer);
                } catch (Throwable var5) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }

                    throw var5;
                }

                if (writer != null) {
                    writer.close();
                }
            } catch (IOException var6) {
                LOGGER.log(Level.WARNING, String.format("Path %s could not be created", this.getName()), var6);
            }

        }

        public static AbstractTypeWriter forCreation(String pathName, ZUser user, TypeWriter typeWriter) {
            if (typeWriter == TypeWriter.MOVEMENT) {
                return new TypeMovement(pathName, user);
            } else {
                throw new IllegalStateException("can't find type writer for: " + typeWriter.name());
            }
        }

        public static AbstractTypeWriter forFile(File file, TypeWriter typeWriter) {
            if (typeWriter == TypeWriter.MOVEMENT) {
                return new TypeMovement(file);
            } else {
                throw new IllegalStateException("can't find type writer for: " + typeWriter.name());
            }
        }

        public File getFile() {
            return this.file;
        }

        public List<ZLocation> getLocationList() {
            return this.locationList;
        }

        public String getName() {
            return this.file.getName().substring(0, this.file.getName().lastIndexOf(46));
        }

        public static void register(AbstractTypeWriter abstractZNPCPath) {
            PATH_TYPES.put(abstractZNPCPath.getName(), abstractZNPCPath);
        }

        public static AbstractTypeWriter find(String name) {
            return (AbstractTypeWriter)PATH_TYPES.get(name);
        }

        public static Collection<AbstractTypeWriter> getPaths() {
            return PATH_TYPES.values();
        }

        private static class TypeMovement extends AbstractTypeWriter {
            private static final int MAX_LOCATIONS;
            private ZUser npcUser;
            private BukkitTask bukkitTask;

            public TypeMovement(File file) {
                super(TypeWriter.MOVEMENT, file);
            }

            public TypeMovement(String fileName, ZUser npcUser) {
                super(TypeWriter.MOVEMENT, fileName);
                this.npcUser = npcUser;
                this.start();
            }

            public void initialize(DataInputStream dataInputStream) throws IOException {
                while(dataInputStream.available() > 0) {
                    String worldName = dataInputStream.readUTF();
                    double x = dataInputStream.readDouble();
                    double y = dataInputStream.readDouble();
                    double z = dataInputStream.readDouble();
                    float yaw = dataInputStream.readFloat();
                    float pitch = dataInputStream.readFloat();
                    this.getLocationList().add(new ZLocation(worldName, x, y, z, yaw, pitch));
                }

            }

            public void write(DataOutputStream dataOutputStream) throws IOException {
                if (!this.getLocationList().isEmpty()) {
                    Iterator locationIterator = this.getLocationList().iterator();

                    while(locationIterator.hasNext()) {
                        ZLocation location = (ZLocation)locationIterator.next();
                        dataOutputStream.writeUTF(location.getWorldName());
                        dataOutputStream.writeDouble(location.getX());
                        dataOutputStream.writeDouble(location.getY());
                        dataOutputStream.writeDouble(location.getZ());
                        dataOutputStream.writeFloat(location.getYaw());
                        dataOutputStream.writeFloat(location.getPitch());
                        if (!locationIterator.hasNext()) {
                            register(this);
                        }
                    }

                }
            }

            public void start() {
                this.npcUser.setHasPath(true);
                this.bukkitTask = ServersNPC.SCHEDULER.runTaskTimerAsynchronously(() -> {
                    if (this.npcUser.toPlayer() != null && this.npcUser.isHasPath() && MAX_LOCATIONS > this.getLocationList().size()) {
                        Location location = this.npcUser.toPlayer().getLocation();
                        if (this.isValid(location)) {
                            this.getLocationList().add(new ZLocation(location));
                        }
                    } else {
                        this.bukkitTask.cancel();
                        this.npcUser.setHasPath(false);
                        this.write();
                    }

                }, 1, 1);
            }

            public MovementPath getPath(NPC npc) {
                return new MovementPath(npc, this);
            }

            protected boolean isValid(Location location) {
                if (this.getLocationList().isEmpty()) {
                    return true;
                } else {
                    ZLocation last = (ZLocation)this.getLocationList().get(this.getLocationList().size() - 1);
                    double xDiff = Math.abs(last.getX() - location.getX());
                    double yDiff = Math.abs(last.getY() - location.getY());
                    double zDiff = Math.abs(last.getZ() - location.getZ());
                    return xDiff + yDiff + zDiff > 0.01D;
                }
            }

            static {
                MAX_LOCATIONS = (Integer)Configuration.CONFIGURATION.getValue(ConfigurationValue.MAX_PATH_LOCATIONS);
            }

            protected static class MovementPath extends PathInitializer.AbstractPath {
                private int currentEntryPath = 0;
                private boolean pathReverse = false;

                public MovementPath(NPC npc, TypeMovement path) {
                    super(npc, path);
                }

                public void handle() {
                    this.updatePathLocation((ZLocation)this.getPath().getLocationList().get(this.currentEntryPath = this.getNextLocation()));
                    int nextIndex = this.getNextLocation();
                    if (nextIndex < 1) {
                        this.pathReverse = false;
                    } else if (nextIndex >= this.getPath().getLocationList().size() - 1) {
                        this.pathReverse = true;
                    }

                }

                private int getNextLocation() {
                    return this.pathReverse ? this.currentEntryPath - 1 : this.currentEntryPath + 1;
                }

                protected void updatePathLocation(ZLocation location) {
                    this.setLocation(location);
                    ZLocation next = (ZLocation)this.getPath().getLocationList().get(this.getNextLocation());
                    Vector vector = next.toVector().add(new Vector(0.0D, location.getY() - next.getY(), 0.0D));
                    Location direction = next.bukkitLocation().clone().setDirection(location.toVector().subtract(vector).multiply(new Vector(-1, 0, -1)));
                    this.getNpc().setLocation(direction, false);
                    this.getNpc().lookAt((ZUser)null, direction, true);
                }
            }
        }

        public static enum TypeWriter {
            MOVEMENT;

            private TypeWriter() {
            }
        }
    }

    public static class ZNPCPathDelegator {
        private final File file;

        protected ZNPCPathDelegator(File file) {
            this.file = file;
        }

        public DataOutputStream getOutputStream() throws IOException {
            return new DataOutputStream(new FileOutputStream(this.file));
        }

        public DataInputStream getInputStream() throws IOException {
            return new DataInputStream(new FileInputStream(this.file));
        }

        public static ZNPCPathDelegator forFile(File file) {
            return new ZNPCPathDelegator(file);
        }

        public static ZNPCPathDelegator forPath(AbstractTypeWriter pathAbstract) {
            return new ZNPCPathDelegator(pathAbstract.getFile());
        }
    }

    public interface PathInitializer {
        void handle();

        ZLocation getLocation();

        public abstract static class AbstractPath implements PathInitializer {
            private final NPC npc;
            private final AbstractTypeWriter typeWriter;
            private ZLocation location;

            public AbstractPath(NPC npc, AbstractTypeWriter typeWriter) {
                this.npc = npc;
                this.typeWriter = typeWriter;
            }

            public NPC getNpc() {
                return this.npc;
            }

            public AbstractTypeWriter getPath() {
                return this.typeWriter;
            }

            public void setLocation(ZLocation location) {
                this.location = location;
            }

            public ZLocation getLocation() {
                return this.location;
            }
        }
    }
}
