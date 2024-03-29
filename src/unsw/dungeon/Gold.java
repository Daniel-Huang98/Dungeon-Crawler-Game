package unsw.dungeon;

public class Gold extends Entity implements playerObserver{
	boolean deleted = false;
	
	/**
	 * Constructor for the gold entity
	 * @param x X position of the gold in the dungeon
	 * @param y Y position of the gold in the dungeon
	 */
    public Gold(int x, int y) {
        super(x, y);
    }

    /**
     * If the player walks over the gold, the player will collect the gold
     * @param obj: a subject that is observed
     * @param dX: the subject's change in X direction
     * @param dY: the subject's change in Y direction
     */
    @Override
    public void update(playerSubject obj, int dX, int dY) {
    	if (obj instanceof Player) {
	    	if((dX + ((Player)obj).getX()) == this.getX() && (((Player)obj).getY()+dY) == this.getY()) {
	    		System.out.println("collected the gold");
	    		((Player)obj).collectGold();
	    		this.delete();
	    	}
    	}
    }
    

	@Override
	public void delete() {
		deleted = true;
		this.gone().set(true);
	}

	@Override
	public boolean isDeleted() {
		return deleted;
		
	}
}
