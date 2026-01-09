package de.daver.unigate.command.impl.lang;

import de.daver.unigate.command.LiteralNode;

public class LanguageCommand extends LiteralNode {

    public LanguageCommand() {
        super("language");
        then(new ReloadSubCommand());
    }
}
