package joshie.crafting.api;

public interface IPlayerDataClient extends IPlayerData {
	/** Set the speed on the client **/
	public void setSpeed(float speed);

	/** Erases any existing researches, and makes these the ones **/
	public void setResearch(IResearch... researches);

	/** Erases any existing killings under this name and replaces with this value **/
	public void setKillings(String entity, int killings);

	/** Erases any existing completions, and replaces them **/
	public void setCompleted(ICondition... conditions);
}
