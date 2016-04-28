package joshie.progression.helpers;

import joshie.progression.criteria.Criteria;
import joshie.progression.network.PacketCompleted.DummyAchievement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AchievementHelper {
    public static void display(final ItemStack icon, final String description) {
        GuiAchievement gui = Minecraft.getMinecraft().guiAchievement;
        gui.displayUnformattedAchievement(new DummyAchievement(new Criteria(null, null) {
            @Override
            public ItemStack getIcon() {
                return icon;
            }
        }) {
            @Override
            @SideOnly(Side.CLIENT)
            public String getDescription() {
                return description;
            }
        });

        ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, Minecraft.getSystemTime(), "notificationTime", "field_146263_l");
        ReflectionHelper.setPrivateValue(GuiAchievement.class, gui, false, "permanentNotification", "field_146262_n");
    }
}
