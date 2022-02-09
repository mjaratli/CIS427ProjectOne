import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Server {

    private static final int SERVER_PORT = 3541;

    public static void main(String[] args) {
        createCommunicationLoop();
    }//end main

    public static void createCommunicationLoop() {
        try {
            //create server socket
            ServerSocket serverSocket =
                    new ServerSocket(SERVER_PORT);

            System.out.println("Server started at " +
                    new Date() + "\n");
            //listen for a connection
            //using a regular *client* socket
            Socket socket = serverSocket.accept();

            //now, prepare to send and receive data
            //on output streams
            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream outputToClient =
                    new DataOutputStream(socket.getOutputStream());

            //server loop listening for the client
            //and responding
            while(true) {
                String strReceived = inputFromClient.readUTF();
                String[] arrayOfStrReceived = strReceived.split(" ");


                outputToClient.writeUTF("Failure: Please provide both a username and a password. Try again.");

                if(arrayOfStrReceived[0].equalsIgnoreCase("login"))
                {
                    //Opening the logins.txt file and creating two strings to identify the current user
                    //NOTE MAKE TRY CATCH FOR OUT OF BOUNDS
                    String userID = arrayOfStrReceived[1];
                    String userPassword = arrayOfStrReceived[2];
                    Scanner infile = null;
                    String input;
                    boolean loginSuccessful = false;
                    try {
                        File f = new File("logins.txt");
                        infile = new Scanner(f);
                        while(infile.hasNext()) {
                            input = infile.nextLine();
                            String[] arrayOfFile = input.split(" ");
                            if((userID.compareTo(arrayOfFile[0]) == 0) && (userPassword.compareTo(arrayOfFile[1]) == 0))
                            {
                                outputToClient.writeUTF("success");
                                loginSuccessful = true;
                            }
                        }
                        if(loginSuccessful == false)
                        {
                            outputToClient.writeUTF("Failure: Please provide correct username and password. Try again.");
                        }
                    }
                    catch(FileNotFoundException ex) {
                        System.out.println("Ooops can't find the file.");
                    }
                    finally {
                        if(infile != null) {
                            infile.close();
                        }
                    }
                }
                /* else if(strReceived.equalsIgnoreCase("quit")) {
                    System.out.println("Shutting down server...");
                    outputToClient.writeUTF("Shutting down server...");
                    serverSocket.close();
                    socket.close();
                    break;  //get out of loop
                }
                else {
                    System.out.println("Unknown command received: "
                            + strReceived);
                    outputToClient.writeUTF("Unknown command.  "
                            + "Please try again.");

                }*/
            }//end server loop
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
    }//end createCommunicationLoop
}
