package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.DrawHelper;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.JSONHelper;
import joshie.crafting.json.Theme;
import joshie.crafting.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardFallDamage extends RewardBase implements ITextEditable {
    private int maxAbsorbed = 1;

    public RewardFallDamage() {
        super("fallDamage", 0xFF661A00);
    }

    @Override
    public Bus getEventBus() {
        return Bus.FORGE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            int damage = (int) (event.distance - 3);
            int maxAbsorbed = PlayerTracker.getPlayerData(player).getAbilities().getFallDamagePrevention();
            if (damage < maxAbsorbed) {
                event.setCanceled(true);
            } else {
                event.distance = event.distance - maxAbsorbed;
            }
        }
    }

    @Override
    public void readFromJSON(JsonObject data) {
        maxAbsorbed = JSONHelper.getInteger(data, "maxAbsorption", maxAbsorbed);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setInteger(data, "maxAbsorption", maxAbsorbed, 1);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).addFallDamagePrevention(maxAbsorbed);
    }

    private static final ItemStack feather = new ItemStack(Items.feather);

    //TODO: Replace with more appropriate icon
    @Override
    public ItemStack getIcon() {
        return feather;
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int color = Theme.INSTANCE.optionsFontColor;
        
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            DrawHelper.triggerDraw.drawText("absorption: " + SelectTextEdit.INSTANCE.getText(), 4, 18, color);
        } else DrawHelper.triggerDraw.drawText("absorption: " + getTextField(), 4, 18, color);
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            textField = "" + maxAbsorbed;
        }

        return textField;
    }

    @Override
    public void setTextField(String text) {
        String fixed = text.replaceAll("[^0-9]", "");
        this.textField = fixed;

        try {
            this.maxAbsorbed = Integer.parseInt(textField);
        } catch (Exception e) {
            this.maxAbsorbed = 1;
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Fall Resistance: " + maxAbsorbed);
    }
}
