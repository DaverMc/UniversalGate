package de.daver.unigate.command.lang;

import de.daver.unigate.core.command.LiteralNode;

public class LanguageCommand extends LiteralNode {

    public LanguageCommand() {
        super("language");
        then(new ReloadSubCommand());
    }
}
