package game.myAssets;

public class Card
{
    public enum Color
    {
        RED,
        GREEN,
        BLUE,
        YELLOW,
        BLACK
    }
    Color color;
    protected int id;
    protected int digit;

    protected Card(Card.Color color)
    {
        this.digit = -1;
        this.color = color;
    }

    public Card(int digit, Card.Color color)
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
        this.color = color;
    }

    public int getDigit()
    {
        return digit;
    }
    public Color getColor()
    {
        return color;
    }
}
