import java.util.ArrayList;

public class graphMaster {

    /* PDDL:
    Init(at(startPointX, startPointY))
    Goal(at(endPointX, endPointY))

    Action(MoveX(X1,Y1,X2),
        PRECOND: IsClear(X2, Y1) && at(X1, Y1) && |X1 - X2| == 1
        EFFECT:  !at(X1, Y1) && at(X2, Y1))
    Action(MoveY(X1,Y1,Y2),
        PRECOND: IsClear(X1, Y2) && at(X1, Y1) && |Y1 - Y2| == 1
        EFFECT:  !at(X1, Y1) && at(X1, Y2))

    Action(Move(Left)
        PRECOND: IsClear(Left)
        EFFECT:  Poss(x-1,y))
    Action(Move(Right)
        PRECOND: IsClear(Right)
        EFFECT:  Poss(x+1,y))
    Action(Move(Up)
        PRECOND: IsClear(Up)
        EFFECT:  Poss(x,y-1))
    Action(Move(Down)
        PRECOND: IsClear(Down)
        EFFECT:  Poss(x,y+1))

    CAN YOU DO A HLA THAT WOULD MOVE N TIMES IN A DIRECTION? WOULD THAT REDUCE TREE SIZE?

    I think the best plan is to just do one action per level with no HLAs. That way I can just do a level count heuristic.
    Like it would be super trash on memory cost, but it'd work.
     */


    //RECORDING 22 28:38

    /*
        The Plan:
        - reuse the file reading code from last time
        - somehow parse that into a planning graph
        - create a set of rules/methods that will be used for navigation
        - define the mutexes
        - Store the nogoods somehow???


        The challenge of this project comes from building the graph and then extracting the solution from said graph.

        The Graph:
        - Linked list?
        - Maybe all nodes are objects and each level is its own array?
        - event object: contains an action value and a list of possible results
        - For mutexes: maybe have a global map that maps the objects together? this might cut down on redundancy of private vars

        Node Object:
            Has:
            - Parent
            - Outcomes
            - Action
         */

    private byte startPointX, startPointY;
    private ArrayList<Byte> endPointX, endPointY;
    public static byte[][] map; //I made this static as a shortcut and should probably fix it

    /**
     * Constructor
     * @param m map of world space
     * @param x starting x position
     * @param y starting y position
     * @param epX arraylist of endpoint x coordinates
     * @param epY arraylist of endpoint y coordinates
     */
    graphMaster(byte[][] m, byte x, byte y, ArrayList<Byte> epX, ArrayList<Byte> epY) {
        map = m;
        endPointX = epX;
        endPointY = epY;
        startPointX = x;
        startPointY = y;
    }

    void buildGraph() {
        ArrayList<ArrayList<state>> state_levels = new ArrayList<>();
        ArrayList<ArrayList<action>> action_levels = new ArrayList<>();

        ArrayList<state> s0 = new ArrayList<>();
        s0.add(new state(null, true, startPointX, startPointY));

        while (state_levels.size() < 300) {
            ArrayList<state> possibleSolutions = new ArrayList<>();

            //generate actions
            ArrayList<action> aN = new ArrayList<>();
            for (state s : state_levels.get(state_levels.size()-1)) { //for each state in the current level
                action[] acts = s.generateActions();
                //nightmere
                for (action a : acts) {
                    boolean alreadyExists = false;
                    for (action existingAction : aN) {
                        if (a.outcomes == existingAction.outcomes) {
                            alreadyExists = true;
                            existingAction.parents.add(s);
                            break; // TODO: check if this actually breaks the loop
                        }
                    }
                    if (!alreadyExists) {
                        aN.add(a);
                    }
                }
            }
            action_levels.add(aN);

            //generate states
            ArrayList<state> sN = new ArrayList<>();
            for (action a : action_levels.get(action_levels.size()-1)) { //I'M GOING TO SCREAM
                for (state outcome : a.outcomes) {
                    boolean alreadyExists = false;
                    for (state existingState : sN) {
                        if (outcome.isNegated == existingState.isNegated
                                && existingState.isEqual(outcome.X, outcome.Y)) {
                            alreadyExists = true;
                            existingState.parents.add(a);
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        sN.add(outcome);
                        for (int j = 0; j < endPointX.size(); j++) {
                            if (outcome.isEqual(endPointX.get(j), endPointY.get(j)))
                                possibleSolutions.add(sN.get(sN.size()-1));
                            //I'm really not sure how Java is going to handle this
                        }
                    }
                }
            }
            state_levels.add(sN);

            //check for solutions
        }
    }

    public static boolean isClear(byte x, byte y) {
        try {
            return (map[x][y] < 3);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
