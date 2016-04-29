package joshie.progression.plugins.enchiridion.actions;

import com.google.gson.JsonObject;
import joshie.enchiridion.gui.book.buttons.actions.AbstractAction;
import joshie.progression.Progression;
import joshie.progression.helpers.JSONHelper;
import joshie.progression.lib.PInfo;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public abstract class AbstractActionProgression extends AbstractAction {
    protected transient UUID uuid = UUID.randomUUID();
    public String display = "New";

    public AbstractActionProgression() {}
    public AbstractActionProgression(String name) {
        super(name);
        this.resource = new ResourceLocation(PInfo.BOOKPATH + name + ".png");
    }

    public AbstractActionProgression copyAbstract(AbstractActionProgression action) {
        super.copyAbstract(action);
        action.uuid = uuid;
        action.display = display;
        return this;
    }

    @Override
    public void readFromJson(JsonObject object) {
        super.readFromJson(object);

        try {
            uuid = UUID.fromString(JSONHelper.getString(object, "uuid", "d977334a-a7e9-5e43-b87e-91df8eebfdff"));
        } catch (Exception e){}
    }

    @Override
    public void writeToJson(JsonObject object) {
        super.writeToJson(object);

        if (uuid != null) {
            JSONHelper.setString(object, "uuid", uuid.toString(), "d977334a-a7e9-5e43-b87e-91df8eebfdff");
        }
    }

    @Override
    public String getName() {
        return Progression.translate("action." + name);
    }
}
