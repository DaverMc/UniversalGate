package de.daver.unigate.core.sql.builder;

import de.daver.unigate.core.sql.ConvertedDataType;
import de.daver.unigate.core.sql.SQLArgument;
import de.daver.unigate.core.sql.SQLDataSetter;

import java.util.function.Function;

public class Arguments {

    public static SQLArgument<?> of(SQLDataType type) {
        return type.createArgument();
    }

    public static <T> SQLArgument<T> toString(Class<T> type) {
        return converted(type, SQLDataType.STRING, Object::toString);
    }

    public static <T extends Enum<T>> SQLArgument<T> enumName(Class<T> type) {
        return converted(type, SQLDataType.STRING, Enum::name);
    }

    public static <T, R> SQLArgument<T> converted(Class<T> type, SQLDataType nativeType, Function<T, R> convertable) {
        return new SQLArgument<>(type, new ConvertedDataType<T, R>() {

            @Override
            public R convert(T t) {
                return convertable.apply(t);
            }

            @Override
            @SuppressWarnings("unchecked")
            public SQLDataSetter<R> nativeType() {
                return (SQLDataSetter<R>) nativeType.setter();
            }
        });
    }

}
