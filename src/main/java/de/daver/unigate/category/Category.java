package de.daver.unigate.category;


public record Category(String name) {

    public String id() {
        return name().toLowerCase();
    }

}
