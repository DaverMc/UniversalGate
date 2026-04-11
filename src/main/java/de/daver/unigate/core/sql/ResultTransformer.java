package de.daver.unigate.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

public interface ResultTransformer<T> {

    T transform(ResultSet set) throws SQLException;

    static <T> ResultTransformer<List<T>> asList(ResultTransformer<T> transformer) {
        return asCollection(ArrayList::new, transformer);
    }

    static <T> ResultTransformer<Set<T>> asSet(ResultTransformer<T> transformer) {
        return asCollection(HashSet::new, transformer);
    }

    static <T, C extends Collection<T>> ResultTransformer<C> asCollection(Supplier<C> supplier, ResultTransformer<T> transformer) {
        return set -> {
            C collection = supplier.get();
            while (set.next()) collection.add(transformer.transform(set));
            return collection;
        };
    }

}
