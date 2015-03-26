package joshie.crafting.api;

import joshie.crafting.player.DataStats;

public interface IPlayerDataClient extends IPlayerData {
	public void setAbilities(DataStats abilities);
}
