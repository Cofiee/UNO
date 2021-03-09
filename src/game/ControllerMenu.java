package game;

import game.myAssets.Card;
import game.myAssets.SpecialCard;

public class ControllerMenu
{
    public void test()
    {
        Card aaaa = new SpecialCard(SpecialCard.Type.CH_COLOR);
        System.out.println(aaaa.getDigit());
        System.out.println(((SpecialCard)aaaa).getType());
    }
}
