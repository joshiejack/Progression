package joshie.crafting.api;

import joshie.crafting.player.DataAbilities;

public interface IPlayerDataClient extends IPlayerData {
	public void setAbilities(DataAbilities abilities);
}
