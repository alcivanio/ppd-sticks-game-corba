package br.edu.ifce.servants;

import GameApp.GameCallback;
import GameApp.GamePOA;
import org.omg.CORBA.ORB;

import java.util.ArrayList;

/**
 * Created by alcivanio on 27/06/17.
 */

public class GameServant extends GamePOA
{
    //DEFINITIONS
    final int NUMBER_PLAYERS = 2;

    //FLAGS
    boolean gameStarted = false;
    int winnerPosition = 0;

    //VARIABLES
    ArrayList<GameCallback> listPlayers = new ArrayList<GameCallback>();
    ArrayList<Integer> listBets = new ArrayList<>();
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void connectNewPlayer(GameCallback callbackObj) {
        if (!gameStarted) {
            listPlayers.add(callbackObj);
            tryStartGame();//try to start a menu
        }
    }






    private void teste() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    //callback.callback("Something");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).run();

    }


    public GameServant() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                showMenu();

            }
        });

        t.run();
    }


    public void showMenu() {
        callWaitingPlayers();
    }

    public void callWaitingPlayers() {
        System.out.println("THE SERVER IS WORKING. WAITING FOR PLAYERS GET IN...");
    }


    public void messageToEverybody(String message) {
        for(GameCallback clb : listPlayers) {
            clb.callback(message);
        }
    }


    public void tryStartGame() {
        if (!(listPlayers.size() == NUMBER_PLAYERS)) { return; }
        gameStarted         = true;
        boolean gameGoingOn = true;
        while (gameGoingOn) {
            System.out.println("ASKING PLAYERS STICKS IN HAND...");
            messageToEverybody("ASKING PLAYERS STICKS IN HAND...");
            askPlayersSticksInHand();
            System.out.println("ASKING PLAYERS BETS...");
            messageToEverybody("ASKING PLAYERS BETS...");
            askPlayersBets();
            checkForPlayerWinning();
            reordenateArray();
            gameGoingOn = checkForGameContinuing();
        }

        System.out.println("END OF THE GAME :)");

    }


    public void askPlayersSticksInHand() {
        for(GameCallback clb : listPlayers) {
            clb.askNumberSticksInHand();
        }
    }


    public void askPlayersBets() {
        listBets.clear();//restart the list of bets, just for a checking cause :)
        int maxSticks = getMaxSticks();
        for(GameCallback clb : listPlayers) {
            boolean isRepeated = true;

            while (isRepeated) {
                Integer newBet = (int) clb.chooseABet(maxSticks); //ASK FOR PLAYER BET

                isRepeated = false;
                for (Integer el : listBets) { //just checking if its repeated
                    if (el.intValue() == newBet.intValue()){
                        isRepeated = true;
                    }
                }

                if (isRepeated){
                    clb.callback("OPS, SOMEONE ALREADY SAID IT :( TELL US ANOTHER BET!");
                }

                listBets.add(newBet);
            }
        }
    }


    public void checkForPlayerWinning() {
        int roundResult     = getRoundResult();
        GameCallback winner = null;

        for (GameCallback clb : listPlayers) {
            if( clb.checkRoundResult(roundResult) ) {
                winner = clb;
            }
        }

        if (winner != null) {//if there is a winner, remove it.
            winnerPosition++;
            winner.callback("YOU'RE THE " + winnerPosition + " WINNER, CONGRATS.");
            listPlayers.remove(winner);
        }
    }

    public void reordenateArray() {
        GameCallback clb = listPlayers.remove(0);
        listPlayers.add(clb);
    }


    public boolean checkForGameContinuing() {
        if (listPlayers.size() == 1) {
            GameCallback clb = listPlayers.get(0);
            clb.callback("OH, BAD NEWS: YOU'RE THE BIG *LOSER*!");
            listPlayers.clear();
            return false;
        }

        return true;
    }


    //iterate in each player and ask its number of sticks in hand.
    public int getRoundResult() {
        int sticks = 0;
        for (GameCallback clb : listPlayers) {
            sticks += clb.getSticksInHand();
        }

        return sticks;
    }


    public int getMaxSticks() {
        int sticks = 0;
        for (GameCallback clb : listPlayers) {
            sticks += clb.getAvailableSticks();
        }

        return sticks;
    }













    public String say(GameCallback callobj, String msg)
    {
        //callback = callobj;
        //teste();
        return "\n Ciao Mondo!! \n";
    }


}
