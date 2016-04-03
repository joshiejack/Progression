package joshie.progression.api;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.UUID;

public interface IPlayerTeam {
    /** Will return the owner as an entity player, this can be null **/
    public EntityPlayer getOwnerEntity();

    /** Returns a set of players, including the owner, only includes online members **/
    public Collection<EntityPlayer> getTeamEntities();

    /** Returns true if this is a single player team instead of a team team **/
    public boolean isSingle();

    /** Returns true if conditions should check for one valid team **/
    public boolean isTrueTeam();

    /** Returns the uuid of the owner **/
    public UUID getOwner();
}
