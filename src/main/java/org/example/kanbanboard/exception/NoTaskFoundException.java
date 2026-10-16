package org.example.kanbanboard.exception;

public class NoTaskFoundException extends Exception {
    public NoTaskFoundException(){};
    public NoTaskFoundException(String message) {
        super(message);
    }
}
