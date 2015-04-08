package joshie.crafting.rewards;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import joshie.crafting.CraftingMod;
import joshie.crafting.api.Bus;
import joshie.crafting.api.CraftingAPI;
import joshie.crafting.api.ICriteria;
import joshie.crafting.api.IReward;
import joshie.crafting.api.crafting.CraftingEvent.CanCraftItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanRepairItemEvent;
import joshie.crafting.api.crafting.CraftingEvent.CanUseItemCraftingEvent;
import joshie.crafting.api.crafting.CraftingEvent.CraftingType;
import joshie.crafting.api.crafting.ICrafter;
import joshie.crafting.crafting.CraftingRegistry;
import joshie.crafting.gui.GuiCriteriaEditor;
import joshie.crafting.gui.IItemSelectable;
import joshie.crafting.gui.SelectItemOverlay;
import joshie.crafting.gui.SelectItemOverlay.Type;
import joshie.crafting.gui.TextFieldHelper;
import joshie.crafting.helpers.ClientHelper;
import joshie.crafting.helpers.CraftingHelper;
import joshie.crafting.helpers.ItemHelper;
import joshie.crafting.helpers.StackHelper;
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

public class RewardCrafting extends RewardBase implements IItemSelectable {
    private TextFieldHelper editMod;
    private ItemStack stack = new ItemStack(Blocks.furnace);
    private CraftingType type = CraftingType.CRAFTING;
    private boolean matchDamage = true;
    private boolean matchNBT = false;
    private boolean usage = true;
    private boolean crafting = true;
    public String modid = "IGNORE";

    public RewardCrafting() {
        super("Allow Action", 0xFF0085B2, "crafting");
        editMod = new TextFieldHelper("modid", this);
    }

    @Override
    public IReward newInstance() {
        return new RewardCrafting();
    }

    @Override
    public Bus getBusType() {
        return Bus.FORGE;
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
            Collection<ICriteria> requirements = CraftingAPI.crafting.getCraftingCriteria(type, event.entityPlayer.getCurrentEquippedItem());
            if (requirements.size() > 0) {
                for (ICriteria c : requirements) {
                    GuiCriteriaEditor.INSTANCE.selected = c;
                    break;
                }

                event.entityPlayer.openGui(CraftingMod.instance, 1, null, 0, 0, 0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        ICrafter crafter = CraftingAPI.crafting.getCrafterFromPlayer(event.entityPlayer);
        if (!crafter.canCraftItem(CraftingType.CRAFTING, event.itemStack)) {
            boolean hasStuff = false;
            for (CraftingType type : CraftingType.craftingTypes) {
                Collection<ICriteria> requirements = CraftingAPI.crafting.getCraftingCriteria(type, event.itemStack);
                if (requirements.size() > 0) {
                    if (!hasStuff) {
                        event.toolTip.clear();
                        event.toolTip.add(EnumChatFormatting.RED + event.itemStack.getDisplayName());
                        event.toolTip.add("Currently Locked");
                        hasStuff = true;
                    }

                    event.toolTip.add(EnumChatFormatting.WHITE + type.getDisplayName());
                    for (ICriteria c : requirements) {
                        c.addTooltip(event.toolTip);
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
                if (isEventCancelled(player, CraftingType.ENTITY, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(BreakSpeed event) {
        if (isEventCancelled(event.entityPlayer, CraftingType.BREAKBLOCK, event.entityPlayer.getCurrentEquippedItem(), ItemHelper.getStackFromBlockData(event.block, event.metadata))) {
            event.newSpeed = 0F;
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (isEventCancelled(player, CraftingType.BREAKBLOCK, player.getCurrentEquippedItem(), ItemHelper.getStackFromBlockData(event.block, event.blockMetadata))) {
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
                if (isEventCancelled(player, CraftingType.HARVEST, player.getCurrentEquippedItem(), stack)) {
                    it.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttemptToUseItemForCrafting(CanUseItemCraftingEvent event) {
        if (event.stack == null) return;
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (crafter.canCraftWithAnything()) return;
        if (!crafter.canUseItemForCrafting(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToRepairItem(CanRepairItemEvent event) {
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (!crafter.canRepairItem(event.stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttemptToCraftItem(CanCraftItemEvent event) {        
        if (event.stack == null) return;
        ICrafter crafter = event.player != null ? CraftingAPI.crafting.getCrafterFromPlayer(event.player) : CraftingAPI.crafting.getCrafterFromTile(event.tile);
        if (!crafter.canCraftItem(event.type, event.stack)) {
            event.setCanceled(true);
        }
    }

    private boolean isEventCancelled(EntityPlayer player, CraftingType type, ItemStack usageStack, ItemStack craftingStack) {
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
        ICrafter crafter = CraftingAPI.crafting.getCrafterFromPlayer(player);
        if (!crafter.canCraftItem(CraftingType.CRAFTING, player.getCurrentEquippedItem())) {
            event.setCanceled(true);
            return false;
        } else return true;
    }

    @Override
    public IReward deserialize(JsonObject data) {
        RewardCrafting reward = new RewardCrafting();
        reward.stack = StackHelper.getStackFromString(data.get("item").getAsString());
        if (data.get("craftingType") != null) {
            String craftingtype = data.get("craftingType").getAsString();
            for (CraftingType type : CraftingType.craftingTypes) {
                if (type.name.equalsIgnoreCase(craftingtype)) {
                    reward.type = type;
                    break;
                }
            }
        }

        if (data.get("matchDamage") != null) {
            reward.matchDamage = data.get("matchDamage").getAsBoolean();
        }

        if (data.get("matchNBT") != null) {
            reward.matchNBT = data.get("matchNBT").getAsBoolean();
        }

        if (data.get("disableCrafting") != null) {
            reward.crafting = data.get("disableCrafting").getAsBoolean();
        }

        if (data.get("disableUsage") != null) {
            reward.usage = data.get("disableUsage").getAsBoolean();
        }

        if (data.get("modid") != null) {
            reward.modid = data.get("modid").getAsString();
        }

        if (CraftingMod.NEI_LOADED) {
            if (data.get("hideFromNEI") != null) {
                if (data.get("hideFromNEI").getAsBoolean() == false) {
                    reward.isAdded = false;
                    API.hideItem(stack);
                }
            }
        }

        return reward;
    }

    @Override
    public void serialize(JsonObject elements) {
        elements.addProperty("item", StackHelper.getStringFromStack(stack));
        if (type != CraftingType.CRAFTING) {
            elements.addProperty("craftingType", type.name.toLowerCase());
        }

        if (matchDamage != true) {
            elements.addProperty("matchDamage", matchDamage);
        }

        if (matchNBT != false) {
            elements.addProperty("matchNBT", matchNBT);
        }

        if (crafting != true) {
            elements.addProperty("disableCrafting", false);
        }

        if (usage != true) {
            elements.addProperty("disableUsage", false);
        }

        if (!modid.equals("IGNORE")) {
            elements.addProperty("modid", modid);
        }
    }

    private boolean isAdded = true;

    @Override
    public void reward(UUID uuid) {
        if (CraftingMod.NEI_LOADED && !isAdded) {
            API.addItemListEntry(stack);
            isAdded = true;
        }
    }

    @Override
    public void onAdded() {
        CraftingAPI.crafting.addRequirement(type, modid, stack, matchDamage, matchNBT, usage, crafting, criteria);
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

    public CraftingType next() {
        int id = type.id + 1;
        if (id < CraftingType.craftingTypes.size()) {
            return CraftingType.craftingTypes.get(id);
        }

        return CraftingType.craftingTypes.get(0);
    }

    @Override
    public Result clicked() {
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
    public void draw() {
        drawStack(getIcon(), 76, 44, 1.4F);
        int typeColor = 0xFFFFFFFF;
        int matchColor = 0xFFFFFFFF;
        int match2Color = 0xFFFFFFFF;
        int usageColor = 0xFFFFFFFF;
        int craftColor = 0xFFFFFFFF;
        int modColor = 0xFFFFFFFF;

        if (ClientHelper.canEdit()) {
            if (mouseX <= 84 && mouseX >= 1) {
                if (mouseY >= 17 && mouseY <= 25) typeColor = 0xFFBBBBBB;
                if (mouseY > 25 && mouseY <= 33) matchColor = 0xFFBBBBBB;
                if (mouseY > 34 && mouseY <= 41) match2Color = 0xFFBBBBBB;
                if (mouseY > 42 && mouseY <= 50) usageColor = 0xFFBBBBBB;
                if (mouseY > 50 && mouseY <= 58) craftColor = 0xFFBBBBBB;
                if (mouseY > 58 && mouseY <= 66) modColor = 0xFFBBBBBB;
            }
        }
        
        drawText("type: " + type.name.toLowerCase(), 4, 18, typeColor);
        drawText("matchDamage: " + matchDamage, 4, 26, matchColor);
        drawText("matchNBT: " + matchNBT, 4, 34, match2Color);
        drawText("usage: " + usage, 4, 42, usageColor);
        drawText("crafting: " + crafting, 4, 50, craftColor);
        drawText("modid: " + editMod.getText(), 4, 58, modColor);
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
