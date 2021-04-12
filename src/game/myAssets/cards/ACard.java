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
    int points;
    public Color getColor()
    {
        return color;
    }
    public void setColor(ACard.Color color)
    {
        this.color = color;
    }
    public int getPoints()
    {
        return points;
    }
    public void setPoints(int points)
    {
        this.points = points;
    }
}
