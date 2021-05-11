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
     * @param myHand
     */
    MyTreeNodeV2(GameStateV2 state, Vector<ACard> myHand)
    {
        this.state = state;
        this.
    }

    MyTreeNodeV2(MyTreeNodeV2 parentNode)
    {
        this.visitCount = parentNode.visitCount;
        this.winCount = parentNode.winCount;
        this.parent = parentNode;
        this.state = parent.state.deepClone();
        for (Vector<ACard> hand: parent.playersHands)
        {
            this.playersHands.add((Vector<ACard>)hand.clone());
        }
    }

    /**
     * Metoda generuje losowy stan gdy na podstawie posiadanych informacji
     *
     */
    public void determine()
    {
        Collections.shuffle(state.cardSet);
        int j = 1;
        for (int size: state.playersHandsSizes)
        {
            playersHands.add(new Vector<>());
            for (int i = 0; i < size; ++i)
            {
                ACard card = state.cardSet.remove(0);
                playersHands.get(j).add(card);
            }
        }
    }

    /**
     * Listuje mozliwe do wykonania ruchy w danej turze
     * @return
     */
    public ArrayList<ACard> getPossibleMoves()
    {
        Vector<ACard> actualPlayerHand = playersHands.get(state.actualPlayerIndex);
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

    /**
     * Tworzy dzieci z mozliwych ruchow zwroconych przez metode getPossibleMoves()
     * Gdy nie jest w stanie wykonac ruchu to tworzy dzicko w ktorym dobiera jedna karte
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
                if(childNode.playersHands.get(state.actualPlayerIndex).size() == 0)
                    childNode.isTerminal = true;
                childNode.state.nextPlayer();
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
        this.playersHands.get(state.actualPlayerIndex).remove(card);
        this.state.playersHandsSizes[state.actualPlayerIndex] = this.playersHands.get(state.actualPlayerIndex).size();
        this.state.table.add(card);
        if(card instanceof TakeTwoCard);

    }

    /**
     * Metoda odpowiada za symulacje dobrania jednej karty w przypadku braku mozliwosci znalezienia odpowiedniego wyboru
     * Ustawia flage failedCard
     * */
    private void drawOne()
    {
        if(state.cardSet.size() == 0) return;
        //state.failedCard = state.table.peek();
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
        playersHands.get(state.actualPlayerIndex).add(takenCard);
    }
}
