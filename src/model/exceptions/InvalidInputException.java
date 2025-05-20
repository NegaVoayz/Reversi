package model.exceptions;

public class InvalidInputException extends GameException {
    public InvalidInputException(String input) {
        super("Invalid input: " + input);
    }
}