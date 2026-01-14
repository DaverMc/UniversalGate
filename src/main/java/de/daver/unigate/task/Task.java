package de.daver.unigate.task;

import java.util.UUID;

public class Task {

    private final String id;
    private final UUID creator;

    private UUID member;
    private TaskState state;

    public Task(String id, UUID creator, TaskState state) {
        this.id = id;
        this.creator = creator;
        this.member = null;
        this.state = state;
    }

    public String id() {
        return this.id;
    }

    public UUID creator() {
        return this.creator;
    }

    public UUID member() {
        return this.member;
    }

    public TaskState state() {
        return this.state;
    }

    public void setMember(UUID uuid) {
        this.member = uuid;
    }
}

