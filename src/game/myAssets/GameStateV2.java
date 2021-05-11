package game.myAssets;

import game.myAssets.cards.ACard;

import java.util.Stack;
import java.util.Vector;

public class GameStateV2
{
    Vector<Player> players;
    public int lastPlayerIndex;

    public Stack<ACard> table;

    public Vector<ACard> cardSet;
    //public Vector<ACard> redCards;
    //public Vector<ACard> blueCards;
    //public Vector<ACard> greenCards;
    //public Vector<ACard> yellowCards;
    public Vector<ACard> deck;

    //public Vector<ACard> failedCard;
    public int[] playersHandsSizes;

    public enum Direction
    {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    public Direction direction = Direction.CLOCKWISE;
    public int actualPlayerIndex = 0;
    public int numberOfTakenCards = 0;

    public GameStateV2()
    {
        this.players = new Vector<>();
        this.table = new Stack<>();
        this.cardSet = new Vector<>();
        this.deck = new Vector<>();

        //failedCard = new Vector<>();
    }

    public GameStateV2 deepClone()
    {
        GameStateV2 tmpState = new GameStateV2();
        tmpState.players = (Vector<Player>) this.players.clone();
        tmpState.lastPlayerIndex = this.lastPlayerIndex;
        tmpState.table = (Stack<ACard>)this.table.clone();
        tmpState.deck = (Vector<ACard>)this.deck.clone();
        tmpState.playersHandsSizes = new int[this.players.size()];
        for(int i = 0; i < this.players.size(); ++i)
        {
            tmpState.playersHandsSizes[i] = this.playersHandsSizes[i];
        }
        tmpState.cardSet = (Vector<ACard>)this.cardSet.clone();
        tmpState.direction = this.direction;
        tmpState.actualPlayerIndex = this.actualPlayerIndex;
        tmpState.numberOfTakenCards = this.numberOfTakenCards;
        return tmpState;
    }

    public void adjustPerspective(int myPlayerIndex)
    {
        actualPlayerIndex = myPlayerIndex;
        int[] tmpArr = new int[lastPlayerIndex + 1];
        for(int i = 0; i <= lastPlayerIndex; ++i)
        {
            tmpArr[i] = playersHandsSizes[actualPlayerIndex];
        }
        playersHandsSizes = tmpArr;
    }
    /**
     * Metoda przesowa index o jeden
     */
    public void nextPlayer()
    {
        if(direction == Direction.CLOCKWISE)
        {
            if(actualPlayerIndex == lastPlayerIndex)
                actualPlayerIndex = 0;
            else
                actualPlayerIndex++;
        }
        else
        {
            if (actualPlayerIndex == 0)
                actualPlayerIndex = lastPlayerIndex;
            else
                actualPlayerIndex--;
        }
    }

    public void switchDirection()
    {
        if(this.direction == GameStateV2.Direction.CLOCKWISE)
            this.direction = GameStateV2.Direction.COUNTERCLOCKWISE;
        else
            this.direction = GameStateV2.Direction.CLOCKWISE;
    }
}
