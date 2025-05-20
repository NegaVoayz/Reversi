package model.exceptions;

public abstract class InvalidMoveException extends GameException {
    public InvalidMoveException(String message) { super(message); }
}