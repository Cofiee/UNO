package game.myAssets.AI;

import game.myAssets.GameStateV2;
import game.myAssets.cards.ACard;

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
        head = new MyTreeNodeV2(state,myHand);
    }

    /**
     *  Zwraca znaleziona karte do rzucenia
     * @return ACard - znaleziona karta lub
     * @return null - w przypadku braku dopasowania
     */
    public ACard search()
    {
        /*
        if(head.getPossibleMoves().size() == 0)
            return null;
        if(head.children.size() == 0)
            head.createChildren();
        for(int i = 0; i < this.ITERATIONS; ++i)
        {
            MyTreeNodeV2 bestChild = selection(head); //Selection
            if(bestChild.children.size() == 0) //expantion
                bestChild.createChildren();
            int randomIndex = randomGenerator.nextInt(bestChild.children.size());
            MyTreeNodeV2 nodeToExploration = bestChild.children.get(randomIndex);
            nodeToExploration = clone(nodeToExploration);
            nodeToExploration.determine(); // DETERMINIZACJA
            int outcome = simulation(nodeToExploration); //rollout
            backpropagation(bestChild, outcome); //backpropagation
        }
        System.out.println("Liczba Dzieci: " + head.children.size());
        int i = 0;
        for (MyTreeNodeV2 child: head.children)
        {
            System.out.println("Dziecko " + i++ + "  " + child.visitCount + "  " + child.winCount);
        }
        MyTreeNodeV2 winnerNode = selection(head);
        return winnerNode.state.table.peek();*/
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
        double c = 2.0; //state parameter
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
        //randomChoice.state.failedCard = null;
        return randomChoice;
    }
    /**
     * @return metoda zwraca wartosc wyboru
     * */
    public double evaluate(MyTreeNodeV2 myTreeNode)
    {
        double cardValue = 0.0;
        GameStateV2 state = myTreeNode.state;
        switch (state.table.peek().getColor())
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
        /*
        if(state.failedCard != null)
        if(state.failedCard.get(state.actualPlayerIndex).getColor() == state.table.peek().getColor())
        {
            cardValue += 0.5;
        }*

         */
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

    /**
     *
     * @param node
     * @return
     */
    private MyTreeNodeV2 clone(MyTreeNodeV2 node)
    {
        MyTreeNodeV2 clonedNode = new MyTreeNodeV2(node);
        return clonedNode;
    }
}
