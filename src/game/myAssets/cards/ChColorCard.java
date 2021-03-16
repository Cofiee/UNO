package game.myAssets.cards;

import game.myAssets.EngineGame;

public class ChColorCard extends ACard
                            implements ISpecialCard
{
    public ChColorCard()
    {
        this.color = Color.BLACK;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        engineGame.getControllerGame().chColorAlert();
        engineGame.setTopColor(Color.BLUE);
    }
}
