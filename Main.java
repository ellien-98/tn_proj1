//import org.omg.Messaging.SyncScopeHelper;
//import sun.java2d.pipe.SpanClipRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of Missionaries. Same number will be applied for cannibals: ");
        int missionaries = Integer.parseInt(sc.nextLine());
        int cannibals = missionaries;

        System.out.println("Enter the capacity of the boat: ");
        int capacity = Integer.parseInt(sc.nextLine());

        System.out.println("Enter the times the boat is allowed to cross the river: ");
        int k = Integer.parseInt(sc.nextLine());
        sc.close();
        
        State initialState = new State (missionaries, cannibals, 0, 0, false, capacity, k);
 
        SpaceSearcher searcher = new SpaceSearcher();

        long start = System.currentTimeMillis();
        State terminalState = searcher.AStarClosedSet(initialState, 1);
        long end = System.currentTimeMillis();

        if (terminalState == null) System.out.println("Could not find a solution.");
        else
        {
            State temp = terminalState;
            ArrayList<State> path = new ArrayList<>();
            while(temp.getFather() != null)
            {
                path.add(temp.getFather());
                temp = temp.getFather();
            }
            Collections.reverse(path);
            for(State item: path)
            {
                item.print();
            }
            terminalState.print();
            System.out.println();
            System.out.println("Search time: " + (double)(end-start)/1000 + " sec.");
        }
    }
}
