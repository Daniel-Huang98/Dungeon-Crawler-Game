package unsw.dungeon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import battle.battle;
import battle.deathBattle;
import battle.swordBattle;

/**
 * The player entity, contains sword reference, a boolean representing
 * if the player is able to make a legal move, a boolean representing if
 * the player has died or not, a boolean representing the key and a battle
 * strategy
 *
 */
public class Player extends Entity implements playerSubject, playerObserver {

    private Dungeon dungeon;
    boolean canMove = true;
    List<playerObserver>observers;
    Sword sword;
    Potion potion;
    boolean alive;
    boolean hasKey = false;
    battle action;


    /*
     * Create a player positioned in square (x,y)
     * @param dungeon : dungeon entity
     * @param x : x coordinate
     * @param y : y coordinate
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.observers = new ArrayList<playerObserver>();
        this.alive = true;
        this.sword = null;
        this.action = new deathBattle();
    }

    /*
     * Checks if player can legally move up by notifying
     * all observer
     */
    public void moveUp() {
    	notifyEntities(0,-1);
    	if(!canMove) {
    		canMove = true;
    		return;
    	}
        if (getY() > 0)
            y().set(getY() - 1);
        if(this.potion != null)this.decrementPotionHealth();
    }

    /*
     * Checks if player can legally move down by notifying
     * all observer
     */
    public void moveDown() {
    	notifyEntities(0,1);
    	if(!canMove) {
    		canMove = true;
    		return;
    	}
        if (getY() < dungeon.getHeight() - 1)
            y().set(getY() + 1);
        if(this.potion != null)this.decrementPotionHealth();
    }

    /*
     * Checks if player can legally move left by notifying
     * all observer
     */
    public void moveLeft() {
    	notifyEntities(-1,0);
    	if(!canMove) {
    		canMove = true;
    		return;
    	}
        if (getX() > 0)
            x().set(getX() - 1);
        if(this.potion != null)this.decrementPotionHealth();
    }

    /*
     * Checks if player can legally move right by notifying
     * all observer
     */
    public void moveRight() {
    	notifyEntities(1,0);
    	if(!canMove) {
    		canMove = true;
    		return;
    	}
        if (getX() < dungeon.getWidth() - 1)
            x().set(getX() + 1);
        if(this.potion != null)this.decrementPotionHealth();
    }
    
    /**
     * Sets if the player can move
     * @param flag Set flag to true if the player can move, set to false if it can't move
     */
    public void setCanMove(boolean flag) {
    	this.canMove = flag;
    }
    
   
    public boolean getKey() {
    	return hasKey;
    }
    
    public void setKey(boolean flag) {
    	this.hasKey = flag;
    }
    
    /**
     * Sets the coordinate of the player in the dungeon
     * @param x x coordinate of the player
     * @param y y coordinate of the player
     */
    public void setCoordinates(int x, int y) {
    	x().set(x);
    	y().set(y);
    }
    
    public void setSword(Sword obj) {
    	this.sword = obj;
    	System.out.println("I has the sword now");
    }
    
    public Sword getSword() {
    	return this.sword;
    }
    
    public void setPotion(Potion obj) {
    	this.potion = obj;
    }
    
    public Potion getPotion() {
    	return this.potion;
    }
    
    /**
     * Will set allive to false and notifies the dungeon of the player's death
     */
    public void die() {
    	this.alive = false;
    	System.out.println("you have died");
    	dungeon.die();
    }
    
    /**
     * returns booleans based on whether the player is still alive
     * @return returns true if player is alive else returns false
     */
    public boolean isAlive() {
    	return this.alive;
    }
    
    /** 
     * When sword breaks, change battle strategy
     */
    public void decrementSwordHealth() {
    	if(sword.decrementHealth()) {
    		this.sword = null;
    		this.action = new deathBattle();
    	}
    }
    
    /** 
     * When invincibility runs out, change battle strategy
     */
    public void decrementPotionHealth() {
    	if(potion.decrementHealth()) {
    		this.potion = null;
    		if(sword != null) {
    			this.action = new swordBattle();
    		} else {
    			this.action = new deathBattle();
    		}
    	}
    }
   
    public void setAction(battle action) {
    	this.action = action;
    }
    
    public battle getAction() {
    	return this.action;
    }
    
    /**
     * notifies the dungeon that gold has been collected
     */
    public void collectGold() {
    	this.dungeon.collectGold();
    }
    
    /**
     * notifies the dungeon that an enemy has been killed
     */
    public void killEnemy() {
    	this.dungeon.killEnemy();
    }
    
    /**
     * notifies the dungeon that the player has tried to exit
     */
    public void exit() {
    	this.dungeon.exit();
    }
    
    /**
     * notifies all observers of its change in coordinates
     */
    @Override
    public void notifyEntities(int dX, int dY) {
    	for(playerObserver e: observers) {
    		if(!e.isDeleted())e.update(this,dX, dY);
    	}
    }
    
    @Override
    public void addObserver(playerObserver obj) {
    	this.observers.add(obj);
    }
    
    @Override
    public void deleteObserver(playerObserver obj){
    	this.observers.remove(obj);
    }

    /**
     * if an enemy has touched or moved over the player, the player will battle the enemy 
     */
	@Override
	public void update(playerSubject obj, int dX, int dY) {
		if (obj instanceof Enemy) {
	    	if(((Enemy)obj).getX() == this.getX() && ((Enemy)obj).getY() == this.getY()) {
	    		if(getAction().attacked(this)) {
	    			((Enemy)obj).delete(); 	
	    			System.out.println("Enemy has died");
	    		}
	    	}
		}
	}

	@Override
	public void delete() {
		return;
		
	}

	@Override
	public boolean isDeleted() {
		return false;
	}
}
