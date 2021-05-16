package game.myAssets.cards;

import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGame;
import game.myAssets.EngineGameSpV2;
import game.myAssets.GameStateV2;

public class TakeTwoCard extends ACard
                            implements ISpecialCard
{
    public TakeTwoCard(Color color)
    {
        if(color == Color.BLACK)
        {
            //WYJATEK
        }
        this.color = color;
        this.points = 20;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        int numberOfTakenCards = engineGame.getNumberOfTakenCards();
        engineGame.setNumberOfTakenCards(numberOfTakenCards + 2);
    }

    public void action(EngineGameSpV2 engineGame)
    {
        GameStateV2 state = engineGame.getState();
        state.setNumberOfTakenCards(state.numberOfTakenCards + 2);
    }

    @Override
    public void action(MyTreeNodeV2 node)
    {
        GameStateV2 state = node.getState();
        state.numberOfTakenCards += 2;
    }
}
