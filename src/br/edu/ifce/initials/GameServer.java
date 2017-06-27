package br.edu.ifce.initials;

import GameApp.*;          // The package containing our stubs.
import br.edu.ifce.servants.*;
import org.omg.CosNaming.*; // HelloServer will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions. 
import org.omg.CORBA.*;     // All CORBA applications need these classes. 
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;


public class GameServer
{
    public static void main(String args[])
    {
        try {
            //just initializing the manager...
            ORB orb = ORB.init(args, null);
            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            //getting a servant reference.
            GameServant ciaoImpl = new GameServant();
            ciaoImpl.setORB(orb);

            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(ciaoImpl);
            Game cref = GameHelper.narrow(ref);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "GAME";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, cref);

            orb.run();
        }

        catch(Exception e) {
            System.err.println("ERROR : " + e);
            e.printStackTrace(System.out);
        }
    }
}