package joshie.progression;

import joshie.progression.api.ProgressionAPI;
import joshie.progression.commands.CommandManager;
import joshie.progression.gui.fields.FieldRegistry;
import joshie.progression.gui.filters.FilterSelectorHelper;
import joshie.progression.handlers.APIHandler;
import joshie.progression.handlers.ProgressionEvents;
import joshie.progression.handlers.RuleHandler;
import joshie.progression.json.Options;
import joshie.progression.network.PacketHandler;
import joshie.progression.player.PlayerHandler;
import joshie.progression.player.PlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static joshie.progression.ItemProgression.ItemMeta.book;
import static joshie.progression.ItemProgression.getStackFromMeta;
import static net.minecraft.init.Items.FLINT;

public class PCommonProxy implements IGuiHandler {
    public void preInit(ASMDataTable asm) {
      //Create the API

        ProgressionAPI.registry = new APIHandler();
        ProgressionAPI.player = new PlayerHandler();
        ProgressionAPI.filters = new FilterSelectorHelper();
        ProgressionAPI.fields = new FieldRegistry();

        //Register Handlers
        MinecraftForge.EVENT_BUS.register(CommandManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
        MinecraftForge.EVENT_BUS.register(new ProgressionEvents());

        //Register the items
        Progression.item = (ItemProgression) new ItemProgression().setUnlocalizedName("item").setRegistryName("item");
        GameRegistry.register(Progression.item);

        GameRegistry.addRecipe(new ShapedOreRecipe(getStackFromMeta(book), "FS", "PP", 'P', Items.PAPER, 'S', Items.STRING, 'F', FLINT));
        if (Options.tileClaimerRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Progression.item, 1, ItemProgression.ItemMeta.claim.ordinal()), new Object[] { "F", "P", 'F', Items.FLINT, 'P', "plankWood" }));
        }

        RuleHandler.registerRules(asm);
        CommandManager.registerCommands(asm);
        PacketHandler.registerPackets(asm);

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
        ProgressionAPI.registry.registerDamageSource(DamageSource.flyIntoWall);
        ProgressionAPI.registry.registerDamageSource(DamageSource.dragonBreath);
        NetworkRegistry.INSTANCE.registerGuiHandler(Progression.instance, Progression.proxy);
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
