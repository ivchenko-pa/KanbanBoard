package org.example.kanbanboard.dto;

import org.example.kanbanboard.model.Status;

public record TaskDto(String description, Status status) {
}
