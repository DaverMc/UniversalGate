package de.daver.unigate.dimension;

import de.daver.unigate.core.sql.ResultTransformer;
import de.daver.unigate.core.sql.SQLStatement;
import de.daver.unigate.core.sql.builder.Arguments;
import de.daver.unigate.core.sql.builder.SQLDataType;
import de.daver.unigate.core.sql.builder.SQLStatementBuilder;
import de.daver.unigate.core.sql.builder.SQLiteColumnType;
import de.daver.unigate.dimension.gen.DimensionType;
import org.checkerframework.checker.units.qual.A;

import java.util.Random;
import java.util.UUID;

interface Queries {
     SQLStatement SELECT_ALL = SQLStatementBuilder.select("*")
             .from("dimensions")
             .build();

     SQLStatement SELECT_ACTIVE = SQLStatementBuilder.select("*")
             .from("dimensions")
             .where("state = 'ACTIVE'")
             .build();

     SQLStatement SELECT_ARCHIVED = SQLStatementBuilder.select("*")
             .from("dimensions")
             .where("state = 'ARCHIVED'")
             .build();

     SQLStatement SELECT_ARCHIVED_DIMENSION = SQLStatementBuilder.select("*")
             .from("dimensions")
             .where("state = 'ARCHIVED' AND name = ?")
             .argument(Arguments.of(SQLDataType.STRING))
             .build();

     SQLStatement SELECT_DIMENSION = SQLStatementBuilder.select("*")
             .from("dimensions")
             .where("name = ?")
             .argument(Arguments.of(SQLDataType.STRING))
             .build();

     SQLStatement CREATE_DIMENSIONS_TABLE = SQLStatementBuilder.create()
             .table("IF NOT EXISTS dimensions")  //TODO DIRTY FIX
             .primaryKey("id", SQLiteColumnType.TEXT)
             .column("name", SQLiteColumnType.TEXT)
             .column("type", SQLiteColumnType.TEXT)
             .column("creation_time", SQLiteColumnType.INTEGER)
             .column("creator", SQLiteColumnType.TEXT)
             .column("stop_lag", SQLiteColumnType.INTEGER)
             .column("state", SQLiteColumnType.TEXT)
             .column("last_loaded", SQLiteColumnType.INTEGER)
             .build();

     SQLStatement CREATE_ALLOWED_TABLE = SQLStatementBuilder.create()
             .table("IF NOT EXISTS allowed_dimensions")//TODO DIRTY FIX
             .primaryKey("dimension", SQLiteColumnType.TEXT)
             .primaryKey("player", SQLiteColumnType.TEXT)
             .build();

     SQLStatement INSERT_DIMENSION = SQLStatementBuilder.insert()
             .into("dimensions")
             .columns("id", "name", "type", "creation_time", "creator", "stop_lag", "state", "last_loaded")
             .argument(Arguments.toString(UUID.class))
             .argument(Arguments.of(SQLDataType.STRING))
             .argument(Arguments.enumName(DimensionType.class))
             .argument(Arguments.of(SQLDataType.LONG))
             .argument(Arguments.toString(UUID.class))
             .argument(Arguments.of(SQLDataType.BOOL))
             .argument(Arguments.enumName(DimensionState.class))
             .argument(Arguments.of(SQLDataType.LONG))
             .build();

     SQLStatement INSERT_ALLOWED = SQLStatementBuilder.insert()
             .into("allowed_dimensions")
             .columns("dimension", "player")
             .argument(Arguments.toString(UUID.class))
             .argument(Arguments.of(SQLDataType.STRING))
             .build();

     SQLStatement DELETE_ALLOWED = SQLStatementBuilder.delete()
             .from("allowed_dimensions")
             .where("dimension = ? AND player = ?")
             .argument(Arguments.toString(UUID.class))
             .argument(Arguments.of(SQLDataType.STRING))
             .build();

     SQLStatement SELECT_ALLOWED = SQLStatementBuilder.select("*")
             .from("allowed_dimensions")
             .where("dimension = ?")
             .argument(Arguments.toString(UUID.class))
             .build();

     SQLStatement UPDATE_DIMENSION_META = SQLStatementBuilder.update("dimensions")
             .set("name", "stop_lag")
             .where("id = ?")
             .argument(Arguments.of(SQLDataType.STRING))
             .argument(Arguments.of(SQLDataType.BOOL))
             .argument(Arguments.toString(UUID.class))
             .build();

     SQLStatement UPDATE_DIMENSION_STATE = SQLStatementBuilder.update("dimensions")
             .set("state", "last_loaded")
             .where("id = ?")
             .argument(Arguments.enumName(DimensionState.class))
             .argument(Arguments.of(SQLDataType.LONG))
             .argument(Arguments.toString(UUID.class))
             .build();

     SQLStatement DELETE_DIMENSION = SQLStatementBuilder.delete()
             .from("dimensions")
             .where("id = ?")
             .argument(Arguments.toString(UUID.class))
             .build();

     SQLStatement DELETE_DIMENSION_ALLOWED = SQLStatementBuilder.delete()
             .from("allowed_dimensions")
             .where("dimension = ?")
             .argument(Arguments.toString(UUID.class))
             .build();

    ResultTransformer<Dimension> DIMENSION_TRANSFORMER = set -> {
        String idS = set.getString("id");
        if (idS == null) return null;
        UUID id = UUID.fromString(idS);

        String name = set.getString("name");
        if (name == null) return null;

        var typeString = set.getString("type");
        if (typeString == null) return null;
        DimensionType type = DimensionType.valueOf(typeString);

        long creationTime = set.getLong("creation_time");

        String uuidS = set.getString("creator");

        int stopLagI = set.getInt("stop_lag");

        DimensionState state = DimensionState.valueOf(set.getString("state"));

        long lastLoaded = set.getLong("last_loaded");

        DimensionStats stats = new DimensionStats(UUID.fromString(uuidS), creationTime);

        DimensionMeta meta = new DimensionMeta(state, stopLagI == 1, lastLoaded);
        return new Dimension(id, name, type, stats, meta, new Random().nextLong());
    };

    ResultTransformer<String> NAME_TRANSFORMER = set -> set.getString("name");

    ResultTransformer<UUID> ALLOWED_TRANSFORMER = set -> UUID.fromString(set.getString("player"));
}
