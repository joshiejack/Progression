package joshie.crafting.helpers;

import joshie.crafting.data.DataClient;
import joshie.crafting.data.DataCommon;
import joshie.crafting.data.DataServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class DataHelper {
	public static DataCommon getData() {
		return (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)? DataClient.INSTANCE: DataServer.INSTANCE;
	}
}
