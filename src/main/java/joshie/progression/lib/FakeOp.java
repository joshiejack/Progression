package joshie.progression.lib;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class FakeOp extends FakePlayer {
    private static FakeOp fake = null;

    public static FakeOp getInstance() {
        if (fake == null) {
            fake = new FakeOp(DimensionManager.getWorld(0), new GameProfile(UUID.fromString("3f9dede3-80a8-4782-b97f-41a11bc34339"), "Progression"));
        }

        return fake;
    }

    public FakeOp(WorldServer world, GameProfile name) {
        super(world, name);
        playerNetServerHandler = new FakeNetHandler(FMLCommonHandler.instance().getMinecraftServerInstance(), this);
        addedToChunk = false;
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {
        return true;
    }
}
