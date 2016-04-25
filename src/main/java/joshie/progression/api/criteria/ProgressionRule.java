package joshie.progression.api.criteria;

public @interface ProgressionRule {
    /** The name to addCriteria this rule as, must be unique **/
    public String name();

    /** The colour to use for this rule **/
    public int color() default 0xFFCCCCCC;

    /** The icon to use for this rule **/
    public String icon() default "progression:item";

    /** This one should be ignored by you,
     *  it's pretty much for internal use only. */
    public String meta() default "";

    /** This one should be used if a mod is required, comma seperated list **/
    public String mod() default "progression";

    /** This should only be used on triggers **/
    public boolean cancelable() default false;
}
