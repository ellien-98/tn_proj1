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
    private int crosses;

    private int randmiss;
    private int randcans;
    
    //private int movemissleft;
    
    private State father = null;
    State left;
    State right;

    //heuristic score
    private int score;
    
    

    //apostasi apo thn riza mexri thn twrini katastasi
    //private int G;

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
    
    State(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight, boolean boatRight)
    {
        this.missionariesLeft = missionariesLeft;
        this.cannibalsLeft = cannibalsLeft;
        this.missionariesRight = missionariesRight;
        this.cannibalsRight = cannibalsRight;
        this.boatRight = boatRight;       
    }

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
        //State root = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight);
        State child = new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, this.boatRight, this.capacity , this.crosses);
            if (!boatRight) {
            	child.randCrosstoRight();
    			addToList(children, new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, boatRight)); // Two missionaries cross left to right.
    			//addToList(child, new State(missionariesLeft, cannibalsLeft-2, missionariesRight, cannibalsRight+2, boatRight)); // Two cannibals cross left to right.
    			//addToList(child, new State(missionariesLeft - 1, cannibalsLeft -1, missionariesRight + 1, cannibalsRight+1, boatRight)); // One missionary and one cannibal cross left to right.
    			//addToList(child, new State(missionariesLeft - 1, cannibalsLeft, missionariesRight + 1, cannibalsRight, boatRight)); // One missionary crosses left to right.
    			//addToList(child, new State(missionariesLeft, cannibalsLeft-1, missionariesRight, cannibalsRight+1, boatRight)); // One cannibal crosses left to right.
    		} else {
    			child.randCrosstoLeft();
    			addToList(children, new State(this.missionariesLeft, this.cannibalsLeft, this.missionariesRight, this.cannibalsRight, boatRight)); // Two missionaries cross right to left.
    			//addToList(child, new State(cannibalsLeft + 2, missionariesLeft, !boatRight,
    					//cannibalsRight - 2, missionariesRight)); // Two cannibals cross right to left.
    			//addToList(child, new State(cannibalsLeft + 1, missionariesLeft + 1, !boatRight,
    					//cannibalsRight - 1, missionariesRight - 1)); // One missionary and one cannibal cross right to left.
    			//addToList(child, new State(cannibalsLeft, missionariesLeft + 1, !boatRight,
    					//cannibalsRight, missionariesRight - 1)); // One missionary crosses right to left.
    			//addToList(child, new State(cannibalsLeft + 1, missionariesLeft, !boatRight,
    					//cannibalsRight - 1, missionariesRight)); // One cannibal crosses right to left.
    		}

        return children;
    }

    private void addToList(List<State> children, State aState) {
		if (aState.isAcceptable()) {
			aState.setFather(this);
			children.add(aState);
		}
	}
    
    
    
    //calculate the random number of missionaries & cannibals that will move to the right
    private void randCrosstoRight()
    {
        Random rand = new Random();
        //while no missionary and no cannibal has been chosen to move right
        while(this.randmiss+this.randcans == 0) {
        	//calculate the number of missionaries that will cross the river
        	if(missionariesLeft <= capacity) {	//if missionaries left are less than the capacity of the boat e.g. 8 miss, 10 capacity
        		this.randmiss = capacity - rand.nextInt(missionariesLeft+1);		//from 0 (no missionary) up to number of missionaries
        	}
        	else {	//if missionaries=5 and capacity=3
        		this.randmiss = rand.nextInt(capacity+1);	//from 0 up to capacity
        	}
            this.missionariesLeft = this.missionariesLeft-this.randmiss;
            this.missionariesRight = this.missionariesRight + this.randmiss;
            
            //calculate the number of cannibals that will cross
            if(cannibalsLeft <= (capacity - this.randmiss)) {	//if cannibals Left are less than the capacity of the boat minus the number of missionaries on the boat already
        		this.randcans = capacity - this.randmiss - rand.nextInt(cannibalsLeft + 1);
        	}
        	else {	//if cannibals=5 and capacity=3 and missionaries on boat=1
        		this.randcans = rand.nextInt(capacity - randmiss + 1);
        	}
            this.cannibalsLeft = this.cannibalsLeft-this.randcans;
            this.cannibalsRight = this.cannibalsRight + this.randcans;
            
        }
        //the boat must be moved only when we exit the loop, not in case missionaries=cannibals=0 bc the variables have not changed yet
        boatRight = true;
        
        this.crosses=this.crosses+1;
    }
    
    private void randCrosstoLeft()
    {
        Random rand = new Random();
     
        while(this.randmiss + this.randcans == 0) {
        	if(missionariesRight <= capacity) {	//if missionaries right are less than the capacity of the boat e.g. 8 miss, 10 capacity
        		this.randmiss = capacity - rand.nextInt(missionariesRight + 1);		//from 0 (no missionary) up to number of missionaries(+1 is necessary)
        	}
        	else {	//if missionaries=5 and capacity=3
        		this.randmiss = rand.nextInt(capacity+1);	//from 0 up to capacity
        	}
            this.missionariesRight = this.missionariesRight - this.randmiss;
            this.missionariesLeft = this.missionariesLeft + this.randmiss;
            
            if(cannibalsRight <= capacity) {	//if cannibals right are less than the capacity of the boat e.g. 8 miss, 10 capacity
        		this.randcans = capacity - rand.nextInt(cannibalsRight + 1);		//from 0 (no cannibals) up to number of cannibals(+1 is necessary)
        	}
        	else {	//if missionaries=5 and capacity=3
        		this.randcans = rand.nextInt(capacity - randmiss + 1);	//from 0 up to capacity minus the number of missionaries already on boat
        	}
            this.cannibalsRight = this.cannibalsRight - this.randcans;
            this.cannibalsLeft = this.cannibalsLeft + this.randcans;
            
            
           
        }
        boatRight = false;
        
        this.crosses=this.crosses+1;
        
    }

    
  
    
    //euristic 1
    private void moreCannibals ()
    {
        if(boatRight && (missionariesLeft+cannibalsLeft)>0)
        {
            score = score + (2 * (missionariesLeft+cannibalsLeft)) ;
            //G = G + depth(this);
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)==1)
        {
            score = score + 1;
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)>1)
        {
            score = score + (2 * (missionariesLeft+cannibalsLeft)-3);
        }
        else if ((missionariesLeft+cannibalsLeft)==0)
        {
            score = score + 0;
        }
    }
    
    //euristic 2
    private void moreThanTwoPeople()
    {
        if(boatRight && (missionariesLeft+cannibalsLeft) > 0)
        {
            score = score + 2;
        }
        else if(!boatRight && (missionariesLeft+cannibalsLeft)>0)
        {
            score = score + 1;
        }
        else if ((missionariesLeft+cannibalsLeft)==0)
        {
            score = score +0;
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

    public State getFather() {
        return father;
    }

    public void setFather(State father) {
        this.father = father;
    }

    public boolean isFinal()
    {
    	if(this.crosses>maxCrosses) {
    		System.out.println("The number of crosses is bigger than the desirable ");
    		return false;
    	}
    	
        return missionariesLeft == 0 && cannibalsLeft == 0 && this.crosses<=maxCrosses;
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
/*
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
