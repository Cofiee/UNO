package game.myAssets.AI;

import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;

import java.util.ArrayList;
import java.util.Vector;

public class MyTreeNode
{
    public int visitCount = 0;
    public int winCount = 0;
    private MyTreeNode parent = null;
    private boolean isMaxPlayer;
    private ACard topCard;
    private Vector<ACard> deck;
    private Vector<ACard> myHand;
    int opponentHandSize = 7;
    Vector<MyTreeNode> children = new Vector<>();

    public MyTreeNode(ACard topCard, boolean isMaxPlayer, Vector<ACard> myHand, Vector<ACard> deck)
    {
        this.topCard = topCard;
        this.isMaxPlayer = isMaxPlayer;
        this.myHand = myHand;
        this.deck = deck;
    }

    public MyTreeNode(ACard topCard, MyTreeNode parent)
    {
        this.parent = parent;
        this.isMaxPlayer = parent.isMaxPlayer;
        this.topCard = topCard;
        this.myHand = (Vector<ACard>)parent.getMyHand().clone();
        myHand.remove(topCard);
        this.deck = (Vector<ACard>)parent.deck.clone();
    }

    public int getVisitCount()
    {
        return visitCount;
    }

    public int getWinCount()
    {
        return winCount;
    }

    public ACard getTopCard()
    {
        return topCard;
    }

    public MyTreeNode getParent()
    {
        return parent;
    }

    public boolean isMaxPlayer()
    {
        return isMaxPlayer;
    }

    public Vector<ACard> getMyHand()
    {
        return myHand;
    }

    public Vector<MyTreeNode> getChildren()
    {
        return children;
    }

    public int getOpponentHandSize()
    {
        return opponentHandSize;
    }

    public Vector<ACard> getDeck()
    {
        return deck;
    }

    public ArrayList<ACard> getPossibleMoves()
    {
        ArrayList<ACard> matchingCards = new ArrayList<ACard>();
        if(this.topCard instanceof ISpecialCard)
        {
            for (ACard card: this.myHand)
            {
                if(card.getColor() == this.topCard.getColor() || card.getColor() == ACard.Color.BLACK)
                    matchingCards.add(card);
            }
        }
        else if(this.topCard instanceof RegularCard)
        {
            for (ACard card: this.myHand)
            {
                if(card instanceof RegularCard &&
                        (((RegularCard) card).getDigit() == ((RegularCard)this.topCard).getDigit() ||
                                card.getColor() == this.topCard.getColor() ||
                                card.getColor() == ACard.Color.BLACK))
                {
                    matchingCards.add(card);
                }
            }
        }
        return matchingCards;
    }

    public  Vector<MyTreeNode> createChildren()
    {
        children.clear();
        ArrayList<ACard> possibleMoves = getPossibleMoves();
        if(possibleMoves.size() > 0)
        {
            for (ACard possibleMove:
                    possibleMoves)
            {
                this.children.add(new MyTreeNode(possibleMove, this));
            }
        }
        else
        {
            MyTreeNode child = new MyTreeNode(topCard, this);
            child.drawOne();
            this.children.add(child);
        }
        return this.children;
    }

    private void drawOne()
    {
        ACard takenCard = deck.remove(0);
        if(takenCard instanceof ISpecialCard)
        {
            if(takenCard.getColor() == ACard.Color.BLACK)
            {
                topCard = takenCard;
                return;
            }
            else if(takenCard.getClass() == topCard.getClass())
            {
                topCard = takenCard;
                return;
            }
            else if(takenCard.getColor() == topCard.getColor())
            {
                topCard = takenCard;
                return;
            }
        }
        else
        {
            if (takenCard.getClass() == topCard.getClass())
            {
                if (takenCard.getColor() == topCard.getColor()
                        || ((RegularCard) takenCard).getDigit() == ((RegularCard) topCard).getDigit())
                {
                    topCard = takenCard;
                    return;
                }
            }else if(takenCard.getColor() == topCard.getColor())
            {
                topCard = takenCard;
                return;
            }
        }
        myHand.add(takenCard);
    }
}
