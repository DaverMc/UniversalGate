package de.daver.unigate;

import de.daver.unigate.lang.LanguageKey;

public enum LanguageKeys implements LanguageKey {

    DIMENSION_INVITE_RECEIVE("dimension.invite.receive", "sender", "dimension"),
    DIMENSION_INVITE_ACCEPT("dimension.invite.accept", "dimension"),
    DIMENSION_INVITE_SEND("dimension.invite.send", "target", "dimension"),
    DIMENSION_EXPORT_SUCCESS("dimension.export.success", "dimension"),
    DIMENSION_IMPORT_SUCCESS("dimension.import.success", "dimension"),
    DIMENSION_ALLOWED_REMOVE_SUCCESS("dimension.allowed.remove.success", "player", "dimension"),
    DIMENSION_ALLOWED_ADD_SUCCESS("dimension.allowed.add.success", "player", "dimension"),
    DIMENSION_ALLOWED_LIST_ENTRY("dimension.allowed.list.entry", "player"),
    DIMENSION_ALLOWED_LIST_HEADER("dimension.allowed.list.header", "dimension", "players"),
    DIMENSION_ENTER_FAILED("dimension.enter.failed", "dimension"),
    DIMENSION_ENTER_SUCCESS("dimension.enter.success", "dimension"),
    DIMENSION_INFO("dimension.info", "id", "name", "category", "type", "state", "stoplag"),
    DIMENSION_LIST_ENTRY("dimension.list.entry", "dimension"),
    DIMENSION_LIST_HEADER("dimension.list.header", "dimensions"),
    CATEGORY_LIST_ENTRY("category.list.entry", "category"),
    CATEGORY_LIST_HEADER("category.list.header", "categories"),
    CATEGORY_DELETE_SUCCESS("category.delete.success", "category"),
    CATEGORY_COMMAND_HELP("category.command.help"),
    CATEGORY_CREATE_SUCCESS("category.create.success", "category"),
    CHAT_FORMAT("chat.format", "prefix", "player", "suffix", "message"),
    EVENT_JOIN("event.join"),
    TAB_LIST_FOOTER("tab.list.footer"),
    TAB_LIST_HEADER("tab.list.header"),
    TAB_LIST_NAME("tab.list.name", "player"),
    LANGUAGE_RELOAD("language.reload"),
    DIMENSION_CREATE_SUCCESS("dimension.create.success", "dimension", "type"),
    DIMENSION_DELETE_SUCCESS("dimension.delete.success", "dimension"),
    DIMENSION_DELETE_CONFIRM("dimension.delete.confirm", "dimension"),
    DIMENSION_COMMAND_HELP("dimension.command.help"),
    SPEED_SET("speed.set", "speed"),
    DIMENSION_KICK_SUCCESS("dimension.kick.success", "target", "dimension");

    private final String key;
    private final String[] argNames;

    LanguageKeys(String key, String... argNames) {
        this.key = key;
        this.argNames = argNames;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String[] argNames() {
        return this.argNames;
    }
}
