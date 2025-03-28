package model.rules;

public interface Rule {

    GameRule getGameRule();

    InputRule getInputRule();

    String toString();
}
