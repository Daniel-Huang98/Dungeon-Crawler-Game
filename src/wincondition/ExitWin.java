package wincondition;

import unsw.dungeon.Dungeon;

public class ExitWin implements WinCondition{

	@Override
	public boolean canWin(Dungeon dungeon) {
		return dungeon.exitted();
	}

}