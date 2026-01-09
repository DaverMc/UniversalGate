package de.daver.unigate.command;


public interface ArgumentSerializer<T> {

    String serialize(T value);

}
