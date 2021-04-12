package game.myAssets.cards;

import game.myAssets.EngineGame;

public class TakeFourCard extends ACard
                            implements ISpecialCard
{
    public TakeFourCard()
    {
        this.color = ACard.Color.BLACK;
        this.points = 50;
    }
    @Override
    public void action(EngineGame engineGame)
    {
        this.color = engineGame.getControllerGame().chColorAlert();
        int number = engineGame.getNumberOfTakenCards();
        engineGame.setNumberOfTakenCards(number + 4);
    }
}
