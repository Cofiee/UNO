package game.myAssets;

import game.myAssets.cards.*;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGame
{
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

    public EngineGame(){}
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

    public void  prepareGame()
    {
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
    public void switchDirection()
    {
        if(direction == Direction.CLOCKWISE)
            direction = Direction.COUNTERCLOCKWISE;
        else
            direction = Direction.CLOCKWISE;
        //endTurn();
    }
    /*
    public void beginTurn()
    {
        if(player[cycle].isFrozen())
            endTurn();
    }
    public void endTurn()
    {
        if(table.peek().getColor() != Card.Color.BLACK)
            topColor = table.peek().getColor();
        player[cycle].unfreeze();
    }
    public void endGame(){}
    public void showHand(){} //??ZOBACZE JESZCZE JAK WYGLADA SPRAWA Z WIDOKAMI

    public class Actions
    {
        public void putCardOnTable(){} // TO DOPIERO PO USTALNIU JAK BEDA SIE WYSWIETLAC KARTY
        public void takeOne()
        {
            if(deck.isEmpty())
                clearTable();
            player[cycle].getHand().add(deck.remove(0));
            endTurn();
        }
        public void takeTwo()
        {
            Card firstCard;
            if(deck.isEmpty())
                clearTable();
            firstCard = deck.remove(0);
            if (firstCard.getDigit() == -1)
            {
                if (((SpecialCard) firstCard).getType() == SpecialCard.Type.TAKE_TWO)
                {
                    table.add(firstCard);
                    endTurn();
                }
            }
            player[cycle].getHand().add(firstCard);
            if(deck.isEmpty())
                clearTable();
            player[cycle].getHand().add(deck.remove(0));
            endTurn();
        }
        public void takeFour()
        {
            Card firstCard;
            if(deck.isEmpty())
                clearTable();
            firstCard = deck.remove(0);
            if (firstCard.getDigit() == -1)
            {
                if (((SpecialCard) firstCard).getType() == SpecialCard.Type.TAKE_TWO)
                {
                    table.add(firstCard);
                    endTurn();
                }
            }
            player[cycle].getHand().add(firstCard);
            for(int i = 3; i > 0; --i)
            {
                if(deck.isEmpty())
                    clearTable();
                player[cycle].getHand().add(deck.remove(0));
            }
            endTurn();
        }
        public void stop()
        {
            player[nextPLayer()].freeze();
            endTurn();
        }
        public void changeColour()
        {
            //FXOWY WYBOR KOLOROW
        }
    }
    */
}
