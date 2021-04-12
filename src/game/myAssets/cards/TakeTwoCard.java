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
        this.points = 20;
    }

    @Override
    public void action(EngineGame engineGame)
    {
        int number = engineGame.getNumberOfTakenCards();
        engineGame.setNumberOfTakenCards(number + 2);
    }
}
