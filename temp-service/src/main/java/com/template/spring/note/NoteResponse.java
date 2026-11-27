package com.template.spring.note;

import java.time.Instant;
import java.time.ZoneOffset;

public record NoteResponse(Long id, String title, String content, Instant createdAt) {

    static NoteResponse from(Note note) {
        return new NoteResponse(note.getId(), note.getTitle(), note.getContent(),
                note.getCreatedAt().toInstant(ZoneOffset.UTC));
    }
}
