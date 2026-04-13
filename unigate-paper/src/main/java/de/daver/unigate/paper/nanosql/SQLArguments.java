package de.daver.unigate.paper.nanosql;


import de.daver.unigate.api.nanosql.SQLArgument;
import de.daver.unigate.paper.nanosql.argument.StringSQLArgument;

import java.util.function.Function;

public  interface SQLArguments {

    static <T> SQLArgument<T> string(Class<T> type, Function<T, String> transformer) {
        return new StringSQLArgument<>(transformer, type);
    }

    static <T> SQLArgument<T> string(Class<T> type) {
        return string(type, String::valueOf);
    }

    static  SQLArgument<String> string() {
        return string(String.class);
    }

    static <E extends Enum<E>> SQLArgument<E> enumValue(Class<E> type) {
        return new StringSQLArgument<>(Enum::name, type);
    }

}
