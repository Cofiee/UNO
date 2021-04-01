/*
* Autor: Rafał Topolski
* Cel: obsługa logiki gry
* */
package game.myAssets;

import game.ControllerGame;
import game.myAssets.cards.*;
import javafx.scene.control.Alert;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGame
{
    private ControllerGame controllerGame;
    Player[] players;
    enum Direction
    {
        CLOCKWISE,
        COUNTERCLOCKWISE
    }
    Direction direction = Direction.CLOCKWISE;
    int iLastPlayer;
    int iActualPlayer = 0;
    Stack<ACard> table = new Stack<>();
    Vector<ACard> deck = new Vector<>();
    RegularCard.Color topColor;
    int numberOfTakenCards = 0;

    public EngineGame(ControllerGame controller)
    {
        this.controllerGame = controller;
    }
    public void initializePlayers(int numberOfPlayers, int numberOfAi)
    {
        if(numberOfPlayers > 4) return;//throw new Exception();
        if(numberOfAi > 4 - numberOfPlayers) return;
        players = new Player[numberOfPlayers + numberOfAi];
        this.iLastPlayer = players.length - 1;
        for(int i = 0; i < numberOfPlayers; ++i)
            players[i] = new Player();
        //AI
    }
    public Player[] getPlayers()
    {
        return players;
    }
    public int getIActualPlayer()
    {
        return iActualPlayer;
    }
    public Player actualPlayer()
    {
        return players[iActualPlayer];
    }
    public Direction getDirection()
    {
        return direction;
    }
    public ACard.Color getTopColor()
    {
        return topColor;
    }
    public void setTopColor(RegularCard.Color color)
    {
        this.topColor = color;
    }
    public ControllerGame getControllerGame(){return controllerGame;}
    public String getTopDigitString()
    {
        if(table.peek() instanceof RegularCard)
            return  String.valueOf(((RegularCard) table.peek()).getDigit());
        else
            return table.peek().getClass().getSimpleName();
    }
    public ACard getTopCard()
    {
        return table.peek();
    }
    public void  prepareGame()
    {
        initializePlayers(4,0);
        table.clear();
        prepareDeck();
        Collections.shuffle(deck);
        for(Player player : players)
        {
            player.getHand().clear();
            for(int i = 0; i < 7; ++i)
                player.getHand().add(deck.remove(0));
        }
        table.add(deck.remove(0));
        while (table.peek() instanceof ISpecialCard)
        {
            deck.add(table.pop());
            table.push(deck.remove(0));
        }
        this.topColor = table.peek().getColor();
        try{controllerGame.updateTopCard();}
        catch (Exception e){}
        controllerGame.updateColorIcon(topColor);
    }
    public void prepareDeck()
    {
        deck.clear();
        for(ACard.Color color : ACard.Color.values())
        {
            if(color == ACard.Color.BLACK) break;
            for (int i = 0; i < 10; ++i)
            {
                deck.add(new RegularCard(i, color));
            }
            deck.add(new StopCard(color));
            deck.add(new StopCard(color));
            deck.add(new SwitchCard(color));
            deck.add(new SwitchCard(color));
            deck.add(new TakeTwoCard(color));
            deck.add(new TakeTwoCard(color));
        }
        for(int i = 0; i < 4; ++i)
        {
            deck.add(new ChColorCard());
            deck.add(new TakeFourCard());
        }
    }
    public Player nextPLayer()
    {
        switch (direction)
        {
            case CLOCKWISE:
                if(iActualPlayer == iLastPlayer) return players[0];
                else return players[iActualPlayer + 1];
            case COUNTERCLOCKWISE:
                if(iActualPlayer == 0) return players[iLastPlayer];
                else return players[iActualPlayer - 1];
        }
        return null;
    }
    public void clearTable()
    {
        ACard topCard = table.pop();
        deck.addAll(table);
        Collections.shuffle(deck);
        table.clear();
        table.push(topCard);
    }
    public void beginTurn()
    {
        if(actualPlayer().isFrozen())
        {
            actualPlayer().unfreeze();
            //endTurn();
        }
        if(numberOfTakenCards > 0)
        {
            takeCards();
            //endTurn();
        }
    }
    public void switchDirection()
    {
        if(direction == Direction.CLOCKWISE)
            direction = Direction.COUNTERCLOCKWISE;
        else
            direction = Direction.CLOCKWISE;
    }
    public void cardOnTable(int id)
    {
        ACard card = actualPlayer().getHand().elementAt(id);
        ACard topCard = table.peek();
        if(card.getColor() == ACard.Color.BLACK)
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
            ((ISpecialCard) card).action(this);
        }
        else if(card instanceof ISpecialCard && card.getColor() == topColor)
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
            ((ISpecialCard) card).action(this);
        }
        else if(topCard instanceof ISpecialCard && card.getColor() == topColor)
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
        }
        else if(((RegularCard)card).getDigit() == ((RegularCard)topCard).getDigit() ||
                 card.getColor() == topColor)
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
            setTopColor(table.peek().getColor());
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("OOPS");
            alert.setContentText("You picked the wrong card fool!");
            alert.showAndWait();
        }
        /*
        if(card instanceof ChColorCard || card instanceof TakeFourCard ||
                (card instanceof ISpecialCard && card.getColor() == topColor))
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
            ((ISpecialCard) card).action(this);
        }
        else if(topCard instanceof ISpecialCard && card instanceof RegularCard)
        {
            if(card.getColor() == topColor)
            {
                table.add(card);
                actualPlayer().getHand().remove(id);
                topColor = getTopCard().getColor();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("OOPS");
                alert.setContentText("You picked the wrong card fool");
                alert.showAndWait();
            }
        }
        else if(card instanceof RegularCard && topCard instanceof RegularCard)
        {
            if(((RegularCard) card).getDigit() == ((RegularCard) topCard).getDigit() ||
            card.getColor() == topColor)
            {
                table.add(card);
                actualPlayer().getHand().remove(id);
                topColor = getTopCard().getColor();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("OOPS");
                alert.setContentText("You picked the wrong card fool");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("OOPS");
            alert.setContentText("You picked the wrong card fool");
            alert.showAndWait();
        }*/
    }
    /*
    * Metoda pobiera jedna karte dla gracza i pyta gracza czy chce rzucic na stol czy zachowac
    * w rece
     */
    public void takeOne()
    {
        ACard firstCard;
        if(deck.isEmpty())
            clearTable();
        firstCard = deck.remove(0);
        if(firstCard instanceof RegularCard && getTopCard() instanceof RegularCard)
        {
            if(firstCard.getColor() == topColor
                    || ((RegularCard)firstCard).getDigit() == ((RegularCard) table.peek()).getDigit())
            {
                if(controllerGame.takeOrFake(firstCard))
                    table.add(firstCard);
                else
                    actualPlayer().getHand().add(firstCard);
            }
            else
                actualPlayer().getHand().add(firstCard);
        }
        else if(firstCard instanceof RegularCard && getTopCard() instanceof ISpecialCard)
        {
            if(firstCard.getColor() == topColor)
            {
                if(controllerGame.takeOrFake(firstCard))
                    table.add(firstCard);
                else
                    actualPlayer().getHand().add(firstCard);
            }
            else
                actualPlayer().getHand().add(firstCard);
        }
        else
        {
            actualPlayer().getHand().add(firstCard);
        }
    }
    /*
    * Metoda pobiera karty dla gracze i sprawdza czy pierwsza karta ratuje gracza od dobierania
    * */
    public void takeCards()
    {
        //Czy gracz posiada karte
        if(getTopCard() instanceof TakeTwoCard)
        {
            int index = actualPlayer().isTakeTwoCardPossessed();
            if(index >= 0)
            {
                ACard card = actualPlayer().getHand().remove(index);
                if(controllerGame.takeOrFake(card))
                {
                    table.add(card);
                    return;
                }
            }
        }
        else if(getTopCard() instanceof TakeFourCard)
        {
            int index = actualPlayer().isTakeFourCardPossessed();
            if(index >= 0)
            {
               ACard card = actualPlayer().getHand().remove(index);
               if(controllerGame.takeOrFake(card))
               {
                    table.add(card);
                    return;
               }
            }
        }
        //Czy pierwsza karta ratuje
        ACard firstCard;
        if(deck.isEmpty())
            clearTable();
        firstCard = deck.remove(0);
        if(firstCard instanceof TakeTwoCard && table.peek() instanceof TakeTwoCard ||
            firstCard instanceof TakeFourCard && table.peek() instanceof TakeFourCard)
        {
            table.add(firstCard);
            ((ISpecialCard) firstCard).action(this);
            return;
        }
        else
        {
            --numberOfTakenCards;
            actualPlayer().getHand().add(firstCard);
            while(numberOfTakenCards > 0)
            {
                --numberOfTakenCards;
                if(deck.isEmpty())
                    clearTable();
                actualPlayer().getHand().add(deck.remove(0));
            }
        }
    }
    /*
    * Metoda przesowa wskaznik aktualnego gracza na nastepna pozycje uwzgledniajac kierunek rozgrywki
    * Zwraca true gdy jeden z graczy wygra
    * */
    public boolean endTurn()
    {
        if(actualPlayer().getHand().size() == 0)
        {
            endGame();
            return true;
        }
        if(direction == Direction.CLOCKWISE)
        {
            if(iActualPlayer == iLastPlayer)
                iActualPlayer = 0;
            else
                iActualPlayer++;
        }
        else
        {
            if(iActualPlayer == 0)
                iActualPlayer = iLastPlayer;
            else
                iActualPlayer--;
        }
        return false;
    }
    public void endGame()
    {

    }
    public String parseCard(ACard card)
    {
        String fileName = new String();
        if(card instanceof TakeFourCard)
            fileName += "wild_pick_four_large";
        else if(card instanceof ChColorCard)
            fileName += "wild_colora_changer_large";
        else
        {
           switch(card.getColor())
           {
               case RED:
                   fileName += "red_";
                   break;
               case BLUE:
                   fileName += "blue_";
                   break;
               case YELLOW:
                   fileName += "yellow_";
                   break;
               case GREEN:
                   fileName += "green_";
                   break;
           }
           if(card instanceof RegularCard)
               fileName += Integer.toString(((RegularCard) card).getDigit()) + "_";
           else if(card instanceof StopCard)
               fileName += "skip_";
           else if (card instanceof TakeTwoCard)
               fileName += "picker_";
           else
               fileName += "reverse_";
           fileName += "large";
        }
        return fileName += ".png";
    }
}
