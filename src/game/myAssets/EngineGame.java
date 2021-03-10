package game.myAssets;

import game.myAssets.Card;
import game.myAssets.Player;
import game.myAssets.SpecialCard;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGame
{
    int numberOfPlayers;
    Player[] player;
    Stack<Card> table;
    Vector<Card> deck;

    Card.Color topColor;
    int topDigit;

    enum Direction
    {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    Direction direction;
    int cycle;

    public EngineGame(){}
    public void  prepareGame(){}
    public void prepareDeck(){}
    public void clearTable()
    {
        Card topCard = table.pop();
        deck.addAll(table);
        Collections.shuffle(deck);
        table.clear();
        table.push(topCard);
    }
    public int nextPLayer()
    {
        if(direction == Direction.CLOCKWISE && cycle == 3) return 0;
        if(direction == Direction.COUNTERCLOCKWISE && cycle == 0) return 3;
        if (direction == Direction.CLOCKWISE) return cycle + 1;
        else return cycle - 1;
    }
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
        public void reverseDirection()
        {
            if(direction == Direction.CLOCKWISE)
                direction = Direction.COUNTERCLOCKWISE;
            else
                direction = Direction.CLOCKWISE;
            endTurn();
        }
    }
}
