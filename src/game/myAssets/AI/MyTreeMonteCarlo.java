package game.myAssets.AI;

import game.myAssets.cards.ACard;

import java.util.*;

public class MyTreeMonteCarlo
{
    private final int ITERATIONS = 10;

    private Random randomGenerator = new Random();
    MyTreeNode head;
    ArrayList<Integer> moves;


    public MyTreeMonteCarlo(ACard topCard, boolean isMaxPlayer, Vector<ACard> myHand, Vector<ACard> deck)
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

    public void createTree()
    {
        if(head == null)
            return;
        createTreeRecursive(head);
    }

    private void createTreeRecursive(MyTreeNode parent)
    {

    }

    //ISMCTS
    public void search()
    {
        //ArrayList<ACard> matchingCards = getPossibleMoves(node);
        //DOKONCZYC ISMCTS
        for(int i = 0; i < this.ITERATIONS; ++i)
        {
            MyTreeNode bestChild = selection(head);
            if(bestChild.getMyHand().size() != 0)
                expand(bestChild);
            MyTreeNode nodeToExploration = bestChild;
            if(bestChild.getChildren().size() >0)
            {
                int randomIndex = randomGenerator.nextInt(bestChild.getChildren().size());
                nodeToExploration = bestChild.getChildren().get(randomIndex);
            }
            explore(nodeToExploration);
        }
    }

    public MyTreeNode selection(MyTreeNode rootNode)
    {
        Vector<MyTreeNode> children = rootNode.getChildren();
        MyTreeNode bestMove = children.get(0);
        double bestUTC = 0;
        if(children.size() > 0)
        {
            for (MyTreeNode child: children)
            {
                int parentVisitCount = rootNode.getVisitCount();
                double vi = child.getWinCount() / child.getVisitCount();
                double calcedUTC = calcUCT(vi, parentVisitCount, child.getVisitCount());
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
        if(ni == 0)
        {
            ni = Integer.MAX_VALUE;
        }
        double c = 2.0; //state parameter
        double rootBase = Math.log(bigN) / ni;
        return vi + c * Math.sqrt(rootBase);
    }

    public void expand(MyTreeNode node)
    {
        node.createChildren();
    }

    public void explore(MyTreeNode node)
    {
        while(node.getMyHand().size() == 0)
        {
            node = randomPlay(node);
        }
    }

    public MyTreeNode randomPlay(MyTreeNode node)
    {
        node.createChildren();
        int bounds = node.getChildren().size();
        node.getChildren().get(randomGenerator.nextInt(bounds));
        return node;
    }
    //TODO backpropagacja
    public void backpropagation()
    {

    }
}
