import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

   //Creazione di una classe per il Multrithreading
   class ServerThread extends Thread {
     private Socket socket;
     public ServerThread (Socket socket) {
       this.socket = socket;
     }
     //esecuzione del Thread sul Socket
     public void run() {
       try {
    	 BufferedReader ibr= new BufferedReader(new InputStreamReader(socket.getInputStream()));
         DataOutputStream os = new DataOutputStream(socket.getOutputStream());
         
         String userInput;
         
         userInput=ibr.readLine();
       
         System.out.println("Il Client ha scritto: " + userInput);
       
         if (!userInput.isEmpty()) {
        	 System.out.println("UserInput is: "+userInput);
        	 if ( userInput.equalsIgnoreCase("Date")) {
        		 LocalDate ld = LocalDate.now();
        		 os.writeBytes(ld.toString());
        	 } else if ( userInput.equalsIgnoreCase("Time")) {
        		 LocalTime lt = LocalTime.now();
        		 os.writeBytes(lt.toString());
        	 } else os.writeBytes("BAD");
         }
         os.close();
         ibr.close();
         socket.close();
       }
       catch (IOException e) {
         System.out.println("IOException: " + e);
       }
     }
   }
   //Classe Server per attivare la Socket
   public class TCPParallelServer {
     public void start() throws Exception {
    	 try(ServerSocket serverSocket = new ServerSocket(7777)) {
	       //Ciclo infinito di ascolto dei Client
	       while(true) {
	         System.out.println("In attesa di chiamate dai Client... ");
	         Socket socket = serverSocket.accept();
	         System.out.println("Ho ricevuto una chiamata di apertura da:\n" + socket);
	         ServerThread serverThread = new ServerThread(socket);
	         serverThread.start();
	       }
    	 } catch (Exception e) {
    		 throw e;
    	 }
     }
     
     public static void main (String[] args) throws Exception {
       TCPParallelServer tcpServer = new TCPParallelServer();
       tcpServer.start();
     }
   }