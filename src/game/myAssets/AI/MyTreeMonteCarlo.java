package game.myAssets.AI;

import game.myAssets.cards.ACard;

import java.util.*;

public class MyTreeMonteCarlo
{
    private final int ITERATIONS = 3000;

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
        if(head.getChildren().size() == 0)
            head.createChildren();
        for(int i = 0; i < this.ITERATIONS; ++i)
        {
            MyTreeNode bestChild = selection(head);

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
        for (MyTreeNode child:
                head.children)
        {
            System.out.println(child.visitCount + "  " + child.winCount);
        }
        MyTreeNode winnerNode = selection(head);
        return winnerNode.getTopCard();
    }

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
        while(nodeToExplore != null)
        {
            nodeToExplore.visitCount++;
            nodeToExplore.winCount += simulationResult;
            nodeToExplore = nodeToExplore.getParent();
        }
    }

    private MyTreeNode cloneAndRandomize(MyTreeNode node)
    {
        MyTreeNode clonedNode = new MyTreeNode(node.getTopCard(), true, node );
        clonedNode.determine();
        return clonedNode;
    }
}
