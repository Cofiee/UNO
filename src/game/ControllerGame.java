package game;

import game.myAssets.Card;
import game.myAssets.Player;

import java.util.Stack;
import java.util.Vector;

public class ControllerGame
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

    public ControllerGame(){}
    public void prepareDeck(){}
    public void clearTable(){}
    public int nextPLayer(int cycle)
    {
        if(direction == Direction.CLOCKWISE && cycle == 3) return 0;
        if(direction == Direction.COUNTERCLOCKWISE && cycle == 0) return 3;
        if (direction == Direction.CLOCKWISE) return cycle + 1;
        else return cycle - 1;
    }
    public void endTurn() {}

    public class Actions
    {
        public void putCardOnTable(){}
        public void takeOne(){}
        public void takeTwo(){}
        public void takeFour(){}
        public void stop(){}
        public void changeColour(){}
    }
}
