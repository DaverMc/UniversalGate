package de.daver.unigate.core.sql.builder;

import de.daver.unigate.core.sql.builder.steps.*;

import java.util.ArrayList;


public class SQLStatementBuilder {

    public static Step.SelectStep select(String... columns) {
        return new SelectStepImpl(new StringBuilder(), new ArrayList<>(), columns);
    }

    public static Step.InsertStep insert() {
        return new InsertStepImpl(new StringBuilder(), new ArrayList<>());
    }

    public static Step.UpdateStep update() {
        return new UpdateStepImpl(new StringBuilder(), new ArrayList<>());
    }

    public static Step.DeleteStep delete() {
        return new DeleteStepImpl(new StringBuilder(), new ArrayList<>());
    }

    public static Step.CreateStep create() {
        return new CreateStepImpl(new StringBuilder(), new ArrayList<>());
    }

}
