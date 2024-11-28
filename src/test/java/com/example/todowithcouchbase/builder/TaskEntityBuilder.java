package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.entity.TaskEntity;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskEntityBuilder extends BaseBuilder<TaskEntity>{
    public TaskEntityBuilder() { super(TaskEntity.class);}

    public TaskEntity withValidFields(){
        return this
                .withId(UUID.randomUUID().toString())
                .withName(randomNameGenerator())
                .build();
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

        final int nameLength = 6; // Length of the generated name
        final int startIndex = 0; // Starting index for random selection
        String turkishAlphabet = "abcçdefgğhıijklmnoöprsştuüvyz"; // Turkish alphabet characters
        Random random = new Random();

        return random.ints(nameLength, startIndex, turkishAlphabet.length())
                .mapToObj(turkishAlphabet::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());

    }

}
