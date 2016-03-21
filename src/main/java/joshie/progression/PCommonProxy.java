package joshie.progression;

import java.io.File;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.CommandEdit;
import joshie.progression.commands.CommandHelp;
import joshie.progression.commands.CommandManager;
import joshie.progression.commands.CommandReload;
import joshie.progression.commands.CommandReset;
import joshie.progression.criteria.conditions.ConditionAchievement;
import joshie.progression.criteria.conditions.ConditionBiomeType;
import joshie.progression.criteria.conditions.ConditionBoolean;
import joshie.progression.criteria.conditions.ConditionCoordinates;
import joshie.progression.criteria.conditions.ConditionDaytime;
import joshie.progression.criteria.conditions.ConditionHasCriteria;
import joshie.progression.criteria.conditions.ConditionHasPotionEffect;
import joshie.progression.criteria.conditions.ConditionInInventory;
import joshie.progression.criteria.conditions.ConditionPoints;
import joshie.progression.criteria.conditions.ConditionRandom;
import joshie.progression.criteria.filters.block.FilterBlock;
import joshie.progression.criteria.filters.block.FilterBlockMod;
import joshie.progression.criteria.filters.block.FilterBlockOre;
import joshie.progression.criteria.filters.block.FilterBlockStack;
import joshie.progression.criteria.filters.block.FilterBlockState;
import joshie.progression.criteria.filters.crafting.FilterExact;
import joshie.progression.criteria.filters.entity.FilterEntityName;
import joshie.progression.criteria.filters.entity.FilterSkeletonType;
import joshie.progression.criteria.filters.item.FilterItem;
import joshie.progression.criteria.filters.item.FilterItemMeta;
import joshie.progression.criteria.filters.item.FilterItemMod;
import joshie.progression.criteria.filters.item.FilterItemNBT;
import joshie.progression.criteria.filters.item.FilterItemOre;
import joshie.progression.criteria.filters.item.FilterItemStack;
import joshie.progression.criteria.filters.location.FilterPlayerLocation;
import joshie.progression.criteria.filters.location.FilterRandomAround;
import joshie.progression.criteria.filters.potion.FilterPotionEffect;
import joshie.progression.criteria.rewards.RewardBoolean;
import joshie.progression.criteria.rewards.RewardClear;
import joshie.progression.criteria.rewards.RewardCommand;
import joshie.progression.criteria.rewards.RewardCraftability;
import joshie.progression.criteria.rewards.RewardCriteria;
import joshie.progression.criteria.rewards.RewardFallDamage;
import joshie.progression.criteria.rewards.RewardItem;
import joshie.progression.criteria.rewards.RewardPlaceBlock;
import joshie.progression.criteria.rewards.RewardPoints;
import joshie.progression.criteria.rewards.RewardPotion;
import joshie.progression.criteria.rewards.RewardShowTab;
import joshie.progression.criteria.rewards.RewardSpawnEntity;
import joshie.progression.criteria.rewards.RewardSpeed;
import joshie.progression.criteria.rewards.RewardStepAssist;
import joshie.progression.criteria.rewards.RewardTeleport;
import joshie.progression.criteria.rewards.RewardTime;
import joshie.progression.criteria.triggers.TriggerAchievement;
import joshie.progression.criteria.triggers.TriggerBoolean;
import joshie.progression.criteria.triggers.TriggerBreakBlock;
import joshie.progression.criteria.triggers.TriggerChangeDimension;
import joshie.progression.criteria.triggers.TriggerChat;
import joshie.progression.criteria.triggers.TriggerClickBlock;
import joshie.progression.criteria.triggers.TriggerCrafting;
import joshie.progression.criteria.triggers.TriggerItemEaten;
import joshie.progression.criteria.triggers.TriggerKill;
import joshie.progression.criteria.triggers.TriggerLogin;
import joshie.progression.criteria.triggers.TriggerObtain;
import joshie.progression.criteria.triggers.TriggerPoints;
import joshie.progression.criteria.triggers.TriggerTick;
import joshie.progression.gui.fields.FieldRegistry;
import joshie.progression.gui.selector.filters.FilterSelectorHelper;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.CraftingEvents;
import joshie.progression.handlers.RemappingHandler;
import joshie.progression.items.ItemCriteria;
import joshie.progression.json.Options;
import joshie.progression.network.PacketClaimed;
import joshie.progression.network.PacketCompleted;
import joshie.progression.network.PacketFireTrigger;
import joshie.progression.network.PacketHandler;
import joshie.progression.network.PacketOpenEditor;
import joshie.progression.network.PacketReload;
import joshie.progression.network.PacketReset;
import joshie.progression.network.PacketRewardItem;
import joshie.progression.network.PacketSyncAbilities;
import joshie.progression.network.PacketSyncCriteria;
import joshie.progression.network.PacketSyncCustomData;
import joshie.progression.network.PacketSyncImpossible;
import joshie.progression.network.PacketSyncJSONToClient;
import joshie.progression.network.PacketSyncJSONToServer;
import joshie.progression.network.PacketSyncPoints;
import joshie.progression.network.PacketSyncTeam;
import joshie.progression.network.PacketSyncTriggers;
import joshie.progression.player.PlayerHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class PCommonProxy implements IGuiHandler {
    public static Item item;

    public void preInit() {
        Options.init(new Configuration(new File(Progression.root, "options.cfg")));
        RemappingHandler.resetRegistries();

        //Create the API
        ProgressionAPI.registry = new APIHandler();
        ProgressionAPI.player = new PlayerHandler();
        ProgressionAPI.filters = new FilterSelectorHelper();
        ProgressionAPI.fields = new FieldRegistry();

        //Register Handlers
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(new CraftingEvents());

        //Register the items
        PCommonProxy.item = new ItemCriteria().setUnlocalizedName("item");
        GameRegistry.registerItem(PCommonProxy.item, "item");

        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PCommonProxy.item, 1, ItemCriteria.CLAIM), new Object[] { "F", "P", 'F', Items.flint, 'P', "plankWood" }));
        }

        registerFilters();
        registerTriggers();
        registerConditions();
        registerRewards();
        
        //Register Commands
        CommandManager.INSTANCE.registerCommand(new CommandHelp());
        CommandManager.INSTANCE.registerCommand(new CommandEdit());
        CommandManager.INSTANCE.registerCommand(new CommandReload());
        CommandManager.INSTANCE.registerCommand(new CommandReset());

        //Register Packets
        PacketHandler.registerPacket(PacketSyncTriggers.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCriteria.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncImpossible.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncAbilities.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncPoints.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketSyncCustomData.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketRewardItem.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketClaimed.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketCompleted.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketOpenEditor.class, Side.CLIENT);
        PacketHandler.registerPacket(PacketFireTrigger.class, Side.SERVER);
        PacketHandler.registerPacket(PacketSyncTeam.class);
        PacketHandler.registerPacket(PacketReload.class);
        PacketHandler.registerPacket(PacketReset.class);
        PacketHandler.registerPacket(PacketSyncJSONToClient.class);
        PacketHandler.registerPacket(PacketSyncJSONToServer.class);
        
    }

    private void registerFilters() {
        //Item Filters
        ProgressionAPI.registry.registerItemFilter(new FilterItemStack());
        ProgressionAPI.registry.registerItemFilter(new FilterItem());
        ProgressionAPI.registry.registerItemFilter(new FilterItemMeta());
        ProgressionAPI.registry.registerItemFilter(new FilterItemNBT());
        ProgressionAPI.registry.registerItemFilter(new FilterItemMod());
        ProgressionAPI.registry.registerItemFilter(new FilterItemOre());

        //Block Filters
        ProgressionAPI.registry.registerItemFilter(new FilterBlockStack());
        ProgressionAPI.registry.registerItemFilter(new FilterBlock());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockState());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockMod());
        ProgressionAPI.registry.registerItemFilter(new FilterBlockOre());

        //Potion Filters
        ProgressionAPI.registry.registerItemFilter(new FilterPotionEffect());

        //Entity Filters
        ProgressionAPI.registry.registerItemFilter(new FilterEntityName());
        ProgressionAPI.registry.registerItemFilter(new FilterSkeletonType());

        //Location Filters
        ProgressionAPI.registry.registerItemFilter(new FilterPlayerLocation());
        ProgressionAPI.registry.registerItemFilter(new FilterRandomAround());

        //Crafting Filters
        ProgressionAPI.registry.registerItemFilter(new FilterExact());
    }
    
    private void registerTriggers() {
        ProgressionAPI.registry.registerTriggerType(new TriggerBreakBlock());
        ProgressionAPI.registry.registerTriggerType(new TriggerCrafting());
        ProgressionAPI.registry.registerTriggerType(new TriggerItemEaten());
        ProgressionAPI.registry.registerTriggerType(new TriggerKill());
        ProgressionAPI.registry.registerTriggerType(new TriggerLogin());
        ProgressionAPI.registry.registerTriggerType(new TriggerObtain());
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
