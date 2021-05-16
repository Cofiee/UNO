package game.myAssets.cards;

public class RegularCard extends ACard
{
    int digit;

    public RegularCard(int digit, RegularCard.Color color)
    {
        if(digit < 0 || 9 < digit)
        {
            //WYJATEk
        }
        if(color == Color.BLACK)
        {
            //WYJATEk
        }
        this.digit = digit;
        this.points = digit;
        this.color = color;
    }

    public int getDigit()
    {
        return digit;
    }
}
