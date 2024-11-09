package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;

import java.util.Random;

public class SaveTaskRequestBuilder extends BaseBuilder<SaveTaskRequest> {
    public SaveTaskRequestBuilder() { super(SaveTaskRequest.class);}

    public SaveTaskRequestBuilder withValidFields(){
        return this
                .withName(randomNameGenerator());
    }

    public SaveTaskRequestBuilder withName(String name){
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
