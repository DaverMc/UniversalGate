package de.daver.unigate.category;


import de.daver.unigate.Permissions;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public record Category(UUID id, String name, String prefix) {

    public boolean canUse(CommandSender sender) {
        return sender.hasPermission(Permissions.DIMENSION_ENTER_CATEGORY + prefix) |
                sender.hasPermission(Permissions.DIMENSION_ENTER_ALL);
    }

    public static final String DEFAULT_NAME = "default";

}
