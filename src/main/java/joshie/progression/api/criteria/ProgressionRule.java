package joshie.progression.api.criteria;

public @interface ProgressionRule {
    public String name();
    public int color() default 0xFFCCCCCC;
    public String icon() default "progression:item";

    /** This one should be ignored by you,
     *  it's pretty much for internal use only. */
    public String meta() default "";

    /** This one should be used if a mod is required **/
    public String mod() default "progression";

    /** This should only be used on triggers **/
    public boolean cancelable() default false;
}
