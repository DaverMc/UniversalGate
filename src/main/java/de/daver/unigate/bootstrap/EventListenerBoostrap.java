package de.daver.unigate.bootstrap;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class EventListenerBoostrap {

    private final Set<Listener> listeners;

    public EventListenerBoostrap() {
        this.listeners = new HashSet<>();
    }

    public EventListenerBoostrap add(Listener listener) {
        this.listeners.add(listener);
        return this;
    }

    public void registerAll(JavaPlugin plugin) {

    }

}
