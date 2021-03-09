package game.myAssets;

import java.util.Vector;

public class Player
{
    String name;
    Vector<Card> hand;

    boolean Frozen;

    boolean isAI;
    public String getName()
    {
        return name;
    }
    public Vector<Card> getHand()
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
