package game.myAssets.AI;

import game.myAssets.GameState;
import game.myAssets.cards.ACard;

import java.util.*;

public class MyTreeMonteCarlo
{
    private final int ITERATIONS = 100000;

    private Random randomGenerator = new Random();
    MyTreeNode head;
    ArrayList<Integer> moves;

    /**
     * Konstruktor tworzy obiekt obslugujacy wyszukiwanie monte carlo
     * @param state
     * @param isMaxPlayer
     * @param myHand
     */
    public MyTreeMonteCarlo(GameState state, boolean isMaxPlayer, Vector<ACard> myHand)
    {
        GameState deepClone = new GameState(state);
        for (ACard card: myHand)
        {
            deepClone.cardSet.remove(card);
        }
        head = new MyTreeNode(deepClone, isMaxPlayer, myHand);
    }

    public MyTreeNode getHead()
    {
        return head;
    }
/*
    public void setHead(MyTreeNode head)
    {
        this.head = head;
    }
*/

    /**
     *
     * @return
     */
    public ACard search()
    {
        if(head.getChildren().size() == 0)
            head.createChildren();
        for(int i = 0; i < this.ITERATIONS; ++i)
        {
            MyTreeNode bestChild = selection(head); //Selection

            if(bestChild.getChildren().size() == 0) //expantion
                bestChild.createChildren();
            MyTreeNode nodeToExploration = bestChild;
            //-----
            nodeToExploration = cloneAndRandomize(nodeToExploration);
            //-----
            if(bestChild.getChildren().size() > 0)
            {
                int randomIndex = randomGenerator.nextInt(bestChild.getChildren().size());
                nodeToExploration = bestChild.getChildren().get(randomIndex);
            }
            int outcome = simulation(nodeToExploration); //rollout
            backpropagation(bestChild, outcome); //backpropagation
        }
        System.out.println("Liczba Dzieci: " + head.children.size());
        int i = 0;
        for (MyTreeNode child:
                head.children)
        {
            System.out.println("Dziecko " + i++ + "  " + child.visitCount + "  " + child.winCount);
        }
        MyTreeNode winnerNode = selection(head);
        return winnerNode.getTopCard();
    }

    /**
     *
     * @param node
     * @return
     */
    public MyTreeNode selection(MyTreeNode node)
    {
        Vector<MyTreeNode> children = node.getChildren();
        MyTreeNode bestMove = children.get(0);
        double bestUTC = Integer.MIN_VALUE;
        if(children.size() > 0)
        {
            for (MyTreeNode child: children)
            {
                int parentVisitCount = node.getVisitCount();
                int childVisitCount = child.getVisitCount();
                int win = child.getWinCount();
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
    public int simulation(MyTreeNode node)
    {
        MyTreeNode tmpNode = node;
        while(tmpNode.getMyHand().size() != 0 && tmpNode.getOpponentHandSize() != 0 && tmpNode.getDeck().size() != 0)
        {
            tmpNode = randomPlay(tmpNode);
        }
        if(tmpNode.getMyHand().size() == 0)
        {
            return 1;
        }else if(tmpNode.getOpponentHandSize() == 0)
        {
            return -1;
        }else
        {
            return 0;
        }
    }

    /**
     *
     * @param node
     * @return
     */
    ///*
    public MyTreeNode randomPlay(MyTreeNode node)
    {
        node.createChildren();
        int bounds = node.getChildren().size();
        MyTreeNode radomChoice = node.getChildren().get(randomGenerator.nextInt(bounds));
        return radomChoice;
    }
//*/
    /*
    public MyTreeNode randomPlay(MyTreeNode node)
    {
        node.createChildren();
        int bestIndex = 0;
        double evaluation = Integer.MIN_VALUE;
        Vector<MyTreeNode> children = node.getChildren();
        for(int i = 0; i > children.size(); ++i)
        {
            double result = evaluate(children.get(i));
            if(result > evaluation)
            {
                evaluation = result;
                bestIndex = i;
            }
        }
        //int bounds = node.getChildren().size();
        //MyTreeNode radomChoice = node.getChildren().get(randomGenerator.nextInt(bounds));
        MyTreeNode radomChoice = children.get(bestIndex);
        radomChoice.getState().failedCard = null;
        return radomChoice;
    }
*/
    /*
    public double evaluate(MyTreeNode myTreeNode)
    {
        GameState state = myTreeNode.getState();
        if(state)
    }
*/
    /**
     *
     * @param nodeToExplore
     * @param simulationResult
     */
    public void backpropagation(MyTreeNode nodeToExplore, int simulationResult)
    {
        while(nodeToExplore != null)
        {
            nodeToExplore.visitCount++;
            nodeToExplore.winCount += simulationResult;
            nodeToExplore = nodeToExplore.getParent();
        }
    }

    /**
     *
     * @param node
     * @return
     */
    private MyTreeNode cloneAndRandomize(MyTreeNode node)
    {
        MyTreeNode clonedNode = new MyTreeNode(node.getTopCard(), true, node );
        clonedNode.determine();
        return clonedNode;
    }
}
