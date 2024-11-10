package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.entity.TaskEntity;

import java.util.Random;
import java.util.UUID;

public class TaskEntityBuilder extends BaseBuilder<TaskEntity>{
    public TaskEntityBuilder() { super(TaskEntity.class);}

    public TaskEntityBuilder withValidFields(){
        return this
                .withId(UUID.randomUUID().toString())
                .withName(randomNameGenerator());
    }

    public TaskEntityBuilder withId(String id){
        data.setId(id);
        return this;
    }

    public TaskEntityBuilder withName(String name){
        data.setName(name);
        return this;
    }
    private String randomNameGenerator(){
        Random rnd = new Random();
        StringBuilder value = new StringBuilder();
        char[] dizi = new char[]{'a','b','c','d'};
        for(int i=0 ;i<6;i++) {
            value.append(dizi[rnd.nextInt(3)]);
        }
        return value.toString();
        }

}
