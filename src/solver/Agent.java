package solver;

import java.util.List;

/**
 * The search agent
 */
public interface Agent {

    public List<StateCostPair> search(State initial, State goal);

}
