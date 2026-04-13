package de.daver.unigate.api.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public interface Message {

    MessageKey key();
    TagResolver tagResolver();

    String raw(Locale locale);

    default Component component(Locale locale) {
        return MiniMessage.miniMessage().deserialize(raw(locale), tagResolver());
    }

    default Component component(CommandSender sender) {
        return component(getLocale(sender));
    }

    default void send(CommandSender sender) {
        sender.sendMessage(component(sender));
    }

    default void broadcast(Collection<? extends CommandSender> senders) {
        var cachedMessages = new HashMap<Locale, Component>();
        for(var sender : senders) {
            var component = cachedMessages.computeIfAbsent(getLocale(sender), this::component);
            sender.sendMessage(component);
        }
    }

    private Locale getLocale(CommandSender sender) {
        return sender instanceof Player player ? player.locale() : Locale.getDefault();
    }


}
