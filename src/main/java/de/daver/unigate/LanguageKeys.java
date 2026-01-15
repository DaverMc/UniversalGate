package de.daver.unigate;

import de.daver.unigate.core.lang.LanguageKey;

public enum LanguageKeys implements LanguageKey {

    GUI_STATUE_INVENTORY("gui.statue.inventory"),
    ITEM_STATUE_INVENTORY_TITLE("item.statue.inventory.title"),
    ITEM_STATUE_SETTINGS_TITLE("item.statue.settings.title"),
    STATUE_CLONED("statue.cloned"),
    STATUE_NOT_SELECTED("statue.not.selected"),
    STATUE_TOOLS_REMOVED("statue.tools.removed"),
    ITEM_STATUE_HEAD_NAME("item.statue.head.name"),
    ITEM_STATUE_BODY_NAME("item.statue.body.name"),
    ITEM_STATUE_ARM_LEFT_NAME("item.statue.arm.left.name"),
    ITEM_STATUE_ARM_RIGHT_NAME("item.statue.arm.right.name"),
    ITEM_STATUE_LEG_LEFT_NAME("item.statue.leg.left.name"),
    ITEM_STATUE_LEG_RIGHT_NAME("item.statue.leg.right.name"),
    ITEM_STATUE_POSITION_NAME("item.statue.position.name"),
    ITEM_STATUE_EDITOR_LORE_X( "item.statue.editor.lore.x"),
    ITEM_STATUE_EDITOR_LORE_Y( "item.statue.editor.lore.y"),
    ITEM_STATUE_EDITOR_LORE_Z( "item.statue.editor.lore.z"),
    STATUE_TOOLS_CHANGE_AXIS("statue.tools.change.axis", "axis"),
    STATUE_TOOLS_RECEIVED("statue.tools.received"),
    STATUE_DESELECTED("statue.deselected"),
    STATUE_SELECTED("statue.selected"),
    COMMAND_HUB("command.hub"),
    TASK_REOPEN_SUCCESS("task.reopen.success", "task"),
    TASK_NOT_DECLINED("task.not.declined", "task"),
    TASK_EXECUTOR_REMOVE("task.executor.remove", "task", "executor"),
    TASK_EXECUTOR_SET("task.executor.set", "task", "executor"),
    TASK_FINISH_SUCCESS("task.finish.success", "task"),
    TASK_NO_EXECUTOR("task.no.executor", "task"),
    TASK_CANCEL_SUCCESS("task.cancel.success", "task"),
    TASK_NOT_IN_WORK("task.not.in.work", "task"),
    TASK_NOT_OPEN("task.not.open", "task"),
    TASK_ACCEPT_SUCCESS("task.accept.success", "task"),
    TASK_APPROVE_SUCCESS("task.approve.success", "task"),
    TASK_DECLINE_CONFIRM( "task.decline.confirm", "task"),
    TASK_NOT_FINISHED("task.not.finished", "task"),
    TASK_DECLINE_SUCCESS("task.decline.success", "task"),
    TASK_DESCRIPTION_SET("task.description.set", "task", "description"),
    TASK_DELETE_SUCCESS("task.delete.success", "task"),
    TASK_DELETE_CONFIRM("task.delete.confirm", "task"),
    TASK_INFO( "task.info", "id", "dimension", "action", "state", "creator", "executor", "description"),
    TASK_LIST_ENTRY( "task.list.entry", "id", "action"),
    TASK_LIST_HEADER("task.list.header", "tasks"),
    TASK_CREATE_NEW("task.create.new", "task", "dimension"),
    TASK_CREATE_CHANGE("task.create.change", "task", "dimension"),
    EVENT_PORTAL_DISABLED("event.portal.disabled"),
    EVENT_LEAVE("event.leave", "player"),
    DIMENSION_STOPLAG_DISABLE("dimension.stoplag.disable", "dimension"),
    DIMENSION_STOPLAG_ENABLE("dimension.stoplag.enable", "dimension"),
    SERVER_VERSION("server.version"),
    SERVER_MOTD("server.motd"),
    SERVER_LISTED_PLAYERS("server.listed.players"),
    BORDER_UNLIMITED("border.unlimited"),
    BORDER_DEFAULT("border.default"),
    NIGHTVISION_ADD("nightvision.add"),
    NIGHTVISION_REMOVED("nightvision.removed"),
    ITEM_LIGHT_WATER_TITLE("item.light.water.title", "level"),
    ITEM_LIGHT_AIR_TITLE("item.light.air.title", "level"),
    GUI_CREATIVE_ITEMS_TITLE("gui.creativeitems.title"),
    DEBUG_STICK_GIVEN("debugstick.given"),
    DIMENSION_INVITE_RECEIVE("dimension.invite.receive", "sender", "dimension"),
    DIMENSION_INVITE_ACCEPT("dimension.invite.accept", "dimension"),
    DIMENSION_INVITE_SEND("dimension.invite.send", "target", "dimension"),
    DIMENSION_EXPORT_SUCCESS("dimension.export.success", "dimension", "tag"),
    DIMENSION_IMPORT_SUCCESS("dimension.import.success", "dimension"),
    DIMENSION_ALLOWED_REMOVE_SUCCESS("dimension.allowed.remove.success", "player", "dimension"),
    DIMENSION_ALLOWED_ADD_SUCCESS("dimension.allowed.add.success", "player", "dimension"),
    DIMENSION_ALLOWED_LIST_ENTRY("dimension.allowed.list.entry", "player"),
    DIMENSION_ALLOWED_LIST_HEADER("dimension.allowed.list.header", "dimension", "players"),
    DIMENSION_ENTER_FAILED("dimension.enter.failed", "dimension"),
    DIMENSION_ENTER_SUCCESS("dimension.enter.success", "dimension"),
    DIMENSION_INFO("dimension.info", "id", "name", "category", "action", "state", "stoplag"),
    DIMENSION_LIST_ENTRY("dimension.list.entry", "dimension"),
    DIMENSION_LIST_HEADER("dimension.list.header", "dimensions"),
    CATEGORY_LIST_ENTRY("category.list.entry", "category", "id"),
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
    DIMENSION_CREATE_SUCCESS("dimension.create.success", "dimension", "action"),
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
