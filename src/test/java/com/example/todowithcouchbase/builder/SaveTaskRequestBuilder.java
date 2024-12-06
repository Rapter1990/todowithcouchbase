package com.example.todowithcouchbase.builder;

import com.example.todowithcouchbase.task.model.dto.request.SaveTaskRequest;

/**
 * A builder class for creating instances of {@link SaveTaskRequest} with specific properties set.
 */
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
