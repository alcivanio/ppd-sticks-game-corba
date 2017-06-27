package br.edu.ifce.initials;

import GameApp.*;          // The package containing our stubs
import br.edu.ifce.servants.GameCallbackServant;
import org.omg.CosNaming.*; // HelloClient will use the naming service.
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

/*

    Student: José Alcivanio Alves da Silva
    College: Instituto Federal do Ceará - Campus Fortaleza.

    Info:
    This code was inspired in the http://www.inf.ed.ac.uk/teaching/courses/ds/programs/CORBA/Ciao.html code for
    the discipline of paralel and distributed programming.

*/

public class GameClient
{
    static Game gameImpl;

    public static void main(String args[])
    {
        try {
            //kinda activating the POA, not big deal.
            ORB orb = ORB.init(args, null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();


            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "GAME";
            gameImpl    = GameHelper.narrow(ncRef.resolve_str(name));

            GameCallbackServant gameCallbackImpl = new GameCallbackServant();
            gameCallbackImpl.setORB(orb);
            gameCallbackImpl.gameImpl = gameImpl;

            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(gameCallbackImpl);
            GameCallback cref = GameCallbackHelper.narrow(ref);

            //String ciao = gameImpl.say(cref, "\n Buongiorno \n");
            gameImpl.connectNewPlayer(cref);
            //System.out.println(ciao);
            orb.run();

        } catch(Exception e){
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
}