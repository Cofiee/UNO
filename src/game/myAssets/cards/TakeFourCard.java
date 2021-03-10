package game.myAssets.cards;

import game.myAssets.EngineGame;

public class TakeFourCard extends ACard
                            implements ISpecialCard
{
    public TakeFourCard()
    {
        this.color = ACard.Color.BLACK;
    }
    @Override
    public void action(EngineGame engineGame){}
}
