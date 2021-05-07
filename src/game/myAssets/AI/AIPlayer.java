package game.myAssets.AI;

import game.myAssets.GameState;
import game.myAssets.Player;
import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;
import javafx.util.Pair;

import java.util.Stack;
import java.util.Vector;

public class AIPlayer extends Player
{
    int myActionCards = 0;
    int myRegularCards = 0;

    int myRedCards = 0;
    int myBlueCards = 0;
    int myGreenCards = 0;
    int myYellowCards = 0;
    int myBlackCards = 0;

    int nextPlayerHandSize = 0;
    ACard opponentFailedCard = null;

    Vector<ACard>matchingCards = new Vector<>();
    GameState state;

    public MyTreeMonteCarlo myTreeMonteCarlo;

    public AIPlayer(GameState state)
    {
        this.state = state;
    }

    public void scanHand()
    {
        myRedCards = 0;
        myBlueCards = 0;
        myGreenCards = 0;
        myYellowCards = 0;
        myBlackCards = 0;
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
                new Pair<>(ACard.Color.YELLOW, myYellowCards)//,
                //new Pair<>(ACard.Color.BLACK, myBlackCards)
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

    public boolean matchMyCards()
    {
        matchingCards.clear();
        ACard topCard = state.table.peek();
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
        return !matchingCards.isEmpty();
    }

    public ACard playCard()
    {
        if(matchingCards.isEmpty())
            return null;
        scanHand();
        int bestIndex = 0;
        double lastOptimal = 0;
        int i = 0;
        for(ACard card : matchingCards)
        {
            double aggro = 0.0;
            if(card instanceof ISpecialCard)
            {
                aggro = nextPlayerHandSize < 10 ? 10.0 - nextPlayerHandSize / 10.0 : 0;
            }
            else if((opponentFailedCard != null) &&
                    card.getColor() == opponentFailedCard.getColor())
            {
                aggro = 1.0;
            }
            int colorQuantity = 0;
            switch (card.getColor())
            {
                case RED:
                    colorQuantity = myRedCards;
                    break;
                case BLUE:
                    colorQuantity = myBlueCards;
                    break;
                case GREEN:
                    colorQuantity = myGreenCards;
                    break;
                case YELLOW:
                    colorQuantity = myYellowCards;
                    break;
            }
            double activation = card.getPoints() + colorQuantity + aggro;
            if(activation > lastOptimal)
            {
                lastOptimal = activation;
                bestIndex = i;
            }
            ++i;
        }
        ACard pickedCard = matchingCards.remove(bestIndex);
        hand.remove(pickedCard);
        return pickedCard;
    }

    public void createMCTS()
    {
        myTreeMonteCarlo = new MyTreeMonteCarlo(state, true, hand);
    }
}
