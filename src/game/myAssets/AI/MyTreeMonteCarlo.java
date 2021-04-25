package game.myAssets.AI;

import game.myAssets.cards.ACard;

import java.util.*;

public class MyTreeMonteCarlo
{
    private final int ITERATIONS = 10;

    private Random randomGenerator = new Random();
    MyTreeNode head;
    ArrayList<Integer> moves;


    public MyTreeMonteCarlo(ACard topCard, boolean isMaxPlayer, Vector<ACard> myHand, int opponentHandSize ,Vector<ACard> deck)
    {
        head = new MyTreeNode(topCard, isMaxPlayer, myHand, deck);
    }

    public MyTreeNode getHead()
    {
        return head;
    }

    public void setHead(MyTreeNode head)
    {
        this.head = head;
    }

    //ISMCTS
    public ACard search()
    {
        //ArrayList<ACard> matchingCards = getPossibleMoves(node);
        //DOKONCZYC ISMCTS
        for(int i = 0; i < this.ITERATIONS; ++i)
        {
            MyTreeNode bestChild = selection(head);
            if(bestChild.getMyHand().size() != 0) //expantion
                bestChild.createChildren();
            MyTreeNode nodeToExploration = bestChild;
            if(bestChild.getChildren().size() > 0)
            {
                int randomIndex = randomGenerator.nextInt(bestChild.getChildren().size());
                nodeToExploration = bestChild.getChildren().get(randomIndex);
            }
            int outcome = simulation(nodeToExploration); //rollout
            backpropagation(nodeToExploration, outcome); //backpropagation
            System.out.println(nodeToExploration.visitCount + "  " + nodeToExploration.winCount);
        }
        MyTreeNode winnerNode = selection(head);
        head = winnerNode;
        return winnerNode.getTopCard();
    }
    public MyTreeNode selection(MyTreeNode rootNode)
    {
        if(rootNode.getChildren().size() == 0)
            rootNode.createChildren();
        Vector<MyTreeNode> children = rootNode.getChildren();
        MyTreeNode bestMove = children.get(0);
        double bestUTC = Integer.MIN_VALUE;
        if(children.size() > 0)
        {
            for (MyTreeNode child: children)
            {
                int parentVisitCount = rootNode.getVisitCount();
                int childVisitCount = child.getVisitCount();
                if(childVisitCount == 0)
                {
                    childVisitCount = Integer.MAX_VALUE;
                }
                double vi = child.getWinCount() / childVisitCount;
                double calcedUTC = calcUCT(vi, parentVisitCount, childVisitCount);
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
     * Vi is the average reward/value of all nodes beneath this node
     * N is the number of times the parent node has been visited, and
     * ni is the number of times the child node i has been visited
     * */
    private double calcUCT(double vi, int bigN, int ni)
    {
        double c = 2.0; //state parameter
        double rootBase = Math.log(bigN) / ni;
        return vi + c * Math.sqrt(rootBase);
    }

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

    public MyTreeNode randomPlay(MyTreeNode node)
    {
        node.createChildren();
        int bounds = node.getChildren().size();
        MyTreeNode radomChoice = node.getChildren().get(randomGenerator.nextInt(bounds));
        return radomChoice;
    }

    public void backpropagation(MyTreeNode nodeToExplore, int simulationResult)
    {
        nodeToExplore.visitCount++;
        nodeToExplore.winCount += simulationResult;
    }
}
