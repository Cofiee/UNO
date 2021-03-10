package game.myAssets.cards;


import game.myAssets.EngineGame;

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
    }

    @Override
    public void action(EngineGame engineGame)
    {
        engineGame.switchDirection();
    }
}
