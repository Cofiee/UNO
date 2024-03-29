package game.myAssets.AI;

import game.myAssets.GameStateV2;
import game.myAssets.cards.ACard;
import game.myAssets.cards.ISpecialCard;

import java.util.Random;
import java.util.Vector;

public class MyTreeMonteCarlo
{
    private final int ITERATIONS = 100000;

    private Random randomGenerator = new Random();
    MyTreeNodeV2 head;

    /**
     * Konstruktor tworzy obiekt obslugujacy wyszukiwanie monte carlo
     * @param state GameState - Stan gry
     * @param myHand Vector<ACard> reka z kartami
     */
    public MyTreeMonteCarlo(GameStateV2 state, Vector<ACard> myHand)
    {
        this.head = new MyTreeNodeV2(state, (Vector<ACard>) myHand.clone());
    }

    /**
     *  Zwraca znaleziona karte do rzucenia
     * @return ACard - znaleziona karta lub
     * @return null - w przypadku braku dopasowania
     */
    public ACard search()
    {
        head.createChildren();
        for(int i = 0; i < ITERATIONS; ++i)
        {
            MyTreeNodeV2 nodeToExploration = selection(head);
            //if(nodeToExploration.isTerminal)
                //return nodeToExploration.state.table.peek();
            nodeToExploration = nodeToExploration.clone();
            nodeToExploration.determine();
            int result = simulation(nodeToExploration);
            backpropagation(nodeToExploration, result);
        }
        System.out.println("Liczbna dzieci: " + head.children.size());
        for (MyTreeNodeV2 child:
                head.children)
        {
            System.out.println("Liczba odwiedzeni: " + child.visitCount + "\tLiczba zwyciestw: " + child.winCount);
        }
        MyTreeNodeV2 bestNode = selection(head);
        return bestNode.state.table.peek();
    }

    /**
     *
     * @param node
     * @return
     */
    public MyTreeNodeV2 selection(MyTreeNodeV2 node)
    {
        Vector<MyTreeNodeV2> children = node.children;
        MyTreeNodeV2 bestMove = children.get(0);
        double bestUTC = Integer.MIN_VALUE;
        if(children.size() > 0)
        {
            for (MyTreeNodeV2 child: children)
            {
                int parentVisitCount = node.visitCount;
                int childVisitCount = child.visitCount;
                int win = child.winCount;
                double calcedUTC = calcUCT(win, parentVisitCount, childVisitCount);
                if(calcedUTC > bestUTC)
                {
                    bestUTC = calcedUTC;
                    bestMove = child;
                }
            }
            return bestMove;
        }
        else
            return null;
    }

    /**
     * winCount is reward value of all nodes beneath this node
     * N is the number of times the parent node has been simulated, and
     * ni is the number of times the child node i has been visited
     * */
    private double calcUCT(double winCount, int bigN, int ni)
    {
        if(ni == 0)
        {
            return Integer.MAX_VALUE;
        }
        double c = Math.sqrt(2.0); //state parameter
        double rootBase = Math.log(bigN) / ni;
        return winCount / ni + c * Math.sqrt(rootBase);
    }

    /**
     *
     * @param node
     * @return
     */
    public int simulation(MyTreeNodeV2 node)
    {
        MyTreeNodeV2 tmpNode = node;
        while(tmpNode.isTerminal == false)
        {
            tmpNode = randomPlay(tmpNode);
        }
        if(tmpNode.playersHands.get(head.state.actualPlayerIndex).size() == 0)
        {
            return 1;
        }else if(tmpNode.state.cardSet.size() == 0)
        {
            return 0;
        }else
        {
            return -1;
        }
    }

    /**
     *
     * @param node
     * @return
     */
    public MyTreeNodeV2 randomPlay(MyTreeNodeV2 node)
    {
        double lowerbound = -0.5;
        node.createChildren();
        int bestIndex = 0;
        double evaluation = Integer.MIN_VALUE;
        Vector<MyTreeNodeV2> children = node.children;
        if(children.size() > 1)
        {
            for (int i = 0; i < children.size(); ++i)
            {
                double result = evaluate(children.get(i));
                result += randomGenerator.nextDouble() + lowerbound;
                if (result > evaluation)
                {
                    evaluation = result;
                    bestIndex = i;
                }
            }
        }
        MyTreeNodeV2 randomChoice = children.get(bestIndex);
        return randomChoice;
    }

    /**
     * @return metoda zwraca wartosc wyboru
     * */
    public double evaluate(MyTreeNodeV2 myTreeNode)
    {
        double cardValue = 0.0;
        GameStateV2 state = myTreeNode.state;
        ACard card = state.table.peek();
        switch (card.getColor())
        {
            case RED:
                cardValue += myTreeNode.myRedCards / 10;
                break;
            case BLUE:
                cardValue += myTreeNode.myBlueCards / 10;
                break;
            case GREEN:
                cardValue += myTreeNode.myGreenCards / 10;
                break;
            case YELLOW:
                cardValue += myTreeNode.myYellowCards / 10;
            default:
                break;
        }
        if(card instanceof ISpecialCard)
            cardValue += 0.5;
        if(myTreeNode.failedCard[state.actualPlayerIndex] != null && card.getColor() == myTreeNode.failedCard[state.actualPlayerIndex].getColor())
            cardValue += 1.0;
        return cardValue;
    }
//*/
    /**
     *
     * @param nodeToExplore
     * @param simulationResult
     */
    public void backpropagation(MyTreeNodeV2 nodeToExplore, int simulationResult)
    {
        while(nodeToExplore != null)
        {
            nodeToExplore.visitCount++;
            nodeToExplore.winCount += simulationResult;
            nodeToExplore = nodeToExplore.parent;
        }
    }
}
