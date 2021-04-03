package game.myAssets;

import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;
import javafx.util.Pair;

import java.util.Stack;
import java.util.Vector;

public class AIPlayer extends Player
{
    final int ALL_CARDS_IN_GAME = 108;
    final int REGULAR_CARDS_IN_GAME = 76;
    final int TAKE_TWO_CARDS_IN_GAME = 8;
    final int STOP_CARDS_IN_GAME = 8;
    final int SWITCH_CARDS_IN_GAME = 8;
    final int TAKE_FOUR_CARDS_IN_GAME = 4;
    final int CHANGE_COLOR_CARDS_IN_GAME = 4;

    int myActionCards = 0;
    int myRegularCards = 0;

    int myRedCards = 0;
    int myBlueCards = 0;
    int myGreenCards = 0;
    int myYellowCards = 0;
    int myBlackCards = 0;

    int playedRedCards = 0;
    int playedBlueCards = 0;
    int PlayedGreenCards = 0;
    int playedYellowCards = 0;
    int playedBlackCards = 0;

    int TakeTwoCardUsed = 0;
    int TakeFourCardUsed = 0;

    Vector<ACard>matchingCards = new Vector<ACard>();

    Stack<ACard> table;

    public AIPlayer(Stack<ACard> table)
    {
        this.table = table;
    }

    public void scanHand()
    {
        for (ACard card:
             super.hand)
        {
            switch (card.getColor())
            {
                case BLACK:
                    myBlackCards++;
                    break;
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
            }
            if(card instanceof ISpecialCard)
                myActionCards++;
            else
                myRegularCards++;
        }
    }

    private Pair<ACard.Color, Integer>[] pack()
    {
        return new Pair[]{
                new Pair<>(ACard.Color.RED, myRedCards),
                new Pair<>(ACard.Color.BLUE, myBlueCards),
                new Pair<>(ACard.Color.GREEN, myGreenCards),
                new Pair<>(ACard.Color.YELLOW, myYellowCards),
                new Pair<>(ACard.Color.BLACK, myBlackCards)
        };
    }

    public ACard.Color maxColorQuantity()
    {
        Pair<ACard.Color, Integer>[] pairs = pack();
        Pair<ACard.Color, Integer> maxPair = pairs[0];
        for (Pair<ACard.Color, Integer> pair: pairs)
        {
            if(maxPair.getValue() < pair.getValue())
                maxPair = pair;
        }
        return maxPair.getKey();
    }

    public void matchMyCards()
    {
        matchingCards.clear();
        ACard topCard = table.peek();
        if(topCard instanceof ISpecialCard)
        {
            for (ACard card: super.hand)
            {
                if(card.getColor() == topCard.getColor() || card.getColor() == ACard.Color.BLACK)
                    matchingCards.add(card);
            }
        }
        else if(topCard instanceof RegularCard)
        {
            for (ACard card: super.hand)
            {
                if(card instanceof RegularCard &&
                        (((RegularCard) card).getDigit() == ((RegularCard) topCard).getDigit() ||
                                card.getColor() == topCard.getColor() ||
                                card.getColor() == ACard.Color.BLACK))
                {
                    matchingCards.add(card);
                }
            }

        }
        return;
    }

}
