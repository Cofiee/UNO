package game.myAssets.AI;

import game.myAssets.GameState;
import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class MyTreeNode
{
    public int visitCount = 0;
    public int winCount = 0;
    private MyTreeNode parent = null;
    private boolean isMaxPlayer;
    private GameState state;
    private Vector<ACard> myHand;
    private Vector<ACard> deterministicOpponentHand = new Vector<>();
    Vector<MyTreeNode> children = new Vector<>();

    public MyTreeNode(GameState state, boolean isMaxPlayer, Vector<ACard> myHand)
    {
        this.state = state;
        this.isMaxPlayer = isMaxPlayer;
        this.myHand = myHand;
    }

    public MyTreeNode(ACard topCard, boolean isMaxPlayer, MyTreeNode parent)
    {
        state = new GameState(parent.state);
        state.table.add(topCard);
        this.parent = parent;
        this.isMaxPlayer = isMaxPlayer;
        this.myHand = (Vector<ACard>)parent.getMyHand().clone();
        this.deterministicOpponentHand = (Vector<ACard>) parent.deterministicOpponentHand.clone();
        if(!this.isMaxPlayer)
            myHand.remove(topCard);
        else
            deterministicOpponentHand.remove(topCard);
    }

    public GameState getState()
    {
        return state;
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
        return state.table.peek();
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
        return deterministicOpponentHand.size();
    }

    public Vector<ACard> getDeck()
    {
        return state.deck;
    }

    public ArrayList<ACard> getPossibleMoves()
    {
        Vector<ACard> actualPlayerHand;
        if(this.isMaxPlayer)
            actualPlayerHand = myHand;
        else
            actualPlayerHand = deterministicOpponentHand;
        ArrayList<ACard> matchingCards = new ArrayList<ACard>();
        if(state.table.peek() instanceof ISpecialCard)
        {
            for (ACard card: actualPlayerHand)
            {
                if(card.getColor() == state.table.peek().getColor() || card.getColor() == ACard.Color.BLACK)
                    matchingCards.add(card);
            }
        }
        else if(state.table.peek() instanceof RegularCard)
        {
            for (ACard card: actualPlayerHand)
            {
                if(card instanceof RegularCard &&
                        (((RegularCard) card).getDigit() == ((RegularCard)state.table.peek()).getDigit() ||
                                card.getColor() == state.table.peek().getColor() ||
                                card.getColor() == ACard.Color.BLACK))
                {
                    matchingCards.add(card);
                }
            }
        }
        return matchingCards;
    }

    public Vector<MyTreeNode> createChildren()
    {
        children.clear();
        ArrayList<ACard> possibleMoves = getPossibleMoves();
        if(possibleMoves.size() > 0)
        {
            for (ACard possibleMove:
                    possibleMoves)
            {
                this.children.add(new MyTreeNode(possibleMove, !this.isMaxPlayer, this));
            }
        }
        else
        {
            MyTreeNode child = new MyTreeNode(state.table.peek(), !this.isMaxPlayer, this);
            child.drawOne();
            this.children.add(child);
        }
        return this.children;
    }

    /**
    * Metoda odpowiada za symulacje dobrania jednej karty w przypadku braku mozliwosci znalezienia odpowiedniego wyboru
    * */
    private void drawOne()
    {
        state.failedCard = state.table.peek();
        ACard takenCard = state.cardSet.remove(0);
        if(takenCard instanceof ISpecialCard)
        {
            if(takenCard.getColor() == ACard.Color.BLACK)
            {
                state.table.add(takenCard);
                return;
            }
            else if(takenCard.getClass() == state.table.peek().getClass())
            {
                state.table.add(takenCard);
                return;
            }
            else if(takenCard.getColor() == state.table.peek().getColor())
            {
                state.table.add(takenCard);
                return;
            }
        }
        else
        {
            if (takenCard.getClass() == state.table.peek().getClass())
            {
                if (takenCard.getColor() == state.table.peek().getColor()
                        || ((RegularCard) takenCard).getDigit() == ((RegularCard) state.table.peek()).getDigit())
                {
                    state.table.add(takenCard);
                    return;
                }
            }else if(takenCard.getColor() == state.table.peek().getColor())
            {
                state.table.add(takenCard);
                return;
            }
        }
        if(this.isMaxPlayer)
            deterministicOpponentHand.add(takenCard);
        else
            myHand.add(takenCard);
    }

    public void determine()
    {
        Collections.shuffle(state.cardSet);
        for(int i = 0; i < state.playerHandSize; ++i)
        {
            ACard card = state.cardSet.remove(0);
            if(state.failedCard != null)
            while (card.getColor() == state.failedCard.getColor() ||
                    ((RegularCard)card).getDigit() == ((RegularCard)state.failedCard).getDigit())
            {
                state.cardSet.add(card);
                card = state.cardSet.remove(0);
            }
            deterministicOpponentHand.add(card);
        }
    }
}
