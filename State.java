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
    private int capacity;
    private int maxCrosses;
    //private int crosses;

    //private int crossesLeft;

    //-----arxikopoihsh gia na mhn yparxei provlhma sth synarthsh
    private int randmiss = 0;
    private int randcans = 0;

    //private int movemissleft;

    private State father = null;
    State left;
    State right;

    //heuristic score
    private int score;

    //----
    //distance from root
    private int g;

    State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight, int capacity, int maxCrosses) {
        this.missionariesLeft = missionariesLeft;
        this.cannibalsLeft = cannibalsLeft;
        this.missionariesRight = missionariesRight;
        this.cannibalsRight = cannibalsRight;
        this.boatRight = boatRight;

        this.capacity = capacity;
        this.maxCrosses = maxCrosses;
    }

    public int getMissionariesLeft() {
        return missionariesLeft;
    }

    public void setMissionariesLeft(int missionariesLeft) {
        this.missionariesLeft = missionariesLeft;
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
        Random rand = new Random();
        State child;
        //when boat is left and want to go right calculate all possible children
        if (!boatRight) {
            while ((randmiss + randcans) == 0) {
                //calculate the number of missionaries that will cross the river
                if (this.missionariesLeft <= this.capacity) {    //if missionaries left are less than the capacity of the boat e.g. 8 miss, 10 capacity
                    //we must calculate all potential crosses and make them childs
                    for(int i = 1; i <= this.missionariesLeft; i++){
                        randmiss = i;

                        for(int j=1; j<=this.cannibalsLeft; j++){
                            if((j+randmiss) <= this.capacity){  // else mhn kaneis child
                                //this is the point where it makes the i-th child
                                child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
                                randcans = j;
                                child.cannibalsLeft = this.cannibalsLeft - randcans;
                                child.cannibalsRight = this.cannibalsRight + randcans;
                                child.missionariesLeft = this.missionariesLeft - randmiss;
                                child.missionariesRight = this.missionariesRight + randmiss;
                                if (heuristic > 0) {
                                    child.evaluate(heuristic);
                                }
                                if (child.isAcceptable()) {
                                    children.add(child);
                                    child.setFather(this);
                                    boatRight = true;
                                }
                            }
                        }
                    }
                }
                else if (this.missionariesLeft > this.capacity){    //if missionaries=5 and capacity=3
                    for(int i = 1; i <= this.capacity; i++){
                        randmiss = i;
                        for(int j=1; j<=this.cannibalsLeft; j++){
                            if((j+randmiss) <= this.capacity){  // else mhn kaneis child
                                //this is the point where it makes the i-th child
                                child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
                                randcans = j;
                                child.cannibalsLeft = this.cannibalsLeft - randcans;
                                child.cannibalsRight = this.cannibalsRight + randcans;
                                child.missionariesLeft = this.missionariesLeft - randmiss;
                                child.missionariesRight = this.missionariesRight + randmiss;
                                if (heuristic > 0) {
                                    child.evaluate(heuristic);
                                }
                                if (child.isAcceptable()) {
                                    children.add(child);
                                    child.setFather(this);
                                    boatRight = true;
                                }
                            }
                        }
                    }
                }
            }

        }
        //when boat is right and want to go left
        if (boatRight) {
            while ((randmiss + randcans) == 0) {
                //calculate the number of missionaries that will cross the river
                if (this.missionariesRight <= capacity) {    //if missionaries left are less than the capacity of the boat e.g. 8 miss, 10 capacity
                    //we must calculate all potential crosses and make them childs
                    for(int i = 1; i <= this.missionariesRight; i++){
                        randmiss = i;
                        for(int j=1; j<=this.cannibalsRight; j++){
                            if((j+randmiss) <= this.capacity){  // else mhn kaneis child
                                //this is the point where it makes the ij-th child
                                child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
                                randcans = j;
                                child.cannibalsLeft = this.cannibalsLeft + randcans;
                                child.cannibalsRight = this.cannibalsRight - randcans;
                                child.missionariesLeft = this.missionariesLeft + randmiss;
                                child.missionariesRight = this.missionariesRight - randmiss;
                                if (heuristic > 0) {
                                    child.evaluate(heuristic);
                                }
                                if (child.isAcceptable()) {
                                    children.add(child);
                                    child.setFather(this);
                                    boatRight = false;
                                }
                            }
                        }
                    }
                }
                else if (this.missionariesLeft > this.capacity){    //if missionaries=5 and capacity=3
                    for(int i = 1; i <= this.capacity; i++){
                        randmiss = i;
                        for(int j=1; j<=this.cannibalsLeft; j++){
                            if((j+randmiss) <= this.capacity){  // else mhn kaneis child
                                //this is the point where it makes the i-th child
                                child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
                                randcans = j;
                                child.cannibalsLeft = this.cannibalsLeft - randcans;
                                child.cannibalsRight = this.cannibalsRight + randcans;
                                child.missionariesLeft = this.missionariesLeft - randmiss;
                                child.missionariesRight = this.missionariesRight + randmiss;
                                if (heuristic > 0) {
                                    child.evaluate(heuristic);
                                }
                                if (child.isAcceptable()) {
                                    children.add(child);
                                    child.setFather(this);
                                    boatRight = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return children;
    }

    private void addToList(List<State> children, State child) {

        //if (child.isAcceptable()) {
        children.add(child);
        child.setFather(this);
        //}
    }

    //-----
    //calculate the random number of missionaries that will move right
    private boolean randCrosstoRight() {
        //here we check if it can go right, now it is left
        if (boatRight) return false;
        Random rand = new Random();
        //while no missionary and no cannibal has been chosen to move right
        while (randmiss + randcans == 0) { // || !this.isAcceptable()
            //calculate the number of missionaries that will cross the river
            if (this.missionariesLeft <= this.capacity) {    //if missionaries left are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randmiss = this.capacity - rand.nextInt(this.missionariesLeft + 1);        //from 0 (no missionary) up to number of missionaries
            } else {    //if missionaries=5 and capacity=3
                randmiss = rand.nextInt(this.capacity + 1);    //from 0 up to capacity
            }
            missionariesLeft = this.missionariesLeft - randmiss;
            missionariesRight = this.missionariesRight + randmiss;

            //calculate the number of cannibals that will cross
            if (this.cannibalsLeft <= (capacity - randmiss)) {    //if cannibals Left are less than the capacity of the boat minus the number of missionaries on the boat already
                randcans = capacity - randmiss - rand.nextInt(this.cannibalsLeft + 1);
            } else {    //if cannibals=5 and capacity=3 and missionaries on boat=1
                randcans = rand.nextInt(this.capacity - randmiss + 1);
            }
            cannibalsLeft = this.cannibalsLeft - randcans;
            cannibalsRight = this.cannibalsRight + randcans;

            boatRight = true;

        }

        maxCrosses = this.maxCrosses - 1;

        return true;
    }
    
    private boolean randCrosstoLeft() {
        Random rand = new Random();

        if (!boatRight) return false;

        while (randmiss + randcans == 0) { // || !this.isAcceptable()
            if (this.missionariesRight <= capacity) {    //if missionaries right are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randmiss = this.capacity - rand.nextInt(this.missionariesRight + 1);        //from 0 (no missionary) up to number of missionaries(+1 is necessary)
            } else {    //if missionaries=5 and capacity=3
                randmiss = rand.nextInt(this.capacity + 1);    //from 0 up to capacity
            }
            missionariesRight = this.missionariesRight - randmiss;
            missionariesLeft = this.missionariesLeft + randmiss;

            if (this.cannibalsRight <= this.capacity) {    //if cannibals right are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randcans = this.capacity - rand.nextInt(this.cannibalsRight + 1);        //from 0 (no cannibals) up to number of cannibals(+1 is necessary)
            } else {    //if missionaries=5 and capacity=3
                randcans = rand.nextInt(this.capacity - randmiss + 1);    //from 0 up to capacity minus the number of missionaries already on boat
            }
            cannibalsRight = this.cannibalsRight - randcans;
            cannibalsLeft = this.cannibalsLeft + randcans;

        }
        boatRight = false;

        maxCrosses = this.maxCrosses - 1;

        return true;

    }
    //---
    public int depth(State n) {
        if(n == null){
            return g;
        }
        else{
            //g = depth(n.getFather());
            return g=depth(n.getFather()) + 1;
        }
    }

    //euristic 1
    private void moreCannibals() {
        if (boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + (2 * (missionariesLeft + cannibalsLeft)) + depth(this);
            //G = G + depth(this);
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) == 1) {
            score = score + 1 + depth(this);
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) > 1) {
            score = score + (2 * (missionariesLeft + cannibalsLeft) - 3) + depth(this);
        } else if ((missionariesLeft + cannibalsLeft) == 0) {
            score = score + 0 + depth(this);
        }
    }

    //euristic 2
    private void moreThanTwoPeople() {
        if (boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + 2 + depth(this);
        } else if (!boatRight && (missionariesLeft + cannibalsLeft) > 0) {
            score = score + 1 + depth(this);
        } else if ((missionariesLeft + cannibalsLeft) == 0) {
            score = score + 0 + depth(this);
        }
    }

    public boolean isAcceptable() {
        if (missionariesLeft >= 0 && missionariesRight >= 0 && cannibalsLeft >= 0 && cannibalsRight >= 0
                && (missionariesLeft == 0 || missionariesLeft >= cannibalsLeft)
                && (missionariesRight == 0 || missionariesRight >= cannibalsRight)) {
            return true;
        }

        return false;
    }


    public boolean isFinal() {

        return missionariesLeft == 0 && cannibalsLeft == 0;
    }

    public boolean overMaxCrosses() {

        if (this.maxCrosses == 0) {
            System.out.println("The number of crosses is bigger than the desirable ");
            //return true;
        }
        return this.maxCrosses == 0;

    }

    private void evaluate(int heuristic) {
        switch (heuristic) {
            case 1:
                this.moreThanTwoPeople();
                break;
            case 2:
                this.moreCannibals();
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

        //return this.missionariesLeft + this.missionariesRight + this.cannibalsLeft + this.cannibalsRight + this.identifier();
    }

    int identifier() {
        //int result = 0;
        // a unique sum based on the numbers in each state.
        // e.g., for i=j=0 in the fixed initial state --> 3^( (0*0) + 0) * 8 = 1 + 8 = 9
        // for another state, this will not be the same
        //result += Math.pow(this.missionariesRight, (this.missionariesRight + this.cannibalsRight) + this.cannibalsRight);
        //return result;
        int result = (int) Math.pow(this.missionariesRight, (this.missionariesRight * this.score) + this.score) * this.cannibalsRight;

        return result;
    }


    public State getFather() {
        return father;
    }

    public void setFather(State father) {
        this.father = father;

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

    public int getRandmiss() {
        return randmiss;
    }

    public void setRandmiss(int randmiss) {
        this.randmiss = randmiss;
    }

    public int getRandcans() {
        return randcans;
    }

    public void setRandcans(int randcans) {
        this.randcans = randcans;
    }

    public State getLeft() {
        return left;
    }

    public void setLeft(State left) {
        this.left = left;
    }

    public State getRight() {
        return right;
    }

    public void setRight(State right) {
        this.right = right;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        g = this.g;
    }

}


/* ---OLD getChildren
    ArrayList<State> getChildren(int heuristic) {

        ArrayList<State> children = new ArrayList<>();
        //if (maxCrosses != 0) {
        //child for when boat is left and want to go right

        State child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
        if (child.randCrosstoRight()) {
            if (heuristic > 0)
                child.evaluate(heuristic);
            if (child.isAcceptable()) {
                children.add(child);
                child.setFather(this);
            }
            //else{
            // children.add(this);
            //}
        }
        //when boat is right and want to go left
        child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
        if (child.randCrosstoLeft()) {
            if (heuristic > 0)
                child.evaluate(heuristic);
            if (child.isAcceptable()) {
                children.add(child);
                child.setFather(this);
            }
            //else{
            //children.add(this);
            //}
        }
        //}
        //else{
        //System.out.print("Over max crosses");
        //}

        return children;
    }
END OLD    */
