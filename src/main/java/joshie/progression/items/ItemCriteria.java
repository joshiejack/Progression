package joshie.progression.items;

import joshie.progression.Progression;
import joshie.progression.crafting.Crafter;
import joshie.progression.crafting.CraftingRegistry;
import joshie.progression.crafting.CraftingUnclaimed;
import joshie.progression.criteria.Criteria;
import joshie.progression.handlers.APIHandler;
import joshie.progression.helpers.PlayerHelper;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCriteria extends Item {
    public static CreativeTabs tab;
    public static final int CRITERIA = 0;
    public static final int CLAIM = 1;
    public static final int BOOK = 2;
    //private IIcon padlock;
    //private IIcon book;

    public ItemCriteria() {
        final Item item = this;
        tab = new CreativeTabs("progression") {
            private ItemStack stack = new ItemStack(item, 1, BOOK);

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
                return BOOK;
            }
        };

        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(tab);
    }

    public static Criteria getCriteriaFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        return APIHandler.getCriteriaFromName(stack.getTagCompound().getString("Criteria"));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return "Progression Tile Claimer";
        } else if (stack.getItemDamage() == 2) {
            return "Progression Book";
        }

        Criteria criteria = getCriteriaFromStack(stack);
        return criteria == null ? "BROKEN ITEM" : criteria.displayName;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (stack.getItemDamage() == BOOK) player.openGui(Progression.instance, 0, null, 0, 0, 0);
        if (world.isRemote || player == null || stack == null) return true;
        if (stack.getItemDamage() == CLAIM) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                Crafter crafter = CraftingRegistry.getCrafterFromTile(tile);
                if (crafter == CraftingUnclaimed.INSTANCE) {
                    PlayerTracker.setTileOwner(tile, PlayerHelper.getUUIDForPlayer(player));
                    PacketHandler.sendToClient(new PacketClaimed(pos.getX(), pos.getY(), pos.getZ()), (EntityPlayerMP) player);
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getItemDamage() == BOOK) {
            player.openGui(Progression.instance, 0, null, 0, 0, 0);
        } else if (!world.isRemote) {
            Criteria criteria = getCriteriaFromStack(stack);
            if (criteria != null) {
                Result completed = PlayerTracker.getServerPlayer(PlayerHelper.getUUIDForPlayer(player)).getMappings().fireAllTriggers("forced-complete", criteria, criteria.rewards);
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
        if (stack.getItemDamage() == CLAIM) {
            list.add("Right click me on tiles");
            list.add("to claim them as yours");
        } else if (stack.getItemDamage() == BOOK && player.capabilities.isCreativeMode) {
            list.add("Right click me to open");
            list.add("'Progression editor'");
        }
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return damage == CLAIM ? padlock : book;
    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        padlock = register.registerIcon(ProgressionInfo.MODPATH + ":padlock");
        book = register.registerIcon(ProgressionInfo.MODPATH + ":book");
    }*/

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, CLAIM));
        list.add(new ItemStack(item, 1, BOOK));
        for (Criteria c : APIHandler.getCriteria().values()) {
            ItemStack stack = new ItemStack(item);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Criteria", c.uniqueName);
            list.add(stack);
        }
    }
}