package game.myAssets;

import game.ControllerGameSp;
import game.myAssets.AI.AIPlayer;
import game.myAssets.cards.*;
import javafx.scene.control.Alert;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

public class EngineGameSpV2
{
    public int MAX_PLAYERS_NUMBER = 4;
    private ControllerGameSp controllerGame;

    GameStateV2 state;

    private EngineGameSpV2()
    {
        state = new GameStateV2();
    }
    public EngineGameSpV2(ControllerGameSp controller)
    {
        this();
        this.controllerGame = controller;
    }

    public int getIActualPlayer()
    {
        return state.actualPlayerIndex;
    }

    public Player actualPlayer()
    {
        return state.players.get(state.actualPlayerIndex);
    }

    public ACard getTopCard()
    {
        return state.table.peek();
    }

    public void initializePlayers(int numberOfAi)
    {
        state.players.add(new Player());
        state.playersHandsSizes = new int[state.players.size() + numberOfAi];
        for(int i = 0; i < state.players.size(); ++i)
        {
            state.playersHandsSizes[i] = 7;
        }
        for (int i = state.players.size(); i <= numberOfAi; ++i)
        {
            state.players.add(new game.myAssets.AI.AIPlayer(state));
            state.playersHandsSizes[i] = 7;
        }
        state.lastPlayerIndex = state.players.size() - 1;
    }

    public void  prepareGame()
    {
        state.table.clear();
        prepareDeck();
        Collections.shuffle(state.deck);
        for(Player player : state.players)
        {
            player.getHand().clear();
            for(int i = 0; i < 7; ++i)
                player.getHand().add(state.deck.remove(0));
        }
        state.table.add(state.deck.remove(0));
        while (state.table.peek() instanceof ISpecialCard)
        {
            state.deck.add(state.table.pop());
            state.table.push(state.deck.remove(0));
        }
        controllerGame.updateTopCard();
        controllerGame.updateColorIcon(state.table.peek().getColor());
        int[] points = new int[4];
        controllerGame.refreshScoreboard(points);
    }

    public void prepareDeck()
    {
        state.deck.clear();
        for(ACard.Color color : ACard.Color.values())
        {
            if(color == ACard.Color.BLACK) break;
            ACard zero = new RegularCard(0, color);
            state.deck.add(zero);
            state.cardSet.add(zero);
            for (int i = 1; i < 10; ++i)
            {
                ACard first = new RegularCard(i, color);
                ACard second = new RegularCard(i, color);
                state.deck.add(first);
                state.deck.add(second);
                state.cardSet.add(first);
                state.cardSet.add(second);
            }
            //deck.add(new StopCard(color));
            //deck.add(new StopCard(color));
            //deck.add(new SwitchCard(color));
            //deck.add(new SwitchCard(color));
            //deck.add(new TakeTwoCard(color));
            //deck.add(new TakeTwoCard(color));
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
        ACard topCard = state.table.pop();
        state.deck.addAll(state.table);
        for (ACard card:
                state.deck)
        {
            if(card instanceof TakeFourCard || card instanceof ChColorCard)
                card.setColor(ACard.Color.BLACK);
        }
        Collections.shuffle(state.deck);
        state.table.clear();
        state.table.push(topCard);
    }

    public boolean isCardMatch(ACard card)
    {
        if(card instanceof ISpecialCard)
        {
            if(card.getColor() == ACard.Color.BLACK)
            {
                return true;
            }
            else if(card.getClass() == state.table.peek().getClass())
            {
                return true;
            }
            else if(card.getColor() == state.table.peek().getColor())
            {
                return true;
            }
        }
        else
        {
            if (card.getClass() == state.table.peek().getClass())
            {
                if (card.getColor() == state.table.peek().getColor()
                        || ((RegularCard) card).getDigit() == ((RegularCard) state.table.peek()).getDigit())
                {
                    return true;
                }
            }else if(card.getColor() == state.table.peek().getColor())
            {
                return true;
            }
        }
        return false;
    }

    public void cardOnTable(int id)
    {
        ACard card = actualPlayer().getHand().elementAt(id);
        if(isCardMatch(card))
        {
            //state.failedCard = null;
            state.cardSet.remove(card);
            state.table.add(card);
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
            ;//((ISpecialCard) card).action(this);
        nextTurn();
    }

    /**
     * Metoda pobiera jedna karte dla gracza i pyta gracza czy chce rzucic na stol czy zachowac w rece
     */
    public void takeOne()
    {
        //state.failedCard.set(state.actualPlayerIndex,state.table.peek());
        ACard firstCard;
        if(state.deck.isEmpty())
            clearTable();
        firstCard = state.deck.remove(0);
        if(isCardMatch(firstCard))
        {
            if(controllerGame.matchCardDialog(firstCard))
            {
                state.table.add(firstCard);
                if(firstCard instanceof ISpecialCard)
                    ;//((ISpecialCard) firstCard).action(this);
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

    /**
     * Metoda pobiera jedna karte dla gracza AI
     */
    private void takeOneAi()
    {
        ACard firstCard;
        if(state.deck.isEmpty())
            clearTable();
        firstCard = state.deck.remove(0);
        if(isCardMatch(firstCard))
        {
            state.table.add(firstCard);
            if(firstCard instanceof ISpecialCard)
                ;//((ISpecialCard) firstCard).action(this);
        }
        else
        {
            actualPlayer().getHand().add(firstCard);
        }
    }
    /**
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
                    state.table.add(card);
                    //((ISpecialCard)card).action(this);
                    nextTurn();
                    return;
                }
                else if(controllerGame.matchCardDialog(card))
                {
                    state.table.add(card);
                    //((ISpecialCard)card).action(this);
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
                    state.table.add(card);
                    //((ISpecialCard)card).action(this);
                    nextTurn();
                    return;
                }
                else if(controllerGame.matchCardDialog(card))
                {
                    state.table.add(card);
                    //((ISpecialCard)card).action(this);
                    nextTurn();
                    return;
                }
            }
        }
        //Czy pierwsza karta ratuje
        ACard firstCard;
        if(state.deck.isEmpty())
            clearTable();
        firstCard = state.deck.remove(0);
        if(firstCard instanceof TakeTwoCard && state.table.peek() instanceof TakeTwoCard ||
                firstCard instanceof TakeFourCard && state.table.peek() instanceof TakeFourCard)
        {
            state.table.add(firstCard);
            //((ISpecialCard) firstCard).action(this);
            nextTurn();
            return;
        }
        else
        {
            --state.numberOfTakenCards;
            actualPlayer().getHand().add(firstCard);
            while(state.numberOfTakenCards > 0)
            {
                --state.numberOfTakenCards;
                if(state.deck.isEmpty())
                    clearTable();
                actualPlayer().getHand().add(state.deck.remove(0));
            }
        }
        nextTurn();
    }

    /**
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
            state.playersHandsSizes[state.actualPlayerIndex] = actualPlayer().getHand().size();
            controllerGame.updateHandSizes(state.actualPlayerIndex, state.playersHandsSizes[state.actualPlayerIndex]);
            do
            {
                actualPlayer().unfreeze();
                if(state.direction == GameStateV2.Direction.CLOCKWISE)
                {
                    if(state.actualPlayerIndex == state.lastPlayerIndex)
                        state.actualPlayerIndex = 0;
                    else
                        state.actualPlayerIndex++;
                }
                else
                {
                    if(state.actualPlayerIndex == 0)
                        state.actualPlayerIndex = state.lastPlayerIndex;
                    else
                        state.actualPlayerIndex--;
                }
            }while (actualPlayer().isFrozen());
            if(this.state.numberOfTakenCards > 0)
            {
                takeCards();
                continue;
            }
            controllerGame.nextPlayerDialog();
            if(actualPlayer() instanceof AIPlayer)
                playAi();
        }while (actualPlayer() instanceof AIPlayer);
        controllerGame.enableAll();
    }

    /**
     * Metoda konczonca gre
     * @param winner
     */
    public void endMatch(Player winner)
    {
        int points = 0;
        for (Player player : state.players)
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
        System.out.println("Player: " + actualPlayer() + " wins!");
       //ScoreManager scoreManager = new ScoreManager("scoreboard.txt");
       //scoreManager.saveScore(players);
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
        ((game.myAssets.AI.AIPlayer)actualPlayer()).createMCTS();
        ACard card = ((game.myAssets.AI.AIPlayer)actualPlayer()).myTreeMonteCarlo.search();
        actualPlayer().getHand().remove(card);
        state.table.add(card);
        state.playersHandsSizes[state.actualPlayerIndex]--;
    }

    public ACard.Color chColorAction()
    {
        if(actualPlayer() instanceof AIPlayer)
            return ((AIPlayer) actualPlayer()).maxColorQuantity();
        else
            return controllerGame.chColorAlert();
    }
}
