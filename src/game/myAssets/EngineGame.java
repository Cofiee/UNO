/*
* Autor: Rafał Topolski
* Cel: obsługa logiki gry
* */
package game.myAssets;

import game.ControllerGame;
import game.myAssets.AI.AIPlayer;
import game.myAssets.cards.*;
import javafx.scene.control.Alert;
import scoreboard.ScoreManager;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGame
{
    public int MAX_PLAYERS_NUMBER = 4;
    private final ControllerGame controllerGame;
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
    int numberOfTakenCards = 0;

    public EngineGame(ControllerGame controller)
    {
        this.controllerGame = controller;
    }
    public void initializePlayers(int numberOfPlayers, int numberOfAi)
    {
        if(numberOfPlayers > MAX_PLAYERS_NUMBER) return;//throw new Exception();
        if(numberOfAi > MAX_PLAYERS_NUMBER - numberOfPlayers) return;
        players = new Player[numberOfPlayers + numberOfAi];
        this.iLastPlayer = players.length - 1;
        for(int i = 0; i < numberOfPlayers; ++i)
            players[i] = new Player();
        for(int i = numberOfPlayers; i < numberOfPlayers + numberOfAi; ++i)
            ;//players[i] = new AIPlayer(table);
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
    public ControllerGame getControllerGame(){return controllerGame;}
    public int getNumberOfTakenCards()
    {
        return this.numberOfTakenCards;
    }
    public void setNumberOfTakenCards(int number)
    {
        numberOfTakenCards = number;
    }
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
    public Player getNextPLayer()
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
    public void  prepareGame()
    {
        if(players == null)
        {
          initializePlayers(1, 1);
        }
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
        controllerGame.updateTopCard();
        controllerGame.updateColorIcon(table.peek().getColor());
        int[] points = new int[4];
        controllerGame.refreshScoreboard(points);
    }
    public void prepareDeck()
    {
        deck.clear();
        for(ACard.Color color : ACard.Color.values())
        {
            if(color == ACard.Color.BLACK) break;
            deck.add(new RegularCard(0, color));
            for (int i = 1; i < 10; ++i)
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
        /*
        for(int i = 0; i < 4; ++i)
        {
            deck.add(new ChColorCard());
            deck.add(new TakeFourCard());
        }
         */
    }
    public void clearTable()
    {
        ACard topCard = table.pop();
        deck.addAll(table);
        for (ACard card:
             deck)
        {
            if(card instanceof TakeFourCard || card instanceof ChColorCard)
                card.setColor(ACard.Color.BLACK);
        }
        Collections.shuffle(deck);
        table.clear();
        table.push(topCard);
    }
    public void switchDirection()
    {
        if(direction == Direction.CLOCKWISE)
            direction = Direction.COUNTERCLOCKWISE;
        else
            direction = Direction.CLOCKWISE;
    }
    public boolean isCardMatch(ACard card)
    {
        if(card instanceof ISpecialCard)
        {
            if(card.getColor() == ACard.Color.BLACK)
            {
                return true;
            }
            else if(card.getClass() == table.peek().getClass())
            {
                return true;
            }
            else if(card.getColor() == table.peek().getColor())
            {
                return true;
            }
        }
        else
        {
            if (card.getClass() == table.peek().getClass())
            {
                if (card.getColor() == table.peek().getColor()
                        || ((RegularCard) card).getDigit() == ((RegularCard) table.peek()).getDigit())
                {
                    return true;
                }
            }else if(card.getColor() == table.peek().getColor())
            {
                return true;
            }
        }
        return false;
    }
    public void cardOnTable(int id)
    {
        ACard card = actualPlayer().getHand().elementAt(id);
        ACard topCard = table.peek();
        if(isCardMatch(card))
        {
            table.add(card);
            actualPlayer().getHand().remove(id);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("OOPS");
            alert.setContentText("You picked the wrong card fool!");
            alert.showAndWait();
            return;
        }
        if(card instanceof ISpecialCard)
            ((ISpecialCard) card).action(this);
        nextTurn();
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
        if(isCardMatch(firstCard))
        {
            if(controllerGame.matchCardDialog(firstCard))
            {
                table.add(firstCard);
                if(firstCard instanceof ISpecialCard)
                    ((ISpecialCard) firstCard).action(this);
            }
            else
            {
                actualPlayer().getHand().add(firstCard);
            }
        }
        else
        {
            controllerGame.takeCardDialog(firstCard);
            actualPlayer().getHand().add(firstCard);
        }
        nextTurn();
    }
    private void takeOneAi()
    {
        ACard firstCard;
        if(deck.isEmpty())
            clearTable();
        firstCard = deck.remove(0);
        if(isCardMatch(firstCard))
        {
            table.add(firstCard);
            if(firstCard instanceof ISpecialCard)
                ((ISpecialCard) firstCard).action(this);
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
                if(actualPlayer() instanceof AIPlayer)
                {
                    table.add(card);
                    ((ISpecialCard)card).action(this);
                    nextTurn();
                    return;
                }
                else if(controllerGame.matchCardDialog(card))
                {
                    table.add(card);
                    ((ISpecialCard)card).action(this);
                    nextTurn();
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
                if(actualPlayer() instanceof AIPlayer)
                {
                    table.add(card);
                    ((ISpecialCard)card).action(this);
                    nextTurn();
                    return;
                }
                else if(controllerGame.matchCardDialog(card))
                {
                    table.add(card);
                    ((ISpecialCard)card).action(this);
                    nextTurn();
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
            nextTurn();
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
        nextTurn();
    }
    /*
    * Metoda przesowa wskaznik aktualnego gracza na nastepna pozycje uwzgledniajac kierunek rozgrywki
    * Zwraca true gdy jeden z graczy wygra
    * */
    public void nextTurn()
    {
        controllerGame.disableAll();
        do
        {
            if(actualPlayer().getHand().isEmpty())
            {
                endMatch(actualPlayer());
                if(actualPlayer().getScore() > 10)
                {
                    endGame();
                    controllerGame.switchToMainMenu();
                    return;
                }
                else
                {
                    prepareDeck();
                    prepareGame();
                    return;
                }
            }
            do
            {
                actualPlayer().unfreeze();
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
            }while (actualPlayer().isFrozen());
            if(numberOfTakenCards > 0)
            {
                takeCards();
                continue;
            }
            controllerGame.nextPlayerDialog();
            if(actualPlayer() instanceof AIPlayer)
            {
                //((AIPlayer)actualPlayer()).profileOpponent(getNextPLayer());
                playAi();
            }
        }while (actualPlayer() instanceof AIPlayer);
        controllerGame.enableAll();
    }
    public void endMatch(Player winner)
    {
        int points = 0;
        for (Player player : players)
        {
            if(player == winner)
                continue;
            for (ACard card: player.getHand())
            {
                points += card.getPoints();
            }
        }
        winner.addScore(points);
    }
    public void endGame()
    {
        ScoreManager scoreManager = new ScoreManager("scoreboard.txt");
        scoreManager.saveScore(players);
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
    public void playAi()
    {
        if(((AIPlayer)actualPlayer()).matchMyCards())
        {
            ACard card = ((AIPlayer)actualPlayer()).playCard();
            if(card instanceof ISpecialCard)
                ((ISpecialCard) card).action(this);
            table.add(card);
        }
        else
            takeOneAi();
    }
    public ACard.Color chColorAction()
    {
        if(actualPlayer() instanceof AIPlayer)
            return ((AIPlayer) actualPlayer()).maxColorQuantity();
        else
            return controllerGame.chColorAlert();
    }
}
