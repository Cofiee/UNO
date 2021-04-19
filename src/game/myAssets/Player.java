package game.myAssets;

import game.myAssets.cards.ACard;
import game.myAssets.cards.TakeFourCard;
import game.myAssets.cards.TakeTwoCard;
import javafx.scene.Parent;

import java.util.Vector;

public class Player
{
    protected String name;
    protected Vector<ACard> hand;
    protected boolean Frozen;
    /*Karta przy ktorej nie dalo sie rzucic nic na stol*/
    protected ACard failedCard = null;
    protected int score = 0;

    public Player(){this.hand = new Vector<>();}
    public String getName()
    {
        return name;
    }
    public Vector<ACard> getHand()
    {
        return hand;
    }
    public int getScore()
    {
        return score;
    }
    public void setScore(int score)
    {
        this.score = score;
    }
    public void addScore(int score)
    {
        this.score += score;
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
