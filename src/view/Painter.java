package view;

import model.rules.Rule;
import model.structs.GameStatistics;

public interface Painter {
    void updateGameStatistics(GameStatistics statistics, Rule rule);
    void flush();
}
