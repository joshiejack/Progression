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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RewardSpeed extends RewardBase implements ITextEditable {
    private float speed = 0.1F;

    public RewardSpeed() {
        super("speed", 0xFFFFBF00);
    }

    @Override
    public Bus getEventBus() {
        return Bus.FORGE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (player.worldObj.isRemote) {
                float speed = PlayerTracker.getClientPlayer().getAbilities().getSpeed();
                if (speed > 0 && player.onGround && !player.isInWater() && player.isSprinting() && ClientHelper.isForwardPressed()) {
                    player.moveFlying(0F, 1.0F, speed);
                }
            }
        }
    }

    @Override
    public void readFromJSON(JsonObject data) {
        speed = JSONHelper.getFloat(data, "speed", speed);
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setFloat(data, "speed", speed, 0.1F);
    }

    @Override
    public void reward(UUID uuid) {
        PlayerTracker.getServerPlayer(uuid).addSpeed(speed);
    }

    private static final ItemStack speedStack = new ItemStack(Items.potionitem, 1, 8194);

    @Override
    public ItemStack getIcon() {
        return speedStack;
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
        int speedColor = Theme.INSTANCE.optionsFontColor;
        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) speedColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }

        if (SelectTextEdit.INSTANCE.getEditable() == this) {
            DrawHelper.drawText("speed: " + SelectTextEdit.INSTANCE.getText(), 4, 18, speedColor);
        } else DrawHelper.drawText("speed: " + getTextField(), 4, 18, speedColor);
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
