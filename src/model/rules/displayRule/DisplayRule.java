package model.rules.displayRule;

import model.rules.Rule;
import model.structs.GameStatistics;

public interface DisplayRule {

    boolean buildView(GameStatistics statistics, Rule rule);

    boolean update(GameStatistics statistics, Rule rule);
}
