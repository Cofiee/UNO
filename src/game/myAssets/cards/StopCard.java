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
        this.points = 20;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        engineGame.getNextPLayer().freeze();
    }
}
