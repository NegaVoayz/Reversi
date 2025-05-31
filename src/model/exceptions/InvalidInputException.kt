package model.exceptions

class InvalidInputException(input: String?) : GameException("Invalid input: " + input)