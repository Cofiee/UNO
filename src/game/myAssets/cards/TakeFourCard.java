package game.myAssets.cards;

import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGameSpV2;
import game.myAssets.GameStateV2;

public class TakeFourCard extends ACard
                            implements ISpecialCard
{
    public TakeFourCard()
    {
        this.color = ACard.Color.BLACK;
        this.points = 50;
    }

    @Override
    public void action(EngineGameSpV2 engineGame)
    {
        ACard.Color color = engineGame.chColorAction();
        this.color = color == Color.BLACK? Color.RED: color;
        GameStateV2 stateV2 = engineGame.getState();
        stateV2.setNumberOfTakenCards(stateV2.numberOfTakenCards + 4);
    }

    @Override
    public void action(MyTreeNodeV2 node)
    {
        ACard.Color bestColor = node.getActualPlayerBestColor();
        node.setTopColor(bestColor);
        GameStateV2 state = node.getState();
        state.numberOfTakenCards += 4;
    }
}
