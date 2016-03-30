package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.*;
import joshie.progression.criteria.conditions.*;
import joshie.progression.criteria.filters.block.*;
import joshie.progression.criteria.filters.crafting.FilterExact;
import joshie.progression.criteria.filters.entity.FilterEntityDisplayName;
import joshie.progression.criteria.filters.entity.FilterEntityName;
import joshie.progression.criteria.filters.entity.FilterEntityType;
import joshie.progression.criteria.filters.entity.FilterSkeletonType;
import joshie.progression.criteria.filters.item.*;
import joshie.progression.criteria.filters.location.*;
import joshie.progression.criteria.filters.potion.FilterPotionEffect;
import joshie.progression.criteria.rewards.*;
import joshie.progression.criteria.triggers.*;
import joshie.progression.gui.fields.FieldRegistry;
import joshie.progression.gui.filters.FilterSelectorHelper;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.CraftingEvents;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.items.ItemCriteria;
import joshie.progression.json.Options;
import joshie.progression.network.*;
import joshie.progression.player.PlayerHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class PCommonProxy implements IGuiHandler {
    public void preInit() {
      //Create the API
        RemappingHandler.resetRegistries();
        ProgressionAPI.registry = new APIHandler();
        ProgressionAPI.player = new PlayerHandler();
        ProgressionAPI.filters = new FilterSelectorHelper();
        ProgressionAPI.fields = new FieldRegistry();

        //Register Handlers
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(new CraftingEvents());

        //Register the items
        Progression.item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(Progression.item, "item");

        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Progression.item, 1, ItemCriteria.ItemMeta.claim.ordinal()), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }

        registerFilters();
        registerTriggers();
        registerConditions();
        registerRewards();
        
        //Register Commands
        CommandManager.INSTANCE.registerCommand(new CommandHelp());
        CommandManager.INSTANCE.registerCommand(new CommandEdit());
        CommandManager.INSTANCE.registerCommand(new CommandGui());
        CommandManager.INSTANCE.registerCommand(new CommandReload());
        CommandManager.INSTANCE.registerCommand(new CommandReset());

        //Register Packets
        PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCriteria.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncImpossible.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncAbilities.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncPoints.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCustomData.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncTriggerData.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketRewardItem.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketClaimed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenEditor.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketDebugGUI.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketFireTrigger.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncTeam.class);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        PacketHandler.registerPacket(PacketSyncJSONToClient.class);
        PacketHandler.registerPacket(PacketSyncJSONToServer.class);

        //Register DamageSources
        ProgressionAPI.registry.registerDamageSource(DamageSource.anvil);
        ProgressionAPI.registry.registerDamageSource(DamageSource.cactus);
        ProgressionAPI.registry.registerDamageSource(DamageSource.drown);
        ProgressionAPI.registry.registerDamageSource(DamageSource.fall);
        ProgressionAPI.registry.registerDamageSource(DamageSource.fallingBlock);
        ProgressionAPI.registry.registerDamageSource(DamageSource.generic);
        ProgressionAPI.registry.registerDamageSource(DamageSource.inFire);
        ProgressionAPI.registry.registerDamageSource(DamageSource.inWall);
        ProgressionAPI.registry.registerDamageSource(DamageSource.lava);
        ProgressionAPI.registry.registerDamageSource(DamageSource.lightningBolt);
        ProgressionAPI.registry.registerDamageSource(DamageSource.magic);
        ProgressionAPI.registry.registerDamageSource(DamageSource.onFire);
        ProgressionAPI.registry.registerDamageSource(DamageSource.outOfWorld);
        ProgressionAPI.registry.registerDamageSource(DamageSource.starve);
        ProgressionAPI.registry.registerDamageSource(DamageSource.wither);
        NetworkRegistry.INSTANCE.registerGuiHandler(Progression.instance, Progression.proxy);
    }

    private void registerFilters() {
        //Item Filters
        ProgressionAPI.registry.registerFilter(new FilterItemStack());
        ProgressionAPI.registry.registerFilter(new FilterItem());
        ProgressionAPI.registry.registerFilter(new FilterItemMeta());
        ProgressionAPI.registry.registerFilter(new FilterItemNBT());
        ProgressionAPI.registry.registerFilter(new FilterItemNBTFuzzy());
        ProgressionAPI.registry.registerFilter(new FilterItemMod());
        ProgressionAPI.registry.registerFilter(new FilterItemOre());

        //Block Filters
        ProgressionAPI.registry.registerFilter(new FilterBlockStack());
        ProgressionAPI.registry.registerFilter(new FilterBlock());
        ProgressionAPI.registry.registerFilter(new FilterBlockState());
        ProgressionAPI.registry.registerFilter(new FilterBlockMod());
        ProgressionAPI.registry.registerFilter(new FilterBlockOre());

        //Potion Filters
        ProgressionAPI.registry.registerFilter(new FilterPotionEffect());

        //Entity Filterss
        ProgressionAPI.registry.registerFilter(new FilterEntityName());
        ProgressionAPI.registry.registerFilter(new FilterEntityDisplayName());
        ProgressionAPI.registry.registerFilter(new FilterEntityType());
        ProgressionAPI.registry.registerFilter(new FilterSkeletonType());

        //Location Filters
        ProgressionAPI.registry.registerFilter(new FilterPlayerLocationAround());
        ProgressionAPI.registry.registerFilter(new FilterPlayerLocationAbove());
        ProgressionAPI.registry.registerFilter(new FilterPlayerLocationLooking());
        ProgressionAPI.registry.registerFilter(new FilterPlayerLastBroken());
        ProgressionAPI.registry.registerFilter(new FilterRandomAround());
        MinecraftForge.EVENT_BUS.register(new FilterPlayerLastBroken());

        //Crafting Filters
        ProgressionAPI.registry.registerFilter(new FilterExact());
    }
    
    private void registerTriggers() {
        ProgressionAPI.registry.registerTriggerType(new TriggerBreakBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerCrafting());
        ProgressionAPI.registry.registerTriggerType(new TriggerItemEaten());
        ProgressionAPI.registry.registerTriggerType(new TriggerKill());
        ProgressionAPI.registry.registerTriggerType(new TriggerLogin());
        ProgressionAPI.registry.registerTriggerType(new TriggerChangeGui());
        ProgressionAPI.registry.registerTriggerType(new TriggerBoolean());
        ProgressionAPI.registry.registerTriggerType(new TriggerClickBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerPoints());
        ProgressionAPI.registry.registerTriggerType(new TriggerChangeDimension());
        ProgressionAPI.registry.registerTriggerType(new TriggerAchievement());
        ProgressionAPI.registry.registerTriggerType(new TriggerTick());
        ProgressionAPI.registry.registerTriggerType(new TriggerChat());
    }
    
    private void registerConditions() {
        ProgressionAPI.registry.registerConditionType(new ConditionBiomeType());
        ProgressionAPI.registry.registerConditionType(new ConditionRandom());
        ProgressionAPI.registry.registerConditionType(new ConditionCoordinates());
        ProgressionAPI.registry.registerConditionType(new ConditionDaytime());
        ProgressionAPI.registry.registerConditionType(new ConditionInInventory());
        ProgressionAPI.registry.registerConditionType(new ConditionHasPotionEffect());
        ProgressionAPI.registry.registerConditionType(new ConditionHasCriteria());
        ProgressionAPI.registry.registerConditionType(new ConditionBoolean());
        ProgressionAPI.registry.registerConditionType(new ConditionPoints());
        ProgressionAPI.registry.registerConditionType(new ConditionAchievement());
    }
    
    private void registerRewards() {
        ProgressionAPI.registry.registerRewardType(new RewardCommand());
        ProgressionAPI.registry.registerRewardType(new RewardCriteria());
        ProgressionAPI.registry.registerRewardType(new RewardFallDamage());
        ProgressionAPI.registry.registerRewardType(new RewardItem());
        ProgressionAPI.registry.registerRewardType(new RewardBoolean());
        ProgressionAPI.registry.registerRewardType(new RewardPoints());
        ProgressionAPI.registry.registerRewardType(new RewardSpeed());
        ProgressionAPI.registry.registerRewardType(new RewardTime()); 
        ProgressionAPI.registry.registerRewardType(new RewardCraftability());
        ProgressionAPI.registry.registerRewardType(new RewardClear());
        ProgressionAPI.registry.registerRewardType(new RewardPotion());
        ProgressionAPI.registry.registerRewardType(new RewardPlaceBlock());
        ProgressionAPI.registry.registerRewardType(new RewardTeleport());
        ProgressionAPI.registry.registerRewardType(new RewardSpawnEntity());
        ProgressionAPI.registry.registerRewardType(new RewardShowTab());
        ProgressionAPI.registry.registerRewardType(new RewardStepAssist());
        ProgressionAPI.registry.registerRewardType(new RewardHurt());
        ProgressionAPI.registry.registerRewardType(new RewardSpawnItem());
    }

    public void initClient() {}
    public void registerRendering() {}
    
    //Gui Handling
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
