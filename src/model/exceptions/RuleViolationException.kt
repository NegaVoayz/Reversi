package model.exceptions

class RuleViolationException(message: String?) : InvalidMoveException("Rule violated: " + message)
