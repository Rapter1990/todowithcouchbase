package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;
import com.github.javafaker.Faker;

import java.util.Random;

public class SaveTaskRequestBuilder extends BaseBuilder<SaveTaskRequest> {
    public SaveTaskRequestBuilder() { super(SaveTaskRequest.class);}

    public SaveTaskRequestBuilder withValidFields(){
        final Faker faker = new Faker();
        return this.withName(faker.name().name());
    }

    public SaveTaskRequestBuilder withName(String name){
        data.setName(name);
        return this;
    }

}
