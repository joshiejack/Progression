package joshie.progression.criteria.triggers;

import joshie.progression.Progression;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.api.criteria.ITrigger;
import joshie.progression.api.criteria.ProgressionRule;
import joshie.progression.api.special.ICustomDescription;
import joshie.progression.api.special.IInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@ProgressionRule(name = "chat", color = 0xFFCC6600, meta = "onSentMessage", cancelable = true)
public class TriggerChat extends TriggerBaseBoolean implements IInit, ICustomDescription {
    private String matchString;
    private boolean matchBoth;
    private boolean matchFront;
    private boolean matchBack;

    public String toMatch = "*help*";

    @Override
    public ITrigger copy() {
        TriggerChat trigger = new TriggerChat();
        trigger.matchString = matchString;
        trigger.matchBoth = matchBoth;
        trigger.matchFront = matchFront;
        trigger.matchBack = matchBack;
        trigger.toMatch = toMatch;
        return copyBoolean(trigger);
    }

    @Override
    public String getDescription() {
        return Progression.format(getProvider().getUnlocalisedName() + ".description", toMatch);
    }

    @Override
    public void init(boolean isClient) {
        if (toMatch.startsWith("*")) matchFront = true;
        else matchFront = false;
        if (toMatch.endsWith("*")) matchBack = true;
        else matchBack = false;
        matchBoth = matchFront && matchBack;
        matchString = toMatch.replaceAll("\\*", "");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCommandSend(ServerChatEvent event) {
        if (ProgressionAPI.registry.fireTrigger((EntityPlayer) event.getPlayer().getCommandSenderEntity(), getProvider().getUnlocalisedName(), event.getMessage()) == Result.DENY) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean onFired(UUID uuid, Object... data) {
        value = isTrue(data);
        return value;
    }

    @Override
    protected boolean isTrue(Object... data) {
        String text = (String) data[0];
        if (matchBoth && matchString.contains(text)) return true;
        else if (matchFront && !matchBack && matchString.endsWith(text)) return true;
        else if (!matchFront && matchBack && matchString.startsWith(text)) return true;
        else if (matchString.equals(text)) return true;
        else return false;
    }
}
