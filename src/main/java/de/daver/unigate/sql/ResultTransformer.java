package de.daver.unigate.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ResultTransformer<T> {

    T transform(ResultSet set) throws SQLException;

    static <T> ResultTransformer<List<T>> asList(ResultTransformer<T> transformer) {
        return set -> {
            List<T> list = new ArrayList<>();
            while(set.next()) list.add(transformer.transform(set));
            return list;
        };
    }

}
