package joshie.crafting.lib;

public class Exceptions {
	public static class ConditionNotFoundException extends NullPointerException {
		public ConditionNotFoundException(String name) {
			super("The following condition does not actually exist: " + name);
		}
	}
	
	public static class TechnologyNotFoundException extends NullPointerException {
		public TechnologyNotFoundException(String name) {
			super("The following technology does not actually exist: " + name);
		}
	}
}
