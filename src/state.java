import java.util.ArrayList;
import java.util.Arrays;

public class state {
    public byte X, Y;
    public boolean isNegated;
    public action[] actions;
    public ArrayList<action> parents;

    /**
     * Constructor
     * @param p      parent
     * @param isTrue indicate whether or not state is negated
     * @param x      X coordinate
     * @param y      Y coordinate
     */
    state(action p, boolean isTrue, byte x, byte y) {
        parents = new ArrayList<>();
        parents.add(p);
        X = x;
        Y = y;
        isNegated = !isTrue;
    }

    public action[] generateActions() {
        byte actionCount = 0;
        action[] act = new action[5];
        action temp;

        act[actionCount] = new action(this); //Empty action
        actionCount++;

        if (graphMaster.isClear((byte) (X - 1), Y)) { //Move left
            temp = new action(this, "X-AXIS", X, Y, (byte) (X - 1));
            if (temp.success) {
                act[actionCount] = temp;
                actionCount++;
            }
        }
        if (graphMaster.isClear((byte) (X + 1), Y)) { //Move right
            temp = new action(this, "X-AXIS", X, Y, (byte) (X + 1));
            if (temp.success) {
                act[actionCount] = temp;
                actionCount++;
            }
        }
        if (graphMaster.isClear(X, (byte) (Y - 1))) { //Move up
            temp = new action(this, "Y-AXIS", X, Y, (byte) (Y - 1));
            if (temp.success) {
                act[actionCount] = temp;
                actionCount++;
            }
        }
        if (graphMaster.isClear(X, (byte) (Y + 1))) { //Move down
            temp = new action(this, "Y-AXIS", X, Y, (byte) (Y + 1));
            if (temp.success) {
                act[actionCount] = temp;
                actionCount++;
            }
        }

        return Arrays.copyOfRange(act, 0, actionCount);
    }

    public boolean isEqual(byte X1, byte Y1) {
        return (X == X1 && Y == Y1);
    }
}
