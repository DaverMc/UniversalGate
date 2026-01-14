package de.daver.unigate.task;

import java.util.UUID;

public class Task {

    private final String id;
    private final UUID creator;
    private final TaskType type;
    private final String dimensionId;

    private UUID executor;
    private TaskState state;
    private String description;

    public Task(String id, UUID creator, TaskType type, TaskState state, String dimensionId) {
        this.id = id;
        this.creator = creator;
        this.executor = null;
        this.state = state;
        this.type = type;
        this.dimensionId = dimensionId;
    }

    public String id() {
        return this.id;
    }

    public UUID creator() {
        return this.creator;
    }

    public UUID executor() {
        return this.executor;
    }

    public TaskState state() {
        return this.state;
    }

    public String dimensionId() {
        return this.dimensionId;
    }

    public TaskType type() {
        return this.type;
    }

    public String description() {
        return this.description;
    }

    public void setExecutor(UUID uuid) {
        this.executor = uuid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(TaskState taskState) {
        this.state = taskState;
    }
}

