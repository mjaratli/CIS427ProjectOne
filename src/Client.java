import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Client {

    private static final int SERVER_PORT = 3541;

    public static void main(String[] args) {

        DataOutputStream toServer;
        DataInputStream fromServer;
        Scanner input =
                new Scanner(System.in);
        String message;

        //attempt to connect to the server
        try {
            Socket socket =
                    new Socket("localhost", SERVER_PORT);

            //create input stream to receive data
            //from the server
            fromServer =
                    new DataInputStream(socket.getInputStream());

            toServer =
                    new DataOutputStream(socket.getOutputStream());


            while(true) {
                System.out.print("C: ");
                //message that will be sent to the server from the command line:
                message = input.nextLine();
                toServer.writeUTF(message);
                if(message.equalsIgnoreCase("shutdown")) {
                    break;
                }
                //received message from the server:
                message = fromServer.readUTF();
                System.out.println("S: " + message);
            }

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch


    }//end main
}