package game.myAssets.cards;

import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGameSpV2;

public class StopCard extends ACard
                        implements ISpecialCard
{
    public StopCard(Color color)
    {
        if(color == ACard.Color.BLACK)
        {
            //Wyjatek
        }
        this.color = color;
        this.points = 20;
    }

    @Override
    public void action(EngineGameSpV2 engineGame){ engineGame.getNextPLayer().freeze(); }

    @Override
    public void action(MyTreeNodeV2 node){}
}
