package game.myAssets;

public class SpecialCard extends Card
{
    public enum Type
    {
        STOP,
        CHANGE_DIRECTION,
        TAKE_TWO,
        TAKE_FOUR,
        CH_COLOR
    }
    Type type;
    String description;
    public SpecialCard(SpecialCard.Type type, Card.Color color)
    {
        super(color);
        if(type == Type.CH_COLOR || type == Type.TAKE_FOUR)
        {
            //EXCEPTION
        }
        this.type = type;
    }
    public SpecialCard(SpecialCard.Type type)
    {
        super(Card.Color.BLACK);
        if(type != Type.CH_COLOR || type != Type.TAKE_FOUR)
        {
            //EXCEPTION
        }
        this.type = type;
    }

    public Type getType()
    {
        return type;
    }
}
