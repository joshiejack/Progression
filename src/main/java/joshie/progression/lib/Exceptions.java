package joshie.progression.lib;

public class Exceptions {
	public static class CriteriaNotFoundException extends NullPointerException {
		public CriteriaNotFoundException(String name) {
			super("The following condition does not actually exist: " + name);
		}
	}
}
