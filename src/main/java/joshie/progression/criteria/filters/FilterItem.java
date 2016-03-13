package joshie.progression.criteria.filters;

import com.google.gson.JsonObject;

import joshie.progression.gui.fields.IItemGetterCallback;
import joshie.progression.gui.fields.IItemSetterCallback;
import joshie.progression.gui.fields.ItemField;
import joshie.progression.gui.newversion.overlays.FeatureItemSelector.Type;
import joshie.progression.helpers.JSONHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FilterItem extends FilterBase implements IItemGetterCallback, IItemSetterCallback {
    public Item item = Items.beef;

    public FilterItem() {
        super("itemOnly", 0xFFCCCCCC);
        list.add(new ItemField("item", this, 25, 25, 3F, 26, 70, 25, 75, Type.TRIGGER));
    }

    @Override
    public void readFromJSON(JsonObject data) {
        ResourceLocation deflt = Item.itemRegistry.getNameForObject(Items.beef);
        String domain = JSONHelper.getString(data, "domain", deflt.getResourceDomain());
        String path = JSONHelper.getString(data, "path", deflt.getResourcePath());
        item = Item.itemRegistry.getObject(new ResourceLocation(domain, path));
    }

    @Override
    public void writeToJSON(JsonObject data) {
        ResourceLocation location = Item.itemRegistry.getNameForObject(item);
        ResourceLocation deflt = Item.itemRegistry.getNameForObject(Items.beef);
        JSONHelper.setString(data, "domain", location.getResourceDomain(), deflt.getResourceDomain());
        JSONHelper.setString(data, "path", location.getResourcePath(), deflt.getResourcePath());
    }

    @Override
    public boolean matches(ItemStack check) {
    	return item == check.getItem();
    }
    
    @Override
    public ItemStack getItem(String fieldName) {
        return new ItemStack(item);
    }

    @Override
    public void setItem(String fieldName, ItemStack stack) {
        item = stack.getItem();
    }
}
