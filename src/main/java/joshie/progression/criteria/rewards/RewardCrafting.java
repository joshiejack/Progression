package joshie.progression.criteria.rewards;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import joshie.progression.Progression;
import joshie.progression.api.ActionEvent.CanObtainFromActionEvent;
import joshie.progression.api.ActionEvent.CanUseToPeformActionEvent;
import joshie.progression.api.EventBusType;
import joshie.progression.api.ICriteria;
import joshie.progression.api.ProgressionAPI;
import joshie.progression.crafting.ActionType;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.criteria.Criteria;
import joshie.progression.gui.GuiCriteriaEditor;
import joshie.progression.gui.IItemSelectable;
import joshie.progression.gui.SelectItemOverlay;
import joshie.progression.gui.SelectItemOverlay.Type;
import joshie.progression.gui.TextFieldHelper;
import joshie.progression.helpers.BlockActionHelper;
import joshie.progression.helpers.ClientHelper;
import joshie.progression.helpers.CraftingHelper;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.json.Theme;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import codechicken.nei.api.API;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

//TODO: SWITCH TO NEW SYSTEM
public class RewardCrafting extends RewardBase implements IItemSelectable {
    private TextFieldHelper editMod;
    private ItemStack stack = new ItemStack(Blocks.furnace);
    private ActionType type = ActionType.CRAFTING;
    private boolean matchDamage = true;
    private boolean matchNBT = false;
    private boolean usage = true;
    private boolean crafting = true;
    public String modid = "IGNORE";

    public RewardCrafting() {
        super("crafting", 0xFF0085B2);
        editMod = new TextFieldHelper("modid", this);
    }

    @Override
    public EventBusType getEventBus() {
        return EventBusType.FORGE;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(EntityInteractEvent event) {
        checkAndCancelEvent(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!checkAndCancelEvent(event)) {
            Collection<ICriteria> requirements = CraftingRegistry.getCraftingCriteria(type, event.entityPlayer.getCurrentEquippedItem());
            if (requirements.size() > 0) {
                for (ICriteria c : requirements) {
                    GuiCriteriaEditor.INSTANCE.selected = (Criteria) c;
                    break;
                }

                event.entityPlayer.openGui(Progression.instance, 1, null, 0, 0, 0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        Crafter crafter = CraftingRegistry.getCrafterFromPlayer(event.entityPlayer);
        if (!crafter.canCraftItem(ActionType.CRAFTING, event.itemStack)) {
            boolean hasStuff = false;
            for (ActionType type : ActionType.craftingTypes) {
                Collection<ICriteria> requirements = CraftingRegistry.getCraftingCriteria(type, event.itemStack);
                if (requirements.size() > 0) {
                    if (!hasStuff) {
                        event.toolTip.add("Currently Locked");
                        hasStuff = true;
                    }

                    event.toolTip.add(EnumChatFormatting.WHITE + type.getDisplayName());
                    for (ICriteria c : requirements) {
                        ((Criteria)c).addTooltip(event.toolTip);
                    }
                }
            }

            if (hasStuff) {
                event.toolTip.add(EnumChatFormatting.AQUA + "Click for more info");
            }
        }
    }

    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        Entity source = event.source.getSourceOfDamage();
        if (source instanceof EntityPlayer) {
            Iterator<EntityItem> it = event.drops.iterator();
            while (it.hasNext()) {
                EntityItem item = it.next();
                ItemStack stack = item.getEntityItem();
                EntityPlayer player = (EntityPlayer) source;
                if (isEventCancelled(player, ActionType.ENTITY, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        if (isEventCancelled(event.entityPlayer, ActionType.BREAKBLOCK, event.entityPlayer.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(event.block, event.metadata))) {
            event.newSpeed = 0F;
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (isEventCancelled(player, ActionType.BREAKBLOCK, player.getCurrentEquippedItem(), BlockActionHelper.getStackFromBlockData(event.block, event.blockMetadata))) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onHarvestDrop(HarvestDropsEvent event) {
        EntityPlayer player = event.harvester;
        if (player != null) {
            Iterator<ItemStack> it = event.drops.iterator();
            while (it.hasNext()) {
                ItemStack stack = it.next();
                if (isEventCancelled(player, ActionType.HARVEST, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttemptToUseItemToPerformAction(CanUseToPeformActionEvent event) {
        if (event.stack == null) return;
        Crafter crafter = event.player != null ? CraftingRegistry.getCrafterFromPlayer(event.player) : CraftingRegistry.getCrafterFromTile(event.tile);
        if (crafter.canCraftWithAnything()) return;
        if (!crafter.canUseItemForCrafting(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToObtainItem(CanObtainFromActionEvent event) {        
        if (event.stack == null) return;
        Crafter crafter = event.player != null ? CraftingRegistry.getCrafterFromPlayer(event.player) : CraftingRegistry.getCrafterFromTile(event.tile);
        if (!crafter.canCraftItem(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    private boolean isEventCancelled(EntityPlayer player, ActionType type, ItemStack usageStack, ItemStack craftingStack) {
        if (!CraftingHelper.canUseItemForCrafting(type, player, usageStack)) {
            return true;
        } else {
            if (!CraftingHelper.canCraftItem(type, player, craftingStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkAndCancelEvent(PlayerEvent event) {
        if (event.entityPlayer.getCurrentEquippedItem() == null) return true;
        EntityPlayer player = event.entityPlayer;
        Crafter crafter = CraftingRegistry.getCrafterFromPlayer(player);
        if (!crafter.canCraftItem(ActionType.CRAFTING, player.getCurrentEquippedItem())) {
            event.setCanceled(true);
            return false;
        } else return true;
    }

    @Override
    public void readFromJSON(JsonObject data) {
        stack = JSONHelper.getItemStack(data, "item", stack);
        if (JSONHelper.getExists(data, "craftingType")) {
            String craftingtype = JSONHelper.getString(data, "craftingType", type.name.toLowerCase());
            for (ActionType type : ActionType.craftingTypes) {
                if (type.name.equalsIgnoreCase(craftingtype)) {
                    this.type = type;
                    break;
                }
            }
        }
        
        matchDamage = JSONHelper.getBoolean(data, "matchDamage", matchDamage);
        matchNBT = JSONHelper.getBoolean(data, "matchNBT", matchNBT);
        crafting = JSONHelper.getBoolean(data, "disableCrafting", crafting);
        usage = JSONHelper.getBoolean(data, "disableUsage", usage);
        modid = JSONHelper.getString(data, "modid", modid);

        if (Progression.NEI_LOADED) {
            boolean hide = JSONHelper.getBoolean(data, "hideFromNEI", false);
            if (hide) {
                isAdded = false;
                API.hideItem(stack);
            }
        }
    }

    @Override
    public void writeToJSON(JsonObject data) {
        JSONHelper.setItemStack(data, "item", stack);
        JSONHelper.setString(data, "craftingType", type.name.toLowerCase(), ActionType.CRAFTING.name.toLowerCase());
        JSONHelper.setBoolean(data, "matchDamage", matchDamage, true);
        JSONHelper.setBoolean(data, "matchNBT", matchNBT, false);
        JSONHelper.setBoolean(data, "disableCrafting", crafting, true);
        JSONHelper.setBoolean(data, "disableUsage", usage, true);
        JSONHelper.setString(data, "modid", modid, "IGNORE");
    }

    private boolean isAdded = true;

    @Override
    public void reward(UUID uuid) {
        if (Progression.NEI_LOADED && !isAdded) {
            API.addItemListEntry(stack);
            isAdded = true;
        }
    }

    @Override
    public void onAdded() {
        CraftingRegistry.addRequirement(type, modid, stack, matchDamage, matchNBT, usage, crafting, criteria);
    }

    @Override
    public void onRemoved() {
        CraftingRegistry.remove(type, modid, stack, matchDamage, matchNBT, usage, crafting, criteria);
    }

    //TODO: Replace this with an item which overlay the item
    //With some of crafting representation
    @Override
    public ItemStack getIcon() {
        return stack;
    }

    public ActionType next() {
        int id = type.id + 1;
        if (id < ActionType.craftingTypes.size()) {
            return ActionType.craftingTypes.get(id);
        }

        return ActionType.craftingTypes.get(0);
    }

    @Override
    public Result onClicked(int mouseX, int mouseY) {
        if (mouseX >= 77 && mouseX <= 100) {
            if (mouseY >= 43 && mouseY <= 68) {
                SelectItemOverlay.INSTANCE.select(this, Type.REWARD);
                return Result.ALLOW;
            }
        }

        if (mouseX <= 84 && mouseX >= 1) {
            if (mouseY >= 17 && mouseY <= 25) type = next();
            if (mouseY > 25 && mouseY <= 33) matchDamage = !matchDamage;
            if (mouseY > 34 && mouseY <= 41) matchNBT = !matchNBT;
            if (mouseY > 42 && mouseY <= 50) usage = !usage;
            if (mouseY > 50 && mouseY <= 58) crafting = !crafting;
            if (mouseY > 58 && mouseY <= 66) editMod.select();
            if (mouseY >= 17 && mouseY <= 66) return Result.ALLOW;
        }

        return Result.DEFAULT;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        ProgressionAPI.draw.drawStack(getIcon(), 76, 44, 1.4F);
        int typeColor = Theme.INSTANCE.optionsFontColor;
        int matchColor = Theme.INSTANCE.optionsFontColor;
        int match2Color = Theme.INSTANCE.optionsFontColor;
        int usageColor = Theme.INSTANCE.optionsFontColor;
        int craftColor = Theme.INSTANCE.optionsFontColor;
        int modColor = Theme.INSTANCE.optionsFontColor;

        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 25 && mouseY <= 33) matchColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 34 && mouseY <= 41) match2Color = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 42 && mouseY <= 50) usageColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 50 && mouseY <= 58) craftColor = Theme.INSTANCE.optionsFontColorHover;
                if (mouseY > 58 && mouseY <= 66) modColor = Theme.INSTANCE.optionsFontColorHover;
            }
        }
        
        ProgressionAPI.draw.drawText("type: " + type.name.toLowerCase(), 4, 18, typeColor);
        ProgressionAPI.draw.drawText("matchDamage: " + matchDamage, 4, 26, matchColor);
        ProgressionAPI.draw.drawText("matchNBT: " + matchNBT, 4, 34, match2Color);
        ProgressionAPI.draw.drawText("usage: " + usage, 4, 42, usageColor);
        ProgressionAPI.draw.drawText("crafting: " + crafting, 4, 50, craftColor);
        ProgressionAPI.draw.drawText("modid: " + editMod.getText(), 4, 58, modColor);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        onRemoved();
        this.stack = stack;
        onAdded();
    }

    @Override
    public void addTooltip(List list) {
        list.add(EnumChatFormatting.WHITE + "Allow " + type.getDisplayName());
        list.add(stack.getDisplayName());
    }
}
