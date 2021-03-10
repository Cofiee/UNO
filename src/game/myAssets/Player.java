package game.myAssets;

import game.myAssets.cards.RegularCard;

import java.util.Vector;

public class Player
{
    String name;
    Vector<RegularCard> hand;

    boolean Frozen;

    boolean isAI;
    public String getName()
    {
        return name;
    }
    public Vector<RegularCard> getHand()
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
}
