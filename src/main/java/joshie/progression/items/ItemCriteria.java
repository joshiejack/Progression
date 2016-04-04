package joshie.progression.items;

import joshie.progression.Progression;
import joshie.progression.api.criteria.ICriteria;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.crafting.CraftingUnclaimed;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.MCClientHelper;
import joshie.progression.helpers.PlayerHelper;
import joshie.progression.lib.GuiIDs;
import joshie.progression.network.PacketClaimed;
import joshie.progression.network.PacketHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class ItemCriteria extends Item {
    public static ItemStack getStackFromMeta(ItemMeta meta) {
        return new ItemStack(Progression.item, 1, meta.ordinal());
    }

    public static enum ItemMeta {
        criteria, claim, book, edit, booleanValue, clearInventory, clearOrReceiveOrBlockCriteria, fallResistance,
        ifCriteriaCompleted, ifDayOrNight, ifHasAchievement, ifHasBoolean, ifHasPoints, ifIsAtCoordinates,
        ifIsBiome, ifRandom, onChangeDimension, onLogin, onReceivedAchiement, onReceivedBoolean,
        onReceivedPoints, onSecond, onSentMessage, points, speed, showTab, showLayer, sun, moon, stepAssist,
        attackPlayer, onGUIChange;
    }

    public static CreativeTabs tab;

    public ItemCriteria() {
        final Item item = this;
        tab = new CreativeTabs("progression") {
            private ItemStack stack = new ItemStack(item, 1, ItemMeta.book.ordinal());

            @Override
            public String getTranslatedTabLabel() {
                return "Progression";
            }

            @Override
            public boolean hasSearchBar() {
                return true;
            }

            @Override
            public Item getTabIconItem() {
                return item;
            }

            @Override
            public int getIconItemDamage() {
                return ItemMeta.book.ordinal();
            }
        };

        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(tab);
    }

    public static ICriteria getCriteriaFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        if (stack.getItemDamage() != ItemMeta.criteria.ordinal()) return null;
        String uuid = stack.getTagCompound().getString("Criteria");
        if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
            return APIHandler.getCriteriaFromName(UUID.fromString(uuid));
        } else return null;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() == ItemMeta.criteria.ordinal()) {
            ICriteria criteria = getCriteriaFromStack(stack);
            return criteria == null ? "BROKEN ITEM" : criteria.getLocalisedName();
        } else return Progression.translate("item." + ItemMeta.values()[Math.min(ItemMeta.values().length - 1, stack.getItemDamage())].name());
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        ICriteria criteria = getCriteriaFromStack(stack);
        if (criteria != null) {
            return criteria.getIcon().getItem().getColorFromItemStack(criteria.getIcon(), renderPass);
        }
        
        return 16777215;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (stack.getItemDamage() == ItemMeta.book.ordinal() || stack.getItemDamage() == ItemMeta.edit.ordinal()) {
            int guiid = player.isSneaking() ? GuiIDs.GROUP : GuiIDs.EDITOR;
            if (world.isRemote) {
                if (stack.getItemDamage() == ItemMeta.edit.ordinal()) MCClientHelper.FORCE_EDIT = true;
                else MCClientHelper.FORCE_EDIT = false;
            }

            player.openGui(Progression.instance, guiid, null, 0, 0, 0);
        }

        if (world.isRemote || player == null || stack == null) return true;
        if (stack.getItemDamage() == ItemMeta.claim.ordinal()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                Crafter crafter = CraftingRegistry.getCrafterFromTile(tile);
                if (crafter == CraftingUnclaimed.INSTANCE) {
                    PlayerTracker.setTileOwner(tile, PlayerHelper.getUUIDForPlayer(player));
                    PacketHandler.sendToClient(new PacketClaimed(pos.getX(), pos.getY(), pos.getZ()), (EntityPlayerMP) player);
                }
            }
        } else {
            ICriteria criteria = getCriteriaFromStack(stack);
            if (criteria != null) {
                Result completed = PlayerTracker.getServerPlayer(PlayerHelper.getUUIDForPlayer(player)).getMappings().fireAllTriggers("forced-complete", criteria);
                if (!player.capabilities.isCreativeMode && completed == Result.ALLOW) {
                    stack.stackSize--;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == ItemMeta.book.ordinal() || stack.getItemDamage() == ItemMeta.edit.ordinal()) {
            int guiid = player.isSneaking() ? GuiIDs.GROUP : GuiIDs.EDITOR;
            if (world.isRemote) {
                if (stack.getItemDamage() == ItemMeta.edit.ordinal()) MCClientHelper.FORCE_EDIT = true;
                else MCClientHelper.FORCE_EDIT = false;
            }

            player.openGui(Progression.instance, guiid, null, 0, 0, 0);
        } else if (!world.isRemote) {
            ICriteria criteria = getCriteriaFromStack(stack);
            if (criteria != null) {
                Result completed = PlayerTracker.getServerPlayer(PlayerHelper.getUUIDForPlayer(player)).getMappings().fireAllTriggers("forced-complete", criteria);
                if (!player.capabilities.isCreativeMode && completed == Result.ALLOW) {
                    stack.stackSize--;
                }
            }
        }

        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (stack.getItemDamage() == ItemMeta.claim.ordinal()) {
            list.add("Right click me on tiles");
            list.add("to claim them as yours");
        } else if (stack.getItemDamage() == ItemMeta.book.ordinal() || stack.getItemDamage() == ItemMeta.edit.ordinal()) {
            list.add(EnumChatFormatting.ITALIC + "Hold Shift to Edit Team");
            if (player.capabilities.isCreativeMode || stack.getItemDamage() == ItemMeta.edit.ordinal()) {
                list.add("");
                list.add("Right click me to open");
                list.add("'Progression editor'");
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        /*for (ItemMeta meta: ItemMeta.values()) {
            if (meta == ItemMeta.criteria) continue;
            else list.add(new ItemStack(item, 1, meta.ordinal()));
        } */

        list.add(new ItemStack(item, 1, ItemMeta.book.ordinal()));
        list.add(new ItemStack(item, 1, ItemMeta.edit.ordinal()));
        list.add(new ItemStack(item, 1, ItemMeta.claim.ordinal()));

        for (ICriteria c : APIHandler.getCriteria().values()) {
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Criteria", c.getUniqueID().toString());
            list.add(stack);
        }
    }
}