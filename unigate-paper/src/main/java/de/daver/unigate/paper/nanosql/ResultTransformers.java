package de.daver.unigate.paper.nanosql;

import de.daver.unigate.api.nanosql.ResultTransformer;
import de.daver.unigate.api.nanosql.RowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultTransformers {

    private ResultTransformers() {}

    public static <R> ResultTransformer<List<R>> toList(RowMapper<R> rowTransformer) {
        return result -> {
            var list = new ArrayList<R>();
            while(result.next())
                list.add(rowTransformer.map(new SimpleResultRow(result)));
            return list;
        };
    }

    public static <K, V> ResultTransformer<Map<K, V>> toMap(RowMapper<K> keyMapper, RowMapper<V> valueMapper) {
        return result -> {
            var map = new HashMap<K, V>();
            while (result.next()) {
                var row = new SimpleResultRow(result);
                map.put(keyMapper.map(row), valueMapper.map(row));
            }
            return map;
        };
    }


}
