package game.myAssets.cards;


import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGame;
import game.myAssets.EngineGameSpV2;

public class SwitchCard extends ACard
                        implements ISpecialCard
{
    public SwitchCard(ACard.Color color)
    {
        if(color == ACard.Color.BLACK)
        {
            //WYJATEK
        }
        this.color = color;
        this.points = 20;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        engineGame.switchDirection();
    }

    @Override
    public void action(EngineGameSpV2 engineGame)
    {
        engineGame.getState().switchDirection();
    }

    @Override
    public void action(MyTreeNodeV2 node)
    {
        node.getState().switchDirection();
    }
}
