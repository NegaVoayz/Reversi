package model.exceptions;

public class RuleViolationException extends InvalidMoveException {
    public RuleViolationException(String message) {
        super("Rule violated: "+message);
    }
}
