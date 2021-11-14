import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class State implements Comparable<State> {
    private int missionariesLeft;
    private int cannibalsLeft;
    private int missionariesRight;
    private int cannibalsRight;

    boolean boatRight;
    static int capacity;
    private int maxCrosses;

    private State father = null;

    //heuristic score
    private int score;

    //----
    //distance from root
    private int depth;

    State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight, int maxCrosses, int depth) {
        this.missionariesLeft = missionariesLeft;
        this.cannibalsLeft = cannibalsLeft;
        this.missionariesRight = missionariesRight;
        this.cannibalsRight = cannibalsRight;
        this.boatRight = boatRight;


        this.maxCrosses = maxCrosses;

        this.depth = depth;
    }


    void print() {
        System.out.println("-----------------------");
        System.out.println("Missionaries left: " + missionariesLeft);
        System.out.println("Cannibals left: " + cannibalsLeft);
        System.out.println("Missionaries right: " + missionariesRight);
        System.out.println("Cannibals right: " + cannibalsRight);
        System.out.println("-----------------------");
    }


//-----
    ArrayList<State> getChildren(int heuristic) {
        ArrayList<State> children = new ArrayList<>();
        State child;
        //when boat is left and want to go right calculate all possible children
        if (!boatRight) {
                for (int i = 0; i <= this.missionariesLeft; i++) {
                    for (int j = 0; j <= this.cannibalsLeft; j++) {
                        if ( ((i + j) <= capacity) && ((j + i) > 0)) {  // else mhn kaneis child
                            if (capacity>2 && (j>i && i != 0)) continue;
                            //this is the point where it makes the i-th child
                            int newCannibalsLeft = this.cannibalsLeft - j;
                            int newCannibalsRight = this.cannibalsRight + j;
                            int newMissionariesLeft = this.missionariesLeft - i;
                            int newMissionariesRight = this.missionariesRight + i;
                            boolean newBoatRight = true;
                            int newMaxCrosses = maxCrosses - 1;
                            int newdepth = depth +1;
                            child = new State(newMissionariesLeft, newCannibalsLeft, newMissionariesRight, newCannibalsRight, newBoatRight, newMaxCrosses, newdepth);
                            if (heuristic > 0) {
                                child.evaluate(heuristic);
                            }
                            if (maxCrosses < 0)
                            {
                                System.out.println("The number of crosses is bigger than the desirable.");
                                return children;
                            }
                            if (child.isAcceptable()) {
                                children.add(child);
                                child.setFather(this);
                            }
                        }
                    }
                }
            }
        //when boat is right and want to go left
        if (boatRight) {
            for (int i = 0; i <= this.missionariesRight; i++) {
                for (int j = 0; j <= this.cannibalsRight; j++) {
                    if (((i + j) <= capacity) && ((j + i)>0)) {  // else mhn kaneis child
                        if (capacity>2 && (j > i && i != 0)) continue;
                        //this is the point where it makes the i-th child
                        int newCannibalsLeft = this.cannibalsLeft + j;
                        int newCannibalsRight = this.cannibalsRight - j;
                        int newMissionariesLeft = this.missionariesLeft + i;
                        int newMissionariesRight = this.missionariesRight - i;
                        boolean newBoatRight = false;
                        int newMaxCrosses = maxCrosses - 1;
                        int newdepth = depth +1;
                        child = new State(newMissionariesLeft, newCannibalsLeft, newMissionariesRight, newCannibalsRight, newBoatRight, newMaxCrosses, newdepth);
                        if (heuristic > 0) {
                        child.evaluate(heuristic);
                        }
                        if (maxCrosses < 0)
                        {
                            System.out.println("The number of crosses is bigger than the desirable.");
                            return children;
                        }
                        if (child.isAcceptable()) {
                            children.add(child);
                            child.setFather(this);
                        }
                    }
                }
              }
            }
        return children;
    }

    //euristic 1
    private void moreCannibals() {
        if (boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + (2 * (missionariesLeft + cannibalsLeft)) + this.depth; //+score
            //G = G + depth(this);
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) == 1) {
            score = score + 1 + this.depth;
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) > 1) {
            score = score + (2 * (missionariesLeft + cannibalsLeft) - 3) + this.depth;
        } else if ((missionariesLeft + cannibalsLeft) == 0) {
            score = score + 0 + this.depth;
        }
    }

    //euristic 2
    private void moreThanTwoPeople() {
        if (boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + 2 + this.depth;
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + 1 + this.depth;
        } else if ((missionariesLeft + cannibalsLeft) == 0) {
            score = score + 0 + this.depth;
        }
    }

    public boolean isAcceptable() {
        if (missionariesLeft >= 0 && missionariesRight >= 0 && cannibalsLeft >= 0 && cannibalsRight >= 0
                && (missionariesLeft == 0 || missionariesLeft >= cannibalsLeft)
                && (missionariesRight == 0 || missionariesRight >= cannibalsRight) ) {
            return true;
        }
        return false;
    }

    public boolean isFinal() {
        return (missionariesLeft == 0 && cannibalsLeft == 0) ;
    }

    private void evaluate(int heuristic) {
        switch (heuristic) {
            case 1:
                this.moreCannibals();
                break;
            case 2:
                this.moreThanTwoPeople();
                break;
            default:
                break;
        }
    }

    @Override
    public int compareTo(State s) {
        return Double.compare(this.score, s.score); // compare based on the heuristic score.
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }
        State s = (State) obj;
        return (s.cannibalsLeft == cannibalsLeft && s.missionariesLeft == missionariesLeft
                && s.boatRight == boatRight && s.cannibalsRight == cannibalsRight
                && s.missionariesRight == missionariesRight);
    }

    // override this for proper hash set comparisons.
    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.cannibalsLeft, this.missionariesLeft, this.cannibalsRight, this.cannibalsRight, this.boatRight);
    }

    int identifier() {
        int result = (int) Math.pow(this.missionariesRight, (this.missionariesRight * this.score) + this.score) * this.cannibalsRight;

        return result;
    }


    public State getFather() {
        return father;
    }

    public void setFather(State father) {
        this.father = father;

    }


    public int getMissionariesLeft() {
        return missionariesLeft;
    }

    public void setMissionariesLeft(int missionariesLeft) {
        this.missionariesLeft = missionariesLeft;
    }

    public int getCannibalsLeft() {
        return cannibalsLeft;
    }

    public void setCannibalsLeft(int cannibalsLeft) {
        this.cannibalsLeft = cannibalsLeft;
    }

    public int getMissionariesRight() {
        return missionariesRight;
    }

    public void setMissionariesRight(int missionariesRight) {
        this.missionariesRight = missionariesRight;
    }

    public int getCannibalsRight() {
        return cannibalsRight;
    }

    public void setCannibalsRight(int cannibalsRight) {
        this.cannibalsRight = cannibalsRight;
    }

    public boolean isBoatRight() {
        return boatRight;
    }

    public void setBoatRight(boolean boatRight) {
        this.boatRight = boatRight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getMaxCrosses() {
        return maxCrosses;
    }

    public void setMaxCrosses(int maxCrosses) {
        this.maxCrosses = maxCrosses;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        depth = this.depth;
    }

}

