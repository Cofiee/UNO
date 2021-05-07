package game.myAssets;

import game.myAssets.cards.ACard;

import java.util.Stack;
import java.util.Vector;

public class GameState
{
    public Stack<ACard> table;
    public Vector<ACard> cardSet;
    public Vector<ACard> redCards;
    public Vector<ACard> blueCards;
    public Vector<ACard> greenCards;
    public Vector<ACard> yellowCards;
    public Vector<ACard> deck;
    public ACard failedCard;
    public int playerHandSize;
    public GameState(){}

    public GameState(GameState orginal)
    {
        table = (Stack<ACard>) orginal.table.clone();
        cardSet = (Vector<ACard>) orginal.cardSet.clone();
        redCards = (Vector<ACard>) orginal.redCards.clone();
        blueCards = (Vector<ACard>) orginal.blueCards.clone();
        greenCards = (Vector<ACard>) orginal.greenCards.clone();
        yellowCards = (Vector<ACard>) orginal.yellowCards.clone();
        deck = (Vector<ACard>) orginal.deck.clone();
        failedCard = orginal.failedCard;
    }
}
