package com.brodygaudel.ebank.common.event;

public class CustomerDeletedEvent extends BaseEvent<String>{
    public CustomerDeletedEvent(String id) {
        super(id);
    }
}
