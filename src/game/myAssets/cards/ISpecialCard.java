package game.myAssets.cards;

import game.myAssets.AI.MyTreeNodeV2;
import game.myAssets.EngineGameSpV2;

public interface ISpecialCard
{
    void action(MyTreeNodeV2 node);

    void action(EngineGameSpV2 engineGameSpV2);
}
