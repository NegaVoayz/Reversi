package model.rules;

import model.enums.Player;
import model.structs.Move;

public interface InputRule {
    Move ParseInput(String input);
}
