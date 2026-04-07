package de.daver.unigate;

public interface Permissions {

    String PREFIX = "unigate.";

    String COMMAND = PREFIX + "command";
    String COMMAND_CREATIVE_ITEMS = COMMAND + ".creative_items";
    String COMMAND_DEBUG_STICK = COMMAND + ".debug_stick";
    String COMMAND_NIGHT_VISION = COMMAND + ".night_vision";
    String COMMAND_SPEED = COMMAND + ".speed";
    String COMMAND_ICON = COMMAND + ".icon";
    String COMMAND_ICON_RENAME = COMMAND_ICON + ".rename";
    String COMMAND_ICON_LORE = COMMAND_ICON + ".lore";
    String COMMAND_GAMEMODE = COMMAND + ".gamemode";
    String COMMAND_GAMEMODE_OTHER = COMMAND_GAMEMODE + ".other";

    String CATEGORY = PREFIX + "category";
    String CATEGORY_CREATE = CATEGORY + ".create";
    String CATEGORY_DELETE = CATEGORY + ".delete";
    String CATEGORY_LIST = CATEGORY + ".list";

    String LANGUAGE = PREFIX + "language";
    String LANGUAGE_RELOAD = LANGUAGE + ".reload";

    String DIMENSION = PREFIX + "dimension";
    String DIMENSION_ENTER = DIMENSION + ".enter";
    String DIMENSION_ENTER_ALL = DIMENSION_ENTER + ".all";
    String DIMENSION_ENTER_CATEGORY = DIMENSION_ENTER + ".category.";
    String DIMENSION_ALLOWED = DIMENSION + ".allowed";
    String DIMENSION_ALLOWED_ADD = DIMENSION_ALLOWED + ".add";
    String DIMENSION_ALLOWED_REMOVE = DIMENSION_ALLOWED + ".remove";
    String DIMENSION_ALLOWED_LIST = DIMENSION_ALLOWED + ".list";
    String DIMENSION_LIST = DIMENSION + ".list";
    String DIMENSION_INFO = DIMENSION + ".info";
    String DIMENSION_CREATE = DIMENSION + ".create";
    String DIMENSION_DELETE = DIMENSION + ".delete";
    String DIMENSION_EXPORT = DIMENSION + ".export";
    String DIMENSION_IMPORT = DIMENSION + ".import";
    String DIMENSION_STOPLAG = DIMENSION + ".stoplag";
    String DIMENSION_KICK = DIMENSION + ".kick";
    String DIMENSION_INVITE = DIMENSION + ".invite";
    String DIMENSION_INVITE_ACCEPT = DIMENSION_INVITE + ".accept";
    String DIMENSION_TELEPORT = DIMENSION + ".teleport";
    String DIMENSION_ACTIVATE = DIMENSION + ".activate";
    String DIMENSION_ARCHIVE = DIMENSION + ".archive";
    String DIMENSION_RENAME = DIMENSION + ".rename";
    String DIMENSION_GENERATECHUNKS = DIMENSION + ".generatechunks";

    String TASK = PREFIX + "task";
    String TASK_ACCEPT = TASK + ".accept";
    String TASK_APPROVE = TASK + ".approve";
    String TASK_CANCEL = TASK + ".cancel";
    String TASK_CREATE_CHANGE = TASK + ".create.change";
    String TASK_CREATE_NEW = TASK + ".create.new";
    String TASK_DECLINE = TASK + ".decline";
    String TASK_DELETE = TASK + ".delete";
    String TASK_LIST = TASK + ".list";
    String TASK_INFO = TASK + ".info";
    String TASK_FINISH = TASK + ".finish";
    String TASK_DESCRIPTION = TASK + ".description";
    String TASK_EXECUTOR = TASK + ".executor";
    String TASK_EXECUTOR_SET = TASK_EXECUTOR + ".set";
    String TASK_EXECUTOR_REMOVE = TASK_EXECUTOR + ".remove";
    String TASK_REOPEN = TASK + ".reopen";

    String STATUE = PREFIX + "statue";
    String STATUE_USE = STATUE + ".use";
    String STATUE_CLONE = STATUE + ".clone";
    String STATUE_DELETE = STATUE + ".delete";
    String STATUE_POSE = STATUE + ".pose";

}
