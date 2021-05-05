import java.util.ArrayList;

public class action {
    public ArrayList<state> parents;
    public state[] outcomes;
    public boolean success;

    /**
     * Constructor for empty actions
     * @param p parent state
     */
    action(state p) {
        parents = new ArrayList<>();
        parents.add(p);
        success = true;
        outcomes = new state[]{p}; //the outcome is just the parent again
    }

    /**
     * Constructor for movement actions
     * @param p     parent state
     * @param plane is the movement action on the X or Y plane
     * @param arg1  first atom/argument
     * @param arg2  second atom/argument
     * @param arg3  third atom/argument
     */
    action(state p, String plane, byte arg1, byte arg2, byte arg3) {
        parents = new ArrayList<>();
        parents.add(p);

        switch (plane) {
            case "X-AXIS" -> success = moveX(arg1, arg2, arg3);
            case "Y-AXIS" -> success = moveY(arg1, arg2, arg3);
            default -> {
                System.out.println("Not recognized");
                success = false;
            }
        }
    }

    private boolean moveX(byte x1, byte y1, byte x2) {
        //if movement is 1 space, current position is correct, and path is clear
        if (Math.abs(x1 - x2) == 1 && parents.get(0).isEqual(x1, y1) && graphMaster.map[x2][y1] < 3) {
            outcomes = new state[2];
            outcomes[0] = new state(this, true, x2, y1); //at(x2,y1)
            outcomes[1] = new state(this, false, x1, y1); //!at(x1,y1)
            return true;
        }
        return false;
    }

    private boolean moveY(byte x1, byte y1, byte y2) {
        //if movement is 1 space, current position is correct, and path is clear
        if (Math.abs(y1 - y2) == 1 && parents.get(0).isEqual(x1, y1) && graphMaster.map[x1][y2] < 3) {
            outcomes = new state[2];
            outcomes[0] = new state(this, true, x1, y2); //at(x1,y2)
            outcomes[1] = new state(this, false, x1, y1); //!at(x1,y1)
            return true;
        }
        return false;
    }
}
