package game.myAssets.cards;

import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGameSpV2;

public class ChColorCard extends ACard
                            implements ISpecialCard
{
    public ChColorCard()
    {
        this.color = Color.BLACK;
        this.points = 50;
    }

    @Override
    public void action(EngineGameSpV2 engineGame)
    {
        ACard.Color color = engineGame.chColorAction();
        this.color = color == Color.BLACK? Color.RED: color;
    }

    @Override
    public void action(MyTreeNodeV2 node)
    {
        ACard.Color bestColor = node.getActualPlayerBestColor();
        node.setTopColor(bestColor);
    }
}
