package joshie.progression.api;

import joshie.progression.api.data.IDataHelper;
import joshie.progression.api.fields.IFieldRegistry;
import joshie.progression.api.filters.IFilterHelper;

public class ProgressionAPI {
	public static IProgressionAPI registry = null;
    public static IPlayerData player = null;
    public static IFieldRegistry fields = null;
    public static IFilterHelper filters = null;
    public static IDataHelper data = null;
}
