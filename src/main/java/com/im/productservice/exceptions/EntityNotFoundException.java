package com.im.productservice.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException couldNotFindEntity(Class entity, Long id) {
        return new EntityNotFoundException("Could not find " + entity.getSimpleName() + " with id" + id);
    }
}
