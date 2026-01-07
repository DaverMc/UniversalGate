package de.daver.unigate.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record SQLStatement(String raw, List<Argument<?>> arguments) {

    public SQLStatement(String raw) {
        this(raw, new ArrayList<>());
    }

    public SQLStatement addStringArgument() {
        return addArgument(new Argument<>(String.class, SQLDataType.STRING));
    }

    public SQLStatement addLongArgument() {
        return addArgument(new Argument<>(Long.class, SQLDataType.LONG));
    }

    public SQLStatement addBooleanArgument() {
        return addArgument(new Argument<>(Boolean.class,
                (value, statement, index) ->
                        statement.setInt(index, (value)? 1 : 0)));
    }

    public <T, N> SQLStatement addConverted(Class<T> type, SQLDataType<N> nativeType, Function<T, N> convertable) {
        return addArgument(new Argument<>(type, new ConvertedDataType<T, N>() {

            @Override
            public N convert(T t) {
                return convertable.apply(t);
            }

            @Override
            public SQLDataType<N> nativeType() {
                return nativeType;
            }
        }));
    }

    public SQLStatement addArgument(Argument<?> argument) {
        arguments.add(argument);
        return this;
    }


    public record Argument<T>(Class<T> clazz, SQLDataType<T> type) {

        void apply(PreparedStatement statement, Object object, int index) throws SQLException {
            T value = clazz.cast(object);
            type.set(value, statement, index);
        }
    }

}
