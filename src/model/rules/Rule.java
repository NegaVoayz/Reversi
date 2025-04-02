package model.rules;

public interface Rule {

    GameRule getGameRule();

    InputRule getInputRule();

    String toString();

    default String getName() { return "How the hell could you reach here?"; }
}
