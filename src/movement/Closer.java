package movement;
import java.util.*;

import unsw.dungeon.Enemy;
import unsw.dungeon.Entity;

import java.lang.*; 
import java.io.*; 

/**
 * A class that tells the enemy to move towards a certain entity.
 * Contains a 1 to 1 entity map as well as height and width of the
 * dungeon.
 */
public class Closer implements Movement{

	private int height;
	private int width;
	private int lastX = -1;
	private int lastY = -1;
	private ArrayList<ArrayList<Entity>> map = new ArrayList<ArrayList<Entity>>();

	
	/**
	 * Constructs a Closer object
	 */
	public Closer() {		
	}

    /**
     * Move the character away from the entity by using the output of
     * the Dijkstra algorithm.
     * @param e : the enemy that is moving
     * @param dest : the destination entity that the enemy is moving away from
     * @param height : height of dungeon
     * @param width : width of dungeon
     * @param map : entity map
     * @return : updated map after the enemy has moved
     */
	@Override
	public ArrayList<ArrayList<Entity>> moveCharacter(Enemy e, Entity dest,int height, int width, ArrayList<ArrayList<Entity>> map) {
		Dijkstra pathing = new Dijkstra(height, width, map);
		int g[][] = pathing.getGraph();
		if (lastX != -1 && lastY != -1) {
	    	g[e.getY()*map.get(0).size()+e.getX()][lastY*map.get(0).size()+lastX] = 0;
	    	g[lastY*map.get(0).size()+lastX][e.getY()*map.get(0).size()+e.getX()] = 0;
		}
		pathing.dijkstra(e);
    	int curr = dest.getY()*map.get(0).size()+dest.getX();
    	int next = pathing.getFrom()[curr];
    	int counter = 0;
    	
    	//backtrack the traceback array
    	while (next != e.getY()*map.get(0).size()+e.getX() && counter < map.size()*map.get(0).size()) {
    		curr = next;
    		if (curr == -1) break;
    		next = pathing.getFrom()[curr];
    		counter++;
    	}
    	if (curr == -1) return map;
    	//make sure no infinite loop occurs
    	if (counter == map.size()*map.get(0).size()) {
    		return map;
    	}
    	
    	//calculate next coordinate, move the enemy and update the map
    	int nextY = curr/map.get(0).size();
    	int nextX = curr%(map.get(0).size());
    	map.get(e.getY()).set(e.getX(),null);
    	lastX = e.getX();
    	lastY = e.getY();
    	e.setMove(nextX, nextY);
	    map.get(e.getY()).set(e.getX(),(Entity)e);		
	    return map;
	}


}
