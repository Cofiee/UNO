package game.myAssets;

import game.myAssets.cards.ACard;
import game.myAssets.cards.TakeFourCard;
import game.myAssets.cards.TakeTwoCard;
import javafx.scene.Parent;

import java.util.Vector;

public class Player
{
    String name;
    Vector<ACard> hand;
    boolean Frozen;

    public Player(){this.hand = new Vector<>();}
    public String getName()
    {
        return name;
    }
    public Vector<ACard> getHand()
    {
        return hand;
    }
    public boolean isFrozen()
    {
        return Frozen;
    }
    public void unfreeze()
    {
        Frozen = false;
    }
    public void freeze()
    {
        Frozen = true;
    }
    public int isTakeTwoCardPossessed()
    {
        int i = 0;
        for (ACard card: hand)
        {
            if(card instanceof TakeTwoCard)
                return i;
            ++i;
        }
        return -1;
    }
    public int isTakeFourCardPossessed()
    {
        int i = 0;
        for (ACard card: hand)
        {
            if(card instanceof TakeTwoCard)
                return i;
            ++i;
        }
        return -1;
    }
}
