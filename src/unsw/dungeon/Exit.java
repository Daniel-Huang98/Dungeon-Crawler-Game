package unsw.dungeon;

public class Exit extends Entity implements playerObserver{
	boolean deleted = false;
		
    public Exit(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void update(playerSubject player, int dX, int dY) {
    	
    }
    

	@Override
	public void delete() {
		deleted = true;
		
	}

	@Override
	public boolean isDeleted() {
		return deleted;
		
	}

}