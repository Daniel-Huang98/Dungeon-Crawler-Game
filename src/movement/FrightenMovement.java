package movement;

import java.util.ArrayList;

import unsw.dungeon.Enemy;
import unsw.dungeon.Entity;

public class FrightenMovement implements Movement{
	 public boolean checkBounds(int x, int y,ArrayList<ArrayList<Entity>> map) {
	    	return (x < map.get(0).size() && x >= 0 && y < map.size() && y >= 0);
	    }
		
	    /**
	     * Move the character away from the entity by inverting the output of
	     * the Dijkstra algorithm. If no reachable, try all the other directions
	     * @param e : the enemy that is moving
	     * @param dest : the destination entity that the enemy is moving away from
	     * @return : updated map after the enemy has moved
	     */
		@Override
		public ArrayList<ArrayList<Entity>> moveCharacter(Enemy e, Entity dest,int height, int width, ArrayList<ArrayList<Entity>> map) {
			Dijkstra pathing = new Dijkstra(height, width, map);
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
			int oldX = e.getX();
			int oldY = e.getY(); 
			int dX = nextX-oldX;
			int dY = nextY-oldY;
			
			//check if the move opposite to Dijkstra is reachable
			if (checkBounds((oldX + (dX)*-1), (oldY + (dY)*-1),map) && map.get(oldY + (dY)*-1).get(oldX + (dX)*-1) == null) {
				e.setMove(oldX + (dX)*-1, oldY + (dY)*-1);
			}
			//try other directions
			else if (dY == 0) {
				if (checkBounds(oldX, oldY+1,map) && map.get(oldY+1).get(oldX) == null) {
					e.setMove(oldX,oldY+1);
				}
				else if (checkBounds(oldX, oldY-1,map) && map.get(oldY - 1).get(oldX) == null) {
					e.setMove(oldX,oldY-1);
				}
				else {
					e.setMove(nextX, nextY);
				}
			}
			//try other directions
			else if (dX == 0) {
				if (checkBounds(oldX+1, oldY,map) && map.get(oldY).get(oldX+1) == null) {
					e.setMove(oldX+1,oldY);
				}
				else if (checkBounds(oldX-1, oldY,map) && map.get(oldY).get(oldX-1) == null) {
					e.setMove(oldX-1,oldY);
				}
				else {
					e.setMove(nextX, nextY);
				}
			}	  		
			map.get(e.getY()).set(e.getX(),(Entity)e);
			return map;
		}	

}
