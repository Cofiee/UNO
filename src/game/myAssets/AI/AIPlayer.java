package game.myAssets.AI;

import game.myAssets.GameStateV2;
import game.myAssets.Player;
import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;
import game.myAssets.cards.RegularCard;
import javafx.util.Pair;

import java.util.Vector;

public class AIPlayer extends Player
{
    int myRedCards = 0;
    int myBlueCards = 0;
    int myGreenCards = 0;
    int myYellowCards = 0;

    Vector<ACard> matchingCards = new Vector<>();
    GameStateV2 state;

    public MyTreeMonteCarlo myTreeMonteCarlo;

    public AIPlayer(GameStateV2 state)
    {
        this.state = state;
    }

    private Pair<ACard.Color, Integer>[] pack()
    {
        return new Pair[]{
                new Pair<>(ACard.Color.RED, myRedCards),
                new Pair<>(ACard.Color.BLUE, myBlueCards),
                new Pair<>(ACard.Color.GREEN, myGreenCards),
                new Pair<>(ACard.Color.YELLOW, myYellowCards)
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

    public ACard MCTS()
    {
        GameStateV2 aiPerspectiveState = this.state.deepClone();
        myTreeMonteCarlo = new MyTreeMonteCarlo(state, hand);
        return myTreeMonteCarlo.search();
    }
}
