package de.daver.unigate.core.command;


public interface ArgumentSerializer<T> {

    String serialize(T value);

}
