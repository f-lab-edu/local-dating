package com.template.spring.note;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoteNotFoundException extends ResponseStatusException {

    public NoteNotFoundException(long id) {
        super(HttpStatus.NOT_FOUND, "Note " + id + " was not found.");
    }
}
