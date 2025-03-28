package model.rules;

import model.structs.Move;

public interface InputRule {
    Move ParseInput(String input);
}
