package io.github.znetworkw.znpcservers.user;

import io.github.znetworkw.znpcservers.ServersNPC;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.bukkit.event.Event;

public class EventService<T extends Event> {
    private final Class<T> eventClass;
    private final List<Consumer<T>> eventConsumers;

    protected EventService(Class<T> eventClass, List<Consumer<T>> eventConsumers) {
        this.eventClass = eventClass;
        this.eventConsumers = eventConsumers;
    }

    public Class<T> getEventClass() {
        return this.eventClass;
    }

    public List<Consumer<T>> getEventConsumers() {
        return this.eventConsumers;
    }

    public EventService<T> addConsumer(Consumer<T> consumer) {
        this.getEventConsumers().add(consumer);
        return this;
    }

    public void runAll(T event) {
        ServersNPC.SCHEDULER.runTask(() -> {
            this.eventConsumers.forEach((consumer) -> {
                consumer.accept(event);
            });
        });
    }

    public static <T extends Event> EventService<T> addService(ZUser user, Class<T> eventClass) {
        if (hasService(user, eventClass)) {
            throw new IllegalStateException(eventClass.getSimpleName() + " is already register for " + user.getUUID().toString());
        } else {
            EventService<T> service = new EventService<>(eventClass, new ArrayList<>());
            user.getEventServices().add(service);
            user.toPlayer().closeInventory();
            return service;
        }
    }

    public static <T extends Event> EventService<T> findService(ZUser user, Class<T> eventClass) {
        Stream var10000 = user.getEventServices().stream().filter((eventService) -> {
            return eventService.getEventClass().isAssignableFrom(eventClass);
        });
        Objects.requireNonNull(EventService.class);
        return (EventService)var10000.map(EventService.class::cast).findFirst().orElse(null);
    }

    public static boolean hasService(ZUser user, Class<? extends Event> eventClass) {
        return user.getEventServices().stream().anyMatch((eventService) -> {
            return eventService.getEventClass() == eventClass;
        });
    }
}
