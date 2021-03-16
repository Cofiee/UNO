package game.myAssets;

import game.myAssets.cards.ACard;

import java.util.Vector;

public class Player
{
    String name;
    Vector<ACard> hand;

    boolean Frozen;

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
}
