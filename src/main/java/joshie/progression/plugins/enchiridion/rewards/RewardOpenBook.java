package joshie.progression.plugins.enchiridion.rewards;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.book.IBook;
import joshie.progression.Progression;
import joshie.progression.api.special.IGetterCallback;
import joshie.progression.api.special.IInit;
import joshie.progression.criteria.rewards.RewardBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class RewardOpenBook extends RewardBase implements IInit, IGetterCallback {
    private transient IBook theBook;
    public String bookid = "";
    public int page = 1;

    public RewardOpenBook() {
        super("open.book", 0xFFCCCCCC);
    }

    @Override
    public void init() {
        theBook = EnchiridionAPI.instance.getBook(bookid);
    }

    @Override
    public String getField(String fieldName) {
        if (fieldName.equals("page")) return "" + page;
        else return theBook != null ? EnumChatFormatting.GREEN + bookid : EnumChatFormatting.RED + bookid;
    }

    @Override
    public void reward(EntityPlayerMP player) {
        if (theBook != null) {
            EnchiridionAPI.instance.openBook(player, bookid, page);
        }
    }

    @Override
    public String getDescription() {
        if (theBook != null) {
            return Progression.format(getUnlocalisedName() + ".description", theBook.getDisplayName(), page);
        } else return "Incorrectly setup book data";
    }
}
