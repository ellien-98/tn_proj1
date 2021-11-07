import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class State implements Comparable<State>
{
    private int missionariesLeft;
    private int cannibalsLeft;
    private int missionariesRight;
    private int cannibalsRight;

    boolean boatRight;
    private int capacity;
    private int maxCrosses;
    //private int crosses;

    //private int crossesLeft;

    private int randmiss;
    private int randcans;
    
    //private int movemissleft;
    
    private State father = null;
    State left;
    State right;

    //heuristic score
    private int score;
    
    

    //distance from root
    private int G;

    State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight, int capacity, int maxCrosses)
    {
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

    /*
        State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight)
        {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.missionariesRight = missionariesRight;
            this.cannibalsRight = cannibalsRight;
            this.boatRight = boatRight;
        }

        State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight, int crossesLeft)
        {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.missionariesRight = missionariesRight;
            this.cannibalsRight = cannibalsRight;
            this.boatRight = boatRight;
            this.crossesLeft = crossesLeft;

        }
    */
    void print()
    {
        System.out.println("-----------------------");
        System.out.println("Missionaries left: " + missionariesLeft);
        System.out.println("Cannibals left: " + cannibalsLeft);
        System.out.println("Missionaries right: " + missionariesRight);
        System.out.println("Cannibals right: " + cannibalsRight);
        System.out.println("-----------------------");
    }

    ArrayList<State> getChildren (int heuristic)
    {

        ArrayList<State> children = new ArrayList<>();
    	State child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity, this.maxCrosses);
        //if (maxCrosses != 0) {
    		//when boat is left and want to go right 
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

    private void addToList(List<State> children, State child) {

		//if (child.isAcceptable()) {
            children.add(child);
            child.setFather(this);
		//}
	}



    //calculate the random number of missionaries that will move right
    private boolean randCrosstoRight()
    {
        Random rand = new Random();

        if (boatRight) return false;
        //while no missionary and no cannibal has been chosen to move right
        while(randmiss+randcans == 0) { // || !this.isAcceptable()
            //calculate the number of missionaries that will cross the river
            if(this.missionariesLeft <= this.capacity) {	//if missionaries left are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randmiss = this.capacity - rand.nextInt(this.missionariesLeft+1);		//from 0 (no missionary) up to number of missionaries
            }
            else {	//if missionaries=5 and capacity=3
                randmiss = rand.nextInt(this.capacity+1);	//from 0 up to capacity
            }
            missionariesLeft = this.missionariesLeft-randmiss;
            missionariesRight = this.missionariesRight + randmiss;

            //calculate the number of cannibals that will cross
            if(this.cannibalsLeft <= (capacity - randmiss)) {	//if cannibals Left are less than the capacity of the boat minus the number of missionaries on the boat already
                randcans = capacity - randmiss - rand.nextInt(this.cannibalsLeft + 1);
            }
            else {	//if cannibals=5 and capacity=3 and missionaries on boat=1
                randcans = rand.nextInt(this.capacity - randmiss + 1);
            }
            cannibalsLeft = this.cannibalsLeft - randcans;
            cannibalsRight = this.cannibalsRight + randcans;

            boatRight = true;

        }

        maxCrosses = this.maxCrosses  - 1;

    return true;
    }

    private boolean randCrosstoLeft()
    {
        Random rand = new Random();

        if (!boatRight) return false;

        while(randmiss + randcans == 0) { // || !this.isAcceptable()
            if(this.missionariesRight <= capacity) {	//if missionaries right are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randmiss = this.capacity - rand.nextInt(this.missionariesRight + 1);		//from 0 (no missionary) up to number of missionaries(+1 is necessary)
            }
            else {	//if missionaries=5 and capacity=3
                randmiss = rand.nextInt(this.capacity+1);	//from 0 up to capacity
            }
            missionariesRight = this.missionariesRight - randmiss;
            missionariesLeft = this.missionariesLeft + randmiss;

            if(this.cannibalsRight <= this.capacity) {	//if cannibals right are less than the capacity of the boat e.g. 8 miss, 10 capacity
                randcans = this.capacity - rand.nextInt(this.cannibalsRight + 1);		//from 0 (no cannibals) up to number of cannibals(+1 is necessary)
            }
            else {	//if missionaries=5 and capacity=3
                randcans = rand.nextInt(this.capacity - randmiss + 1);	//from 0 up to capacity minus the number of missionaries already on boat
            }
            cannibalsRight = this.cannibalsRight - randcans;
            cannibalsLeft = this.cannibalsLeft + randcans;

        }
        boatRight = false;

        maxCrosses=this.maxCrosses  - 1;

        return true;

    }
    public int depth(State n)
    {
        G = 0;
        ArrayList<State> path = new ArrayList<>();
        while (n.getFather() != null)
        {

            path.add(n.getFather());
            n = n.getFather();
            G++;
        }
        return G;
    }
    
    //euristic 1
    private void moreCannibals ()
    {
        if(boatRight && (missionariesLeft+cannibalsLeft)>0)
        {
            score = score + (2 * (missionariesLeft+cannibalsLeft)) + depth(this);
            //G = G + depth(this);
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)==1)
        {
            score = score + 1 + depth(this);
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)>1)
        {
            score = score + (2 * (missionariesLeft+cannibalsLeft)-3) + depth(this);
        }
        else if ((missionariesLeft+cannibalsLeft)==0)
        {
            score = score + 0 + depth(this);
        }
    }
    
    //euristic 2
    private void moreThanTwoPeople()
    {
        if(boatRight && (missionariesLeft+cannibalsLeft) > 0)
        {
            score = score + 2 + depth(this);
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)>0)
        {
            score = score + 1 + depth(this);
        }
        else if ((missionariesLeft+cannibalsLeft)==0)
        {
            score = score +0 + depth(this);
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
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public boolean isFinal()
    {

        return missionariesLeft == 0 && cannibalsLeft == 0;
    }

    public boolean overMaxCrosses()
    {

        if(this.maxCrosses == 0) {
            System.out.println("The number of crosses is bigger than the desirable ");
            //return true;
        }
        return  this.maxCrosses == 0;

    }

    private void evaluate (int heuristic)
    {
        switch(heuristic)
        {
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
    public int compareTo(State s)
    {
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
    public int hashCode()
    {
    	return java.util.Objects.hash(this.cannibalsLeft, this.missionariesLeft, this.cannibalsRight, this.cannibalsRight, this.boatRight);
    	
        //return this.missionariesLeft + this.missionariesRight + this.cannibalsLeft + this.cannibalsRight + this.identifier();
    }

    int identifier()
    {
        //int result = 0;
	// a unique sum based on the numbers in each state.
        // e.g., for i=j=0 in the fixed initial state --> 3^( (0*0) + 0) * 8 = 1 + 8 = 9
        // for another state, this will not be the same
        //result += Math.pow(this.missionariesRight, (this.missionariesRight + this.cannibalsRight) + this.cannibalsRight);
	//return result;
	int result = (int)Math.pow(this.missionariesRight, (this.missionariesRight * this.score) + this.score) * this.cannibalsRight;

        return result;
    }
    
    
/*
    // override this for proper hash set comparisons.
    @Override
    public boolean equals(Object obj)
    {
        if(this.missionariesLeft != ((State)obj).missionariesLeft) return false;
        if(this.missionariesRight != ((State)obj).missionariesRight) return false;
        if(this.cannibalsLeft != ((State)obj).cannibalsLeft) return false;
        if(this.cannibalsRight != ((State)obj).cannibalsRight) return false;

        if(this.boatRight != ((State)obj).boatRight) return false;

        return true;
    }

    // override this for proper hash set comparisons.
    @Override
    public int hashCode()
    {
        return this.missionariesLeft + this.missionariesRight + this.cannibalsLeft + this.cannibalsRight + identifier();
    }

    int identifier()
    {
        int result = 0;

                // a unique sum based on the numbers in each state.
                // e.g., for i=j=0 in the fixed initial state --> 3^( (0*0) + 0) * 8 = 1 + 8 = 9
                // for another state, this will not be the same
         result += Math.pow(this.missionariesRight, (this.missionariesRight * this.cannibalsLeft) + this.cannibalsRight) * this.missionariesLeft;

        return result;
    }


 boolean missionaryRight()
    {
        if (missionariesLeft == 0) return false;
        this.missionariesLeft--;
        this.missionariesRight++;
        boatRight = true;
        return true;
    }

    boolean twoMissionariesRight()
    {
        if (missionariesLeft == 0 || missionariesLeft == 1) return false;
        this.missionariesLeft = this.missionariesLeft - 2;
        this.missionariesRight = this.missionariesRight + 2;
        boatRight = true;
        return true;
    }

    boolean cannibalRight()
    {
        if (cannibalsLeft == 0) return false;
        this.cannibalsLeft--;
        this.cannibalsRight++;
        boatRight = true;
        return true;
    }

    boolean twoCannibalsRight()
    {
        if (cannibalsLeft == 0 || cannibalsLeft == 1) return false;
        this.cannibalsLeft = this.cannibalsLeft - 2;
        this.cannibalsRight = this.cannibalsRight + 2;
        boatRight = true;
        return true;
    }

    boolean missionaryCannibalRight()
    {
        if (cannibalsLeft == 0 || missionariesLeft == 0) return false;
        this.cannibalsLeft--;
        this.missionariesLeft--;
        this.cannibalsRight++;
        this.missionariesRight++;
        boatRight = true;
        return true;
    }

    boolean missionaryLeft()
    {
        if (missionariesRight == 0) return false;
        this.missionariesRight--;
        this.missionariesLeft++;
        boatRight = false;
        return true;
    }

    boolean twoMissionariesLeft()
    {
        if (missionariesRight == 0 || missionariesRight == 1) return false;
        this.missionariesRight = this.missionariesRight - 2;
        this.missionariesLeft = this.missionariesLeft + 2;
        boatRight = false;
        return true;
    }

    boolean cannibalLeft()
    {
        if (cannibalsRight == 0) return false;
        this.cannibalsRight++;
        this.cannibalsLeft++;
        boatRight = false;
        return true;
    }

    boolean twoCannibalsLeft()
    {
        if (cannibalsRight == 0 || cannibalsRight == 1) return false;
        this.cannibalsRight = this.cannibalsRight - 2;
        this.cannibalsLeft = this.cannibalsLeft + 2;
        boatRight = false;
        return true;
    }

    boolean missionaryCannibalLeft()
    {
        if (cannibalsRight == 0 || missionariesRight == 0) return false;
        this.cannibalsRight--;
        this.missionariesRight--;
        this.cannibalsLeft++;
        this.missionariesLeft++;
        boatRight = false;
        return true;
    }
    int identifier()
    {
        int result = 0;
        for(int i = 0; i < this.dimension; i++)
        {
            for(int j = 0; j < this.dimension; j++)
            {
                // a unique sum based on the numbers in each state.
                // e.g., for i=j=0 in the fixed initial state --> 3^( (0*0) + 0) * 8 = 1 + 8 = 9
                // for another state, this will not be the same
                result += Math.pow(this.dimension, (this.dimension * i) + j) * this.tiles[i][j];
            }
        }
        return result;
    }
}

    // override this for proper hash set comparisons.
    @Override
    public boolean equals(Object obj)
    {
        if(this.dimension != ((State)obj).dimension) return false;
        if(this.emptyTileRow != ((State)obj).emptyTileRow) return false;
        if(this.emptyTileColumn != ((State)obj).emptyTileColumn) return false;

        // check for equality of numbers in the tiles.
        for(int i = 0; i < this.dimension; i++)
        {
            for(int j = 0; j < this.dimension; j++)
            {
                if(this.tiles[i][j] != ((State)obj).tiles[i][j])
                {
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public int hashCode()
    {
        return this.emptyTileRow + this.emptyTileColumn + this.dimension + this.identifier();
    }
*/
   
    /*
    int identifier()
    {
        int result = 0;

        // a unique sum based on the numbers in each state.
        // e.g., for i=j=0 in the fixed initial state --> 3^( (0*0) + 0) * 8 = 1 + 8 = 9
        // for another state, this will not be the same
        result += Math.pow(this.dimension, (this.dimension * i) + j) * this.tiles[i][j];

        return result;
    }
*/
    
    /*
    int depth(State current) // (..., int x)
    {
        // Base case
        if (current == null)
            return -1;
        // Initialize distance as -1
        int dist = -1;
        // Check if x is current node=
        if ((current == this)|| (dist = depth(current.left)) >= 0 || (dist = depth(current.right)) >= 0)

                // Otherwise, check if x is
                // present in the left subtree


                // Otherwise, check if x is
                // present in the right subtree


            // Return depth of the node
            return dist + 1;

        return dist;
    }
    
    public int depth(State n)
    {
        if(n == null)
            return 0;
        return 1 + Math.max(depth(n.left), depth(n.right));

    }
*/
/*  
    ArrayList<State> getChildren (int heuristic)
    {

        ArrayList<State> children = new ArrayList<>();
        //State root = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);


            State child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.missionaryLeft() && boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.twoMissionariesLeft()&& boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.cannibalLeft()&& boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.twoCannibalsLeft()&& boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.missionaryCannibalLeft()&& boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }

            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.missionaryRight()&& !boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.twoMissionariesRight() && !boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.cannibalRight() && !boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.twoCannibalsRight() && !boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);
            }
            child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
            if (child.missionaryCannibalRight() && !boatRight) {
                if (heuristic > 0) child.evaluate(heuristic);
                children.add(child);
                child.setFather(this);

            }

        return children;
    }
  */  
    
}
