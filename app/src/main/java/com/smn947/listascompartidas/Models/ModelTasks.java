package com.smn947.listascompartidas.Models;

public class ModelTasks {
    private int id, status;
    private String task;
    private String list;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public String getList() {
        return list;
    }
    public void setList(String list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int Status) {
        this.status = Status;
    }
}
