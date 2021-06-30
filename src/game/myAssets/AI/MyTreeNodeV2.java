package game.myAssets.AI;

import game.myAssets.GameStateV2;
import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;
import game.myAssets.cards.TakeTwoCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class MyTreeNodeV2
{
    //Czy wezel jest konczacy
    public boolean isTerminal = false;
    //Liczba odwiedzin
    public int visitCount = 0;
    //Liczba zwyciestw
    public int winCount = 0;
    //Wezel rodzic
    MyTreeNodeV2 parent = null;
    //Stan gry
    GameStateV2 state;
    //Tablica przechowuje karty tych graczy  którzy dobrali kartę z deku.
    ACard[] failedCard;
    //Kolor karty na szczycie stosu kart
    ACard.Color topColor;
    //Wektor ręki kart gracza symulującego
    Vector<ACard> myHand;
    //Lista rąk kart wszystkich graczy w grze.
    ArrayList<Vector<ACard>> playersHands = new ArrayList<>();
    //Wektor dzieci wezla
    Vector<MyTreeNodeV2> children = new Vector<>();
    //Liczba poszczegolnych kart danego koloru w rece gracza
    int myRedCards = 0;
    int myBlueCards = 0;
    int myGreenCards = 0;
    int myYellowCards = 0;
    int myBlackCards = 0;
    //Pozostale karty dobierz dwie karty
    int remainingTakeTwoCards = 8;

    /**
     * Konstruktor podstawowy tworzy wezel aktualnego stanu gry
     * @param state
     */
    MyTreeNodeV2(GameStateV2 state, Vector<ACard> myHand)
    {
        this.state = state;
        this.myHand = myHand;
        this.topColor = state.table.peek().getColor();
        this.failedCard = new ACard[4];
        for (ACard card: this.myHand)
        {
            myColorCardIncrement(card);
        }
    }

    /**
     * Konstruktor kopiujacy
     * @param parentNode
     */
    MyTreeNodeV2(MyTreeNodeV2 parentNode)
    {
        this.isTerminal = parentNode.isTerminal;
        this.visitCount = parentNode.visitCount;
        this.winCount = parentNode.winCount;
        this.parent = parentNode;
        this.state = parent.state.deepClone();
        this.failedCard = new ACard[4];
        for (int i = 0; i <= state.lastPlayerIndex; ++i)
        {
            failedCard[i] = parent.failedCard[i];
        }
        this.topColor = parent.topColor;
        this.myHand = (Vector<ACard>) parent.myHand.clone();
        for (Vector<ACard> hand: parent.playersHands)
        {
            this.playersHands.add((Vector<ACard>)hand.clone());
        }
        for (ACard card: this.myHand)
        {
            myColorCardIncrement(card);
        }
        this.remainingTakeTwoCards = parentNode.remainingTakeTwoCards;
        if(state.table.peek() instanceof TakeTwoCard)
            this.remainingTakeTwoCards--;
    }

    /**
     * Pobierz stan gry
     * @return
     */
    public GameStateV2 getState()
    {
        return state;
    }

    /**
     * Pobierz kolor gornej karty
     * @return
     */
    public ACard.Color getTopColor()
    {
        return topColor;
    }

    /**
     * Ustaw kolor gornej karty
     * @param topColor
     */
    public void setTopColor(ACard.Color topColor)
    {
        this.topColor = topColor;
    }

    /**
     * Metoda generuje losowy stan gdy na podstawie posiadanych informacji
     */
    public void determine()
    {
        Collections.shuffle(state.cardSet);
        for (int i = 0; i < state.playersHandsSizes.length; ++i)
        {
            if(i == state.actualPlayerIndex)
                playersHands.add(myHand);
            else
            {
                Vector<ACard> deterministicOpponentHand = new Vector<>();
                while (deterministicOpponentHand.size() < state.playersHandsSizes[i])
                {
                    deterministicOpponentHand.add(state.cardSet.remove(0));
                }
                playersHands.add(deterministicOpponentHand);
            }
        }
    }

    /**
     * Listuje mozliwe do wykonania ruchy w danej turze
     * @return
     */
    public ArrayList<ACard> getPossibleMoves()
    {
        Vector<ACard> actualPlayerHand = getActualPlayerHand();
        ArrayList<ACard> matchingCards = new ArrayList<ACard>();
        if(state.table.peek() instanceof ISpecialCard)
        {
            for (ACard card: actualPlayerHand)
            {
                if(card.getColor() == topColor || card.getColor() == ACard.Color.BLACK)
                    matchingCards.add(card);
            }
        }
        else if(state.table.peek() instanceof RegularCard)
        {
            for (ACard card: actualPlayerHand)
            {
                if(card instanceof RegularCard &&
                        (((RegularCard) card).getDigit() == ((RegularCard)state.table.peek()).getDigit() ||
                                card.getColor() == topColor ||
                                card.getColor() == ACard.Color.BLACK))
                {
                    matchingCards.add(card);
                }
            }
        }
        return matchingCards;
    }

    /**
     * Tworzy dzieci z mozliwych ruchow zwroconych przez metode getPossibleMoves()
     * Gdy nie jest w stanie wykonac ruchu to tworzy dzicko w ktorym dobiera jedna karte
     * Klonuje swoj stan natepnie na stworzonym klonie dokonuje zmian stanu
     * @return wektor stworzonych dzieci
     */
    public Vector<MyTreeNodeV2> createChildren()
    {
        children.clear();
        ArrayList<ACard> possibleMoves = getPossibleMoves();
        if(possibleMoves.size() > 0)
        {
            for (ACard possibleMove: possibleMoves)
            {
                MyTreeNodeV2 childNode = new MyTreeNodeV2(this);
                childNode.cardOnTable(possibleMove);
                childNode.state.nextPlayer();
                if(childNode.state.numberOfTakenCards > 0)
                {
                    childNode.drawMany();
                    childNode.state.nextPlayer();
                }
                children.add(childNode);
            }
        }
        else
        {
            MyTreeNodeV2 childNode = new MyTreeNodeV2(this);
            childNode.drawOne();
            childNode.state.nextPlayer();
            children.add(childNode);
        }
        return this.children;
    }

    /**
     * Usuwa karte z reki odpowiedniego gracza bazujac na isMaxPlayer
     * Nastepnie przeklada wybrana karte na stol
    * */
    private void cardOnTable(ACard card)
    {
        if (card == null) throw new AssertionError();
        Vector<ACard> actualPlayerHand = getActualPlayerHand();
        actualPlayerHand.remove(card);
        state.table.add(card);
        if(card instanceof ISpecialCard)
            ((ISpecialCard)card).action(this);
        state.playersHandsSizes[state.actualPlayerIndex] -= 1;
        if(state.playersHandsSizes[state.actualPlayerIndex] <= 0)
            this.isTerminal = true;
        if(card.getColor() != ACard.Color.BLACK)
            topColor = card.getColor();
        failedCard[state.actualPlayerIndex] = null;
        myColorCardDecrement(card);
    }

    /**
     * Metoda odpowiada za symulacje dobrania jednej karty w przypadku braku mozliwosci znalezienia odpowiedniego wyboru
     * Ustawia flage failedCard
     * */
    private void drawOne()
    {
        if(state.cardSet.size() == 0) return;
        failedCard[state.actualPlayerIndex] = state.table.peek();
        ACard takenCard = state.cardSet.remove(0);
        if(takenCard instanceof ISpecialCard)
        {
            if(takenCard.getColor() == ACard.Color.BLACK)
            {
                cardOnTable(takenCard);
                return;
            }
            else if(takenCard.getClass() == state.table.peek().getClass())
            {
                cardOnTable(takenCard);
                return;
            }
            else if(takenCard.getColor() == state.table.peek().getColor())
            {
                cardOnTable(takenCard);
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
                    cardOnTable(takenCard);
                    return;
                }
            }else if(takenCard.getColor() == state.table.peek().getColor())
            {
                state.table.add(takenCard);
                if(takenCard instanceof ISpecialCard)
                    ((ISpecialCard)takenCard).action(this);
                return;
            }
        }
        getActualPlayerHand().add(takenCard);
        failedCard[state.actualPlayerIndex] = state.table.peek();
        myColorCardIncrement(takenCard);
    }
    /**
     * Metoda odpowiada za symulacje dobrania wielu kart w przypadku braku mozliwosci znalezienia odpowiedniego wyboru
     * Ustawia flage failedCard
     * */
    private void drawMany()
    {
        if(state.numberOfTakenCards <= 0) return;
        if(this.playersHands.isEmpty()) return; // wyjatek
        ACard firstCard = state.cardSet.remove(0);
        //Jesli jest posiadana

        if(state.table.peek() instanceof TakeTwoCard &&
            firstCard instanceof TakeTwoCard)
        {
            state.table.add(firstCard);
            ((ISpecialCard)firstCard).action(this);
            return;
        }
        Vector<ACard> hand = getActualPlayerHand();
        hand.add(firstCard);
        myColorCardIncrement(firstCard);
        state.numberOfTakenCards--;
        while (state.numberOfTakenCards > 0)
        {
            ACard card = state.cardSet.remove(0);
            hand.add(card);
            myColorCardIncrement(card);
            state.numberOfTakenCards--;
        }
    }

    /**
     * Metoda zwraca reke aktualnego gracza
     * Przed determinizacja zostaje zwrocona reka gracza
     * @return
     */
    public Vector<ACard> getActualPlayerHand()
    {
        if(playersHands.size() == 0)
            return myHand;
        else
            return playersHands.get(state.actualPlayerIndex);
    }

    /**
     *
     * @return
     */
    public MyTreeNodeV2 clone()
    {
        MyTreeNodeV2 clonedNode = new MyTreeNodeV2(this);
        return clonedNode;
    }

    /**
     * Pobierz najliczniejszy kolor z reki aktualnego gracza
     * @return
     */
    public ACard.Color getActualPlayerBestColor()
    {
        int numberOfRed = 0;
        int numberOfBlue = 0;
        int numberOfGreen = 0;
        int numberOfYellow = 0;
        for (ACard card: getActualPlayerHand())
        {
            switch (card.getColor())
            {
                case RED:
                    numberOfRed++;
                    break;
                case BLUE:
                    numberOfBlue++;
                    break;
                case GREEN:
                    numberOfGreen++;
                    break;
                case YELLOW:
                    numberOfYellow++;
                    break;
            }
        }
        if(numberOfRed >= numberOfBlue && numberOfRed >= numberOfGreen && numberOfRed >= numberOfYellow)
            return ACard.Color.RED;
        else if(numberOfBlue >= numberOfGreen && numberOfBlue >= numberOfYellow)
            return ACard.Color.BLUE;
        else if(numberOfGreen >= numberOfYellow)
            return ACard.Color.GREEN;
        else
            return ACard.Color.YELLOW;
    }

    /**
     * Zwieksza licznik kolorow
     * @param card
     */
    private void myColorCardIncrement(ACard card)
    {
        switch (card.getColor())
        {
            case RED:
                myRedCards++;
                break;
            case BLUE:
                myBlueCards++;
                break;
            case GREEN:
                myGreenCards++;
                break;
            case YELLOW:
                myYellowCards++;
                break;
            case BLACK:
                myBlackCards++;
                break;
        }
    }

    /**
     * Zmniejsza licznik kolorow
     * @param card
     */
    private void myColorCardDecrement(ACard card)
    {
        switch (card.getColor())
        {
            case RED:
                myRedCards--;
                break;
            case BLUE:
                myBlueCards--;
                break;
            case GREEN:
                myGreenCards--;
                break;
            case YELLOW:
                myYellowCards--;
                break;
            case BLACK:
                myBlackCards--;
                break;
        }
    }
}
