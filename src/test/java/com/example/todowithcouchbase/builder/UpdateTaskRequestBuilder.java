package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;
import com.github.javafaker.Faker;

public class UpdateTaskRequestBuilder extends BaseBuilder<UpdateTaskRequest>{
    public UpdateTaskRequestBuilder() {
        super(UpdateTaskRequest.class);
    }

    public UpdateTaskRequestBuilder withValidFields(){
        final Faker faker = new Faker();
        return this
                .withName(faker.name().name());

    }



    public UpdateTaskRequestBuilder withName(
            final String name
    ){
        data.setName(name);
        return this;
    }

    @Override
    public UpdateTaskRequest build() {
        return super.build();
    }
}
