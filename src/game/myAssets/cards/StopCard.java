package game.myAssets.cards;

import game.myAssets.EngineGame;

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
    }

    @Override
    public void action(EngineGame engineGame)
    {
        engineGame.nextPLayer().freeze();
    }
}
