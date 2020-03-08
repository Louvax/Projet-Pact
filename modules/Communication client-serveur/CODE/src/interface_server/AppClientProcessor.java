package interface_server;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Main.*;

public class AppClientProcessor implements Runnable{

   private Socket sock;
   private PrintWriter writer = null;
   private BufferedInputStream reader = null;
   private int sessionID;
   private Activite act;
   
   public AppClientProcessor(Socket pSock){
      sock = pSock;
   }
   
   @Override
   public void run(){
      System.err.println("Lancement du traitement de la connexion appli");

      boolean closeConnexion = false;
      while(!sock.isClosed()){
         
         try {
            
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedInputStream(sock.getInputStream());
            String response = read();
            InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();
            
            String debug = "";
            debug = "Thread : " + Thread.currentThread().getName() + ". ";
            debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
            debug += " Sur le port : " + remote.getPort() + ".\n";
            debug += "\t -> Commande reçue : " + response + "\n";
            System.err.println("\n" + debug);
            
            switch(response){
               case "initSession":
            	   writer.write("send");
            	   writer.flush();
            	   System.out.println("asked app to send init info");
            	   String stringData = read();
            	   System.out.println("info received, decoding ...");
            	   Thread.sleep(2000);
            	   JSONObject data = decode(stringData);
            	   long nbtemp = (long)data.get("numberOfStudents");
            	   int nb = (int)nbtemp;
            	   String[] noms = new String[nb];
            	   String[] prenoms = new String[nb];
            	   int[] braceletsID = new int[nb];
            	   JSONObject lastNames = (JSONObject)data.get("lastNames");
            	   JSONObject firstNames = (JSONObject)data.get("firstNames");
            	   JSONObject IDs = (JSONObject)data.get("IDs");
            	   for (int i =0; i<nb; i++) {
            		   String strI = Integer.toString(i);
            		   noms[i] = (String)lastNames.get(strI);
            		   prenoms[i] = (String)firstNames.get(strI);
            		   long temp = (long)IDs.get(strI);
            		   braceletsID[i] = (int) temp;
            	   }
            	   System.out.println("decoded !");
            	   System.out.println(noms[0]);
            	   Thread.sleep(4000);
            	   System.out.println("creating activity");
            	   Activite act = new Activite(noms, prenoms, braceletsID, this);
            	   System.out.println("activity created");
            	   this.act = act;
            	   this.sessionID = act.getSession().getSessionID();
            	   writer.write(Integer.toString(sessionID));
            	   writer.flush();
            	   this.act.start();
               	   break;
               case "getStats":
            	   Main.getStats(sessionID);
               	   break;
               case "close":
            	   writer.write("Communication terminée");
            	   writer.flush();
            	   this.act.stop();
            	   closeConnexion = true;
            	   break;
               default : 
            	   writer.write("Commande inconnue !");
            	   writer.flush();
            	   break;
            }
            
            if(closeConnexion){
               System.err.println("COMMANDE CLOSE DETECTEE ! ");
               writer = null;
               reader = null;
               sock.close();
               break;
            }
         }catch(SocketException e){
            System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
            break;
         } catch (IOException e) {
            e.printStackTrace();
         } catch (InterruptedException e) {
			e.printStackTrace();
		}         
      }
   }
   
   private String read() throws IOException{      
      String response = "";
      int stream;
      byte[] b = new byte[4096];
      stream = reader.read(b);
      response = new String(b, 0, stream);
      return response;
   }
   
   private JSONObject decode(String input){
	   JSONParser parser;
	   JSONObject json = null;
	try {
		parser = new JSONParser();
		json = (JSONObject) parser.parse(input);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	   return json;
   }
}