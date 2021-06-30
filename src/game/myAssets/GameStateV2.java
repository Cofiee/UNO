package game.myAssets;

import game.myAssets.cards.ACard;

import java.util.Stack;
import java.util.Vector;

public class GameStateV2
{
    //Wektor graczy
    Vector<Player> players;
    //Index ostatniego gracza
    public int lastPlayerIndex;
    //Czy nastepny gracz jest zamrozony
    public boolean isFrozen;
    //Stos stolu na ktory odkladane sa karty
    public Stack<ACard> table;
    //Zbior kart uzytych w grze
    public Vector<ACard> cardSet;
    //Kupka kart do gry
    public Vector<ACard> deck;
    //Rozmiary rak graczy
    public int[] playersHandsSizes;
    //Kierunki rozgrywki
    public enum Direction
    {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    //Przechowuje aktualny kierunek
    public Direction direction = Direction.CLOCKWISE;
    //Indeks aktualnego gracza
    public int actualPlayerIndex = 0;
    //Liczba kart do wziecia przez gracza
    public int numberOfTakenCards = 0;

    /**
     * Konstruktor tworzy pusty stan gry
     */
    public GameStateV2()
    {
        this.players = new Vector<>();
        this.table = new Stack<>();
        this.cardSet = new Vector<>();
        this.deck = new Vector<>();
    }

    /**
     * Tworzy obiekt swojego klona
     * @return nowy obiekt GameStateV2
     */
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
        if(isFrozen)
        {
            isFrozen = false;
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
    }

    /**
     * @return zwraca indeks nastepnego gracza
     */
    public int getNextPlayerIndex()
    {
        if(direction == Direction.CLOCKWISE)
        {
            if(actualPlayerIndex == lastPlayerIndex)
                return 0;
            else
                return actualPlayerIndex + 1;
        }
        else
        {
            if (actualPlayerIndex == 0)
                return lastPlayerIndex;
            else
                return actualPlayerIndex - 1;
        }
    }
    /**
     * zmien kierunek
     */
    public void switchDirection()
    {
        if(this.direction == GameStateV2.Direction.CLOCKWISE)
            this.direction = GameStateV2.Direction.COUNTERCLOCKWISE;
        else
            this.direction = GameStateV2.Direction.CLOCKWISE;
    }

    /**
     * ustaw liczbe pobieranych kart
     */
    public void setNumberOfTakenCards(int numberOfTakenCards)
    {
        this.numberOfTakenCards = numberOfTakenCards;
    }
}
