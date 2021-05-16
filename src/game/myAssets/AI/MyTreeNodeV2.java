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
    public boolean isTerminal = false;
    public int visitCount = 0;
    public int winCount = 0;
    MyTreeNodeV2 parent = null;
    GameStateV2 state;
    ACard[] failedCard;
    ACard.Color topColor;
    Vector<ACard> myHand;
    ArrayList<Vector<ACard>> playersHands = new ArrayList<>();
    Vector<MyTreeNodeV2> children = new Vector<>();

    int myRedCards = 0;
    int myBlueCards = 0;
    int myGreenCards = 0;
    int myYellowCards = 0;
    int myBlackCards = 0;

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
    }

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
    }

    public GameStateV2 getState()
    {
        return state;
    }

    public ACard.Color getTopColor()
    {
        return topColor;
    }

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
    }

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
        state.numberOfTakenCards--;
        while (state.numberOfTakenCards > 0)
        {
            ACard card = state.cardSet.remove(0);
            hand.add(card);
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
}
