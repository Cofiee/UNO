package game.myAssets.cards;

public abstract class ACard
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
    public Color getColor()
    {
        return color;
    }
}
