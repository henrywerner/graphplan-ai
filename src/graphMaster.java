import java.util.ArrayList;
import java.util.Stack;

/* PDDL:
    Init(at(startPointX, startPointY))
    Goal(at(endPointX, endPointY))

    Action(MoveX(X1,Y1,X2),
        PRECOND: IsClear(X2, Y1) && at(X1, Y1) && |X1 - X2| == 1
        EFFECT:  !at(X1, Y1) && at(X2, Y1))
    Action(MoveY(X1,Y1,Y2),
        PRECOND: IsClear(X1, Y2) && at(X1, Y1) && |Y1 - Y2| == 1
        EFFECT:  !at(X1, Y1) && at(X1, Y2))
*/

public class graphMaster {
    private final byte startPointX, startPointY;
    private final ArrayList<Byte> endPointX, endPointY;
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

        buildGraph();
    }

    /** Graph construction and expansion **/
    void buildGraph() { //Nightmare code. Very big O. Not memory efficient. Somehow still works.
        ArrayList<ArrayList<state>> state_levels = new ArrayList<>();
        ArrayList<ArrayList<action>> action_levels = new ArrayList<>();

        ArrayList<state> s0 = new ArrayList<>();
        s0.add(new state(null, true, startPointX, startPointY));
        state_levels.add(s0);

        /*core loop*/
        while (state_levels.size() < 300) {     //arbitrary limit at 300 levels

            /*generate actions*/
            ArrayList<action> aN = new ArrayList<>();
            for (state s : state_levels.get(state_levels.size()-1)) {   //for each state in the current level
                action[] acts = s.generateActions();
                //nightmare
                for (action a : acts) {                     //for each potential action from current state
                    boolean alreadyExists = false;
                    for (action existingAction : aN) {      //check against existing actions in current level
                        if (a.outcomes == existingAction.outcomes) {
                            alreadyExists = true;
                            existingAction.parents.add(s);  //if alreadyExists, add s to the action's parents
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        aN.add(a); //add new actions to the level arraylist
                    }
                }
            }
            action_levels.add(aN);

            /*generate states*/
            ArrayList<state> possibleSolutions = new ArrayList<>();
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
                            //I'm really not sure how Java is going to handle this.  Edit: it seems to have worked out.
                        }
                    }
                }
            }
            state_levels.add(sN);

            /*check for solutions*/
            while (!possibleSolutions.isEmpty()) {
                if (extractSolution(possibleSolutions.get(0))) { //answer found
                    handler.createVisualMap(map); //print updated visual map
                    return;
                }

                /* This is where I _would_ put the failed solution into a NOGOODS table, but it didn't seem necessary
                *  because there is only one state required for the goal. Possible solutions are only considered when
                *  they initially touch a goal, so the only way a bad path could be reevaluated is if it somehow touched
                *  the goal a second time. */
                possibleSolutions.remove(0);
            }

            /*check if actions or states have leveled out*/
            int aLvl_size = action_levels.size() - 1;
            //int sLvl_size = state_levels.size() - 1;
            //System.out.println("Actions at A" + aLvl_size + ": " + action_levels.get(aLvl_size).size());
            //System.out.println("States at S" + sLvl_size + ": " + state_levels.get(sLvl_size).size());
            if (aLvl_size > 2) {
                if (action_levels.get(aLvl_size).size() == action_levels.get(aLvl_size-1).size()) {
                    System.out.println("Graph leveled out. \nNo Solution");
                    return;
                }
            }

        }
    }

    /**
     * Backwards trace a given state to find and parse a possible solution.
     * @param s state representing a reached goal
     * @return if a solution was identified
     */
    boolean extractSolution(state s) {
        Stack<Integer> path = traceback(s);

        if (path != null) { //working solution was found
            Stack<String> output = new Stack<>();

            int at_X = path.peek();
            int at_Y = at_X / 10;
            at_X = at_X % 10;
            path.pop();

            map[at_X][at_Y] = 6; //set endpoint marker

            /*calculate movement directions and print to console*/
            while (!path.isEmpty()) {
                int prior_X = path.peek();
                int prior_Y = prior_X / 10;
                prior_X = prior_X % 10;

                if (prior_X == at_X) { //likely y movement
                    if (prior_Y + 1 == at_Y) { //down
                        output.push("Down");
                    }
                    else if (prior_Y - 1 == at_Y) { //up
                        output.push("Up");
                    }
                    else if (prior_Y == at_Y) { //stayed in place
                        output.push("Stayed in place");
                    }
                    else {
                        output.push("Unrecognized movement");
                    }
                }
                else if (prior_Y == at_Y) { //likely x movement
                    if (prior_X + 1 == at_X) { //right
                        output.push("Right");
                    }
                    else if (prior_X - 1 == at_X) { //left
                        output.push("Left");
                    }
                    else {
                        output.push("Unrecognized movement");
                    }
                }

                at_X = prior_X;
                at_Y = prior_Y;
                map[at_X][at_Y] = 5;
                path.pop();
            }

            System.out.println("Endpoint found. \nSolution:");
            while (!output.isEmpty()) { //It's 2:30am and I couldn't be bothered to reverse the stack properly
                System.out.println(output.peek());
                output.pop();
            }
            System.out.println();

            return true;
        }
        else {
            System.out.println("Potential solution traceback failed.");
        }
        return false;
    }

    /**
     * Search the parents of a given state to find a path to the starting point.
     * @param s State
     * @return Stack of agent locations corresponding to a valid path from the starting point. Null if path not found.
     */
    Stack<Integer> traceback(state s) {
        if (s.parents.get(0) == null) { //starting point
            Stack<Integer> path = new Stack<>();
            path.push(s.Y * 10 + s.X); //space index of 0-99
            return path;
        }
        for (action a : s.parents) {
            Stack<Integer> path = traceback(a);
            if (path != null) {
                path.push(s.Y * 10 + s.X);
                return path;
            }
        }
        return null;
    }

    /**
     * Search the parents of a given action to find a path to the starting point.
     * @param a Action
     * @return Stack of agent locations corresponding to a valid path from the starting point. Null if path not found.
     */
    Stack<Integer> traceback(action a) {
        for (state s : a.parents) {
            Stack<Integer> path = traceback(s);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    /**
     * Check if a given position is a valid move
     * @param x X coordinate
     * @param y Y coordinate
     * @return If position is unobstructed
     */
    public static boolean isClear(byte x, byte y) {
        try {
            return (map[x][y] < 3);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
