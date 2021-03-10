package game.myAssets.cards;

import game.myAssets.EngineGame;

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
    }

    @Override
    public void action(EngineGame engineGame)
    {

    }
}
