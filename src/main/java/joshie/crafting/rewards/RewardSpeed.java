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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardSpeed extends RewardBase implements ITextEditable {
    private float speed = 0.1F;

    public RewardSpeed() {
        super("Ability: Speed", theme.rewardSpeed, "speed");
    }

    @Override
    public IReward newInstance() {
        return new RewardSpeed();
    }

    @Override
    public Bus getBusType() {
        return Bus.FORGE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
                float speed = CraftingAPI.players.getPlayerData(player).getAbilities().getSpeed();
                if (speed > 0 && player.onGround && !player.isInWater() && player.isSprinting() && ClientHelper.isForwardPressed()) {
                    player.moveFlying(0F, 1.0F, speed);
                }
            }
        }
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardSpeed reward = new RewardSpeed();
        reward.speed = data.get("speed").getAsFloat();
        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("speed", speed);
    }

    @Override
    public void reward(UUID uuid) {
        CraftingAPI.players.getServerPlayer(uuid).addSpeed(speed);
    }

    private static final ItemStack speedStack = new ItemStack(Items.potionitem, 1, 8194);

    @Override
    public ItemStack getIcon() {
        return speedStack;
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
        int speedColor = theme.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) speedColor = theme.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            drawText("speed: " + SelectTextEdit.INSTANCE.getText(), 4, 18, speedColor);
        } else drawText("speed: " + getTextField(), 4, 18, speedColor);
    }

    private String textField;

    @Override
    public String getTextField() {
        if (textField == null) {
            textField = "" + speed;
        }

        return textField;
    }

    @Override
    public void setTextField(String text) {
        String fixed = text.replaceAll("[^0-9.]", "");
        this.textField = fixed;

        try {
            this.speed = Float.parseFloat(textField);
        } catch (Exception e) {
            this.speed = 0F;
        }
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Ability Gain");
        list.add("Speed: " + speed);
    }
}
