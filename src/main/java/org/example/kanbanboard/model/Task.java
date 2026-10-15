package org.example.kanbanboard.model;

import lombok.Builder;

@Builder
public record Task(int id, String description, Status status) {
}
