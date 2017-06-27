package br.edu.ifce.servants;

import GameApp.Game;
import GameApp.GameCallbackPOA;
import org.omg.CORBA.ORB;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by alcivanio on 27/06/17.
 */



public class GameCallbackServant extends GameCallbackPOA
{
    public Game gameImpl;
    private ORB orb;

    public String name;
    public int currentBet;
    public int availableSticks = 3;
    public int sticksInHand;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void callback(String notification)
    {
        System.out.println(notification);
    }

    @Override
    public float askNumberSticksInHand() {
        Float convBet = null;
        System.out.print("\nNumber of sticks in your hand (max. value -> " + availableSticks + " ): ");
        Scanner s = new Scanner(System.in);

        while(convBet == null) {
            String strBet = s.nextLine();
            convBet = new Float(strBet);
            if (convBet.intValue() > availableSticks) { convBet = null; }
        }

        sticksInHand = (int) convBet.floatValue();
        return sticksInHand;
    }


    public float chooseABet(float maxValue) {
        Float convBet = null;
        System.out.print("\nGive us a bet (max. value -> " + (int)maxValue + " ): ");
        Scanner s = new Scanner(System.in);

        while(convBet == null) {
            String strBet = s.nextLine();
            convBet = new Float(strBet);
        }

        currentBet = (int) convBet.floatValue();
        return currentBet;
    }

    @Override
    public float getAvailableSticks() {
        return availableSticks;
    }

    @Override
    public float getSticksInHand() {
        return sticksInHand;
    }


    public boolean checkRoundResult(float numberSticks) {
        boolean isWinner = false;
        callback(numberSticks+ "");
        callback(currentBet + "");
        if (numberSticks == currentBet) {
            availableSticks--;

            if (availableSticks == 0) { isWinner = true; }/*if its more than 0, we show a message here.*/
            else { callback("YES!!! YOU'RE RIGHT :) NOW YOU HAVE " + availableSticks + " AVAILABLE STICKS"); }
        }
        else {callback("OPS, YOU'RE WORNG: YOUR ANSWER: " + currentBet + ". RIGHT ANSWER: " + numberSticks);}
        return isWinner;
    }




}
