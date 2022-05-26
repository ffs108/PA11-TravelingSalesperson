/* 
 * AUTHOR: Francisco Figueroa
 * FILE: PA11Main.java
 * ASSIGNMENT: Programming Assignment 11 - Traveling Salesperson 
 * COURSE: csc210; Spring 21
 * PURPOSE: Given an input MTX file, and a valid command, this program will generate a valid Sparse Matrix
 * representing the MTX instructions. Then taking into account the command will, do one of the following:
 * Use a heuristic approach to approximate an optimal solution to the matrix given
 * Use a backtracking approach to approximate an optimal solution to the matrix given
 * Use the author's approach to approximate an optimal solution to the matrix given
 * Time the results of the three approaches above and print out their time cost as well as route cost
 * 
 * USAGE:
 * input MTX file where that is formatted in the standard of MTX
 * Supported commands are the following: "HEURISTIC", "BACKTRACK", "MINE", "TIME"
 * The "HEURISTIC", "BACKTRACK", "MINE" commands output their route costs and a representation of the route taken
 * "TIME" will output each of the approaches route costs and then follow it with a real world time cost of computing
 * said route
 * It is assumed that the command format is exactly as shown above, any unknown commands will in nothing being
 * output
 */ 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class PA11Main {

	/*
	 * main takes in arguments(2) from the command line
	 * modular hub for rest of program
	 * args[0] is filename and is read into a reader method
	 * args[1] is a command and decides what method(s) will be 
	 * used in the run of main
	 * main returns nothing
	 */
    public static void main(String[] args) throws Exception {
    	DGraph matrix = reader(args[0]);
    	String command = args[1].toUpperCase();
    	if(command.equals("HEURISTIC")) {
	    	Trip hSol = heuristic(matrix);
	    	System.out.println(hSol.toString(matrix));
    	}
    	if(command.equals("BACKTRACK")) {
	    	Trip bSol = backtracking(matrix);
	    	System.out.println(bSol.toString(matrix));
    	}
    	if(command.equals("MINE")) {
    		Trip mSol = mine(matrix);
    		System.out.println(mSol.toString(matrix));
    	}
    	if(command.equals("TIME")) {
    		time(matrix);
    	}
    }
    
    /*
     * reader takes in a String that is a filename of ., along with its directory
     * reader reads in the file and parses through the comment and header
     * sections, it then takes the dimension of the desired matrix
     * finally, it creates the matrix object and fills it with the edges
     * reader returns a DGraph, a sparse matrix of the input file
     */
    private static DGraph reader(String directory) throws java.io.IOException {
    	File file = new File(directory);
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = br.readLine();
        boolean comment = true;
        while (comment) {
            line = br.readLine();
            comment = line.startsWith("%");
        }
        String[] str = line.split("( )+");
        int size = (Integer.valueOf(str[0].trim()));      
        DGraph matrix = new DGraph(size);
        while (true) {
            line = br.readLine();
            if (line == null)  break;
            str = line.split("( )+");
            int start = (Integer.valueOf(str[0].trim()));
            int end = (Integer.valueOf(str[1].trim()));
            double weight = (Double.valueOf(str[2].trim()));
            matrix.addEdge(start-1, end-1, weight);
        }
        br.close();
        return matrix;
    }
    		
    /*
     * heuristic takes in a DGraph object
     * A new Trip object is initialized and after setting the start point
     * the algorithm picks from a list of neighbors, the neighbor that is
     * the closest, repeating this process for the number of neighbors and the 
     * number of nodes present in the matrix 
     * heuristic returns a Trip object
     */
    private static Trip heuristic(DGraph matrix) {
    	Trip myTrip = new Trip(matrix.getNumNodes());
    	int cur = 1;
    	myTrip.chooseNextCity(cur);
    	for(int k=2; k <= matrix.getNumNodes(); k++) {
    		List<Integer> neighbors = matrix.getNeighbors(cur);
			int destination = 0;
			double minDist = 0;
    		for(Integer neighbor: neighbors) {
    			if(destination == 0 && myTrip.isCityAvailable(neighbor)) {
    				destination = neighbor;
    				minDist = matrix.getWeight(cur, neighbor);
    			}
    			else if(myTrip.isCityAvailable(neighbor) && minDist > matrix.getWeight(cur, neighbor)) {
    				destination = neighbor;
    				minDist = matrix.getWeight(cur, neighbor);
    			}
    		}
    		myTrip.chooseNextCity(destination);
    		cur = destination;
    	}
    	return myTrip;
    }
    
    /*
     * backtracking takes in a DGraph object
     * backtracking initializes two Trip objects, one at zero to be used as 
     * an initial compare point and the other as the Trip with the desired nodes
     * it then calls its helper function
     * backtracking returns a Trip object
     */
    private static Trip backtracking(DGraph matrix) {
    	Trip myTrip = new Trip(matrix.getNumNodes());
    	Trip minTrip = new Trip(0);
    	myTrip.chooseNextCity(1);
    	return backHelper(matrix, myTrip, minTrip);
    }
    
    /*
     * backHelper takes in a DGraph object, and two Trip objects
     * it first looks if all possible nodes in trip have been used and compares it
     * to the current minimum trip, replacing it if more optimal - this is our base case
     * if the cost of the trip so far is more than the cost of the minimal trip,
     * then for each city left to travel to, it will choose it as a possible route
     * calls itself and repeats the process, then tests another possibility
     * the final trip should be an optimal estimation to a good route
     * backHelper returns a Trip object 
     */
    private static Trip backHelper(DGraph matrix, Trip tripSoFar, Trip minTrip) {
    	if(tripSoFar.citiesLeft().size() == 0) {
    		if(tripSoFar.tripCost(matrix) < minTrip.tripCost(matrix)) {
    			minTrip.copyOtherIntoSelf(tripSoFar);
    			return minTrip;
    		}
    	}
    	else if(tripSoFar.tripCost(matrix) < minTrip.tripCost(matrix)) {
    		for(Integer city : tripSoFar.citiesLeft()) {
    			tripSoFar.chooseNextCity(city);
    			minTrip = backHelper(matrix, tripSoFar, minTrip);
    			tripSoFar.unchooseLastCity();
    		}
    	}
    	return minTrip;
    }
    
    /*
     * mine takes in a DGraph object
     * mine initializes two Trip objects, one at zero to be used as 
     * an initial compare point and the other as the Trip with the desired nodes
     * it then calls its helper function
     * mine returns a Trip object 
     */
    private static Trip mine(DGraph matrix) {
    	Trip myTrip = new Trip(matrix.getNumNodes());
    	Trip minTrip = new Trip(0);
    	myTrip.chooseNextCity(1);
    	return mineHelper(matrix, myTrip, minTrip);
    }
    
    /*
     * mineHelper is based on the backtracking approach with an added prune that 
     * helps the algorithm skip less optimal routes until they are more relevant
     * minekHelper takes in a DGraph object, and two Trip objects
     * it first looks if all possible nodes in trip have been used and compares it
     * to the current minimum trip, replacing it if more optimal - this is our base case
     * if the cost of the trip so far is more than the cost of the minimal trip,
     * then for each city left to travel to, it will choose it as a possible route
     * only if it is neighbor that costs less than one of its other neighbors
     * otherwise it is skipped until it is more optimal to use, it then
     * calls itself and repeats the process, then tests another possibility
     * the final trip should be an optimal estimation to a good route
     * mineHelper returns a Trip object  
     */
    private static Trip mineHelper(DGraph matrix, Trip tripSoFar, Trip minTrip) {
    	List<Integer> citiesLeft = tripSoFar.citiesLeft();
    	if(citiesLeft.size() == 0) {
    		if(tripSoFar.tripCost(matrix) < minTrip.tripCost(matrix)) {
    			minTrip.copyOtherIntoSelf(tripSoFar);
    			return minTrip;
    		}
    	}
    	else if(tripSoFar.tripCost(matrix) < minTrip.tripCost(matrix)) {
    		for(Integer city : citiesLeft) {
    			if(citiesLeft.contains(city+1)) {
	    			if(matrix.getNeighbors(city-1).contains(city) &&
	    					matrix.getWeight(city-1, city) > matrix.getWeight(city+1, city)) {
		    			tripSoFar.chooseNextCity(city);
		    			minTrip = backHelper(matrix, tripSoFar, minTrip);
		    			tripSoFar.unchooseLastCity();
	    			}
    			}
    		}
    	}
    	return minTrip;
    }
    
    /*
     * time takes in a DGraph object
     * time initializes two longs that keep track of each method call's
     * approximate runtime from 'start' to 'end' the runtime is divided by
     * a million to give the the millisecond approximation
     * each method runtime is printed along with the cost of their routes for 
     * the graph traversal
     * time returns nothing
     */
    private static void time(DGraph matrix) {
    	long start = System.nanoTime();
    	Trip myTrip = heuristic(matrix);
    	long end = System.nanoTime();
    	long time = (end-start)/1000000;
    	System.out.println("heuristic: cost = "+ myTrip.tripCost(matrix)+". " + time + " milliseconds");
    	start = System.nanoTime();
    	myTrip = mine(matrix);
    	end = System.nanoTime();
    	time = (end-start)/1000000;
    	System.out.println("mine: cost = "+ myTrip.tripCost(matrix)+". " + time + " milliseconds");
    	start = System.nanoTime();
    	myTrip = backtracking(matrix);
    	end = System.nanoTime();
    	time = (end-start)/1000000;
    	System.out.println("backtrack: cost = "+ myTrip.tripCost(matrix)+". " + time + " milliseconds");
    }
}
