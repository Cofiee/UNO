package game.myAssets.cards;

import game.myAssets.EngineGame;

public class ChColorCard extends ACard
                            implements ISpecialCard
{
    public ChColorCard()
    {
        this.color = Color.BLACK;
        this.points = 50;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        this.color = engineGame.chColorAction();
    }
}
