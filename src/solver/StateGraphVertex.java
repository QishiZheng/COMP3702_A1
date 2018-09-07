package solver;

import java.util.ArrayList;

/**
 * Node class for StateGraph
 *
 */
public class StateGraphVertex<State> {
    private State state;

    public StateGraphVertex(State s) {
        this.state = s;
    }

    /**
     *
     * @return the state that stored in this node
     */
    public State getState() {
        return state;
    }

    /**
     * Check if the given object is equal to this StateGraphVertex
     * @param obj given object
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if(obj instanceof StateGraphVertex) {
           StateGraphVertex n = (StateGraphVertex) obj;
           if(n.getState().equals(this.state)) {
               return true;
           }
        }
        return false;
    }


}
