package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;

public class SaveTaskRequestBuilder extends BaseBuilder<SaveTaskRequest> {
    public SaveTaskRequestBuilder() { super(SaveTaskRequest.class);}

    public SaveTaskRequestBuilder withValidFields(){
        return this.withName("saveTaskRequest");
    }

    public SaveTaskRequestBuilder withName(String name){
        data.setName(name);
        return this;
    }

}
