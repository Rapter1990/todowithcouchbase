package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.UpdateTaskRequest;

public class UpdateTaskRequestBuilder extends BaseBuilder<UpdateTaskRequest>{
    public UpdateTaskRequestBuilder() {
        super(UpdateTaskRequest.class);
    }

    public UpdateTaskRequestBuilder withValidFields(){
        return this.withName("updateTaskRequest");
    }

    public UpdateTaskRequestBuilder withName(final String name){
        data.setName(name);
        return this;
    }

}
