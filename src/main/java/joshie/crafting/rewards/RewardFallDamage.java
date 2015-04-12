package joshie.crafting.rewards;

import java.util.List;
import java.util.UUID;

import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.IReward;
import joshie.crafting.gui.SelectTextEdit;
import joshie.crafting.gui.SelectTextEdit.ITextEditable;
import joshie.crafting.helpers.ClientHelper;
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
        super("Ability: Fall Resistance", theme.rewardFallDamage, "fallDamage");
    }

    @Override
    public IReward newInstance() {
        return new RewardFallDamage();
    }

    @Override
    public Bus getBusType() {
        return Bus.FORGE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            int damage = (int) (event.distance - 3);
            int maxAbsorbed = CraftingAPI.players.getPlayerData(player).getAbilities().getFallDamagePrevention();
            if (damage < maxAbsorbed) {
                event.setCanceled(true);
            } else {
                event.distance = event.distance - maxAbsorbed;
            }
        }
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardFallDamage reward = new RewardFallDamage();
        reward.maxAbsorbed = data.get("maxAbsorption").getAsInt();
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("maxAbsorption", maxAbsorbed);
    }

    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).addFallDamagePrevention(maxAbsorbed);
    }

    private static final ItemStack feather = new ItemStack(Items.feather);

    //TODO: Replace with more appropriate icon
    @Override
    public ItemStack getIcon() {
        return feather;
    }

    @Override
    public Result clicked() {
        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) {
                SelectTextEdit.INSTANCE.select(this);
                return Result.ALLOW;
            }
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw() {
        int color = theme.optionsFontColor;
        
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) color = theme.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            drawText("absorption: " + SelectTextEdit.INSTANCE.getText(), 4, 18, color);
        } else drawText("absorption: " + getTextField(), 4, 18, color);
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
