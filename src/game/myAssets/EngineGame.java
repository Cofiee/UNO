package game.myAssets;

import game.ControllerGame;
import game.myAssets.cards.*;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGame
{
    private ControllerGame controllerGame;
    Player[] players;
    enum Direction
    {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    Direction direction;
    int iLastPlayer;
    int iActualPlayer;
    Stack<ACard> table;
    Vector<ACard> deck;
    RegularCard.Color topColor;
    int numberOfTakenCards;

    public EngineGame(ControllerGame controller)
    {
        this.controllerGame = controller;
    }
    public Player[] getPlayers()
    {
        return players;
    }
    public int getIActualPlayer()
    {
        return iActualPlayer;
    }
    public Player ActualPlayer()
    {
        return players[iActualPlayer];
    }
    public Direction getDirection()
    {
        return direction;
    }
    public RegularCard.Color getTopColor()
    {
        return topColor;
    }
    public void setTopColor(RegularCard.Color color){this.topColor = color;}
    public ControllerGame getControllerGame(){return controllerGame;}

    public void  prepareGame()
    {
        table.clear();
        prepareDeck();
        Collections.shuffle(deck);
        for(Player player : players)
        {
            player.getHand().clear();
            for(int i = 0; i < 7; ++i)
                player.getHand().add(deck.remove(0));
        }
        table.add(deck.remove(0));
    }
    public void prepareDeck()
    {
        deck.clear();
        for(ACard.Color color : ACard.Color.values())
        {
            if(color == ACard.Color.BLACK) break;
            for (int i = 0; i < 10; ++i)
            {
                deck.add(new RegularCard(i, color));
            }
            deck.add(new StopCard(color));
            deck.add(new StopCard(color));
            deck.add(new SwitchCard(color));
            deck.add(new SwitchCard(color));
            deck.add(new TakeTwoCard(color));
            deck.add(new TakeTwoCard(color));
        }
        for(int i = 0; i < 4; ++i)
        {
            deck.add(new ChColorCard());
            deck.add(new TakeFourCard());
        }
    }
    public Player nextPLayer()
    {
        switch (direction)
        {
            case CLOCKWISE:
                if(iActualPlayer == iLastPlayer) return players[0];
                else return players[iActualPlayer + 1];
            case COUNTERCLOCKWISE:
                if(iActualPlayer == 0) return players[iLastPlayer];
                else return players[iActualPlayer - 1];
        }
        return null;
    }
    public void clearTable()
    {
        ACard topCard = table.pop();
        deck.addAll(table);
        Collections.shuffle(deck);
        table.clear();
        table.push(topCard);
    }
    public void beginTurn()
    {
        if(ActualPlayer().isFrozen())
        {
            ActualPlayer().unfreeze();
            //endTurn();
        }
        if(numberOfTakenCards > 0)
        {
            takeCards();
            //endTurn();
        }
    }
    public void switchDirection()
    {
        if(direction == Direction.CLOCKWISE)
            direction = Direction.COUNTERCLOCKWISE;
        else
            direction = Direction.CLOCKWISE;
    }
    public void cardOnTable(ACard card)
    {
        if(card instanceof ISpecialCard)
        {
            ((ISpecialCard) card).action(this);
        }
    }
    public void takeOne()
    {
        ACard firstCard;
        if(deck.isEmpty())
            clearTable();
        firstCard = deck.remove(0);
        if(topColor == firstCard.getColor())
        {
            ///THROW ON TABLE
        }
        else
        {
            ActualPlayer().getHand().add(firstCard);
        }
    }
    public void takeCards()
    {
        ACard firstCard;
        if(deck.isEmpty())
            clearTable();
        firstCard = deck.remove(0);
        if(firstCard instanceof TakeTwoCard)
        {
            ///KARTA NA STOL
            return;
        }
        else
        {
            --numberOfTakenCards;
            ActualPlayer().getHand().add(firstCard);
            while(numberOfTakenCards > 0)
            {
                --numberOfTakenCards;
                if(deck.isEmpty())
                    clearTable();
                ActualPlayer().getHand().add(deck.remove(0));
            }
        }
    }
    public void changeColor(ACard.Color color)
    {
        this.topColor = color;
    }
    public void endTurn()
    {
        if(direction == Direction.CLOCKWISE)
        {
            if(iActualPlayer == iLastPlayer)
                iActualPlayer = 0;
            else
                iActualPlayer++;
        }
        else
        {
            if(iActualPlayer == 0)
                iActualPlayer = iLastPlayer;
            else
                iActualPlayer--;
        }
    }
    public void endGame(){}
}
