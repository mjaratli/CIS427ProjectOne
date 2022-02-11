import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//I created a person class to hold useful information on the person currently logged in
class Person
{
private boolean loggedIN;
private String userID;
private String userPassword;
public Person(boolean loggedIN, String userID, String userPassword)
{
    this.loggedIN = loggedIN;
    this.userID = userID;
    this.userPassword = userPassword;
}
public Person()
{
    this(false, null, null);
}//end no-arg constructor
    public boolean getLoggedIN() {
        return loggedIN;
    }
    public void setLoggedIN(boolean loggedIN) {
        this.loggedIN = loggedIN;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}

//Server class
public class Server {

    private static final int SERVER_PORT = 3541;

    public static void main(String[] args) {
        createCommunicationLoop();
    }//end main

    //Method for calculating circle area and circumference
    public static String circle(int radius)
    {
    double area = Math.PI * radius * radius;
    double circumference = 2 * Math.PI * radius;
    return "Circle's circumference is " + String.format("%.2f",circumference) + " and area is " + String.format("%.2f",area);
    }

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
            Person person = new Person();
            while(true) {
                //Input from the client in strReceived
                String strReceived = inputFromClient.readUTF();
                //Input from the client put into an array with delimiter " " (space)
                String[] arrayOfStrReceived = strReceived.split(" ");

                //Checks if the command is login
                if(arrayOfStrReceived[0].equalsIgnoreCase("login"))
                {
                    //Checks if a username or a password is missing, or if the array is too large (so no array out of bounds exception is received)
                    if(arrayOfStrReceived.length != 3)
                    {
                        outputToClient.writeUTF("301 message format error");
                        continue;
                    }
                    //If both password and username were provided, we open the logins.txt file and create two strings to identify the current user
                    person.setUserID(arrayOfStrReceived[1]);
                    person.setUserPassword(arrayOfStrReceived[2]);
                    Scanner infile = null;
                    String input;
                    try {
                        File f = new File("logins.txt");
                        infile = new Scanner(f);
                        while(infile.hasNext()) {
                            input = infile.nextLine();
                            String[] arrayOfFile = input.split(" ");
                            //Traversing over the file to check if a correct username and password was provided
                            if(person.getUserID().equalsIgnoreCase(arrayOfFile[0]) && person.getUserPassword().equalsIgnoreCase(arrayOfFile[1]))
                            {
                                outputToClient.writeUTF("success");
                                person.setLoggedIN(true);
                            }
                        }
                        if(!person.getLoggedIN())
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

                //Checks if the command is solve
                else if(arrayOfStrReceived[0].equalsIgnoreCase("solve") && person.getLoggedIN())
                {
                    //If string is not length 3 or length 4
                    if(arrayOfStrReceived.length !=3 && arrayOfStrReceived.length !=4)
                    {
                        if(arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                        {
                            outputToClient.writeUTF("Error : No radius found");
                            continue;
                        }
                        if(arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-r"))
                        {
                            outputToClient.writeUTF("Error : No sides found");
                            continue;
                        }
                        else
                        {
                            outputToClient.writeUTF("301 message format error");
                        }
                        continue;
                    }
                    //If the string is a circle with more than one parameter
                    if(arrayOfStrReceived.length == 4 && arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                    {
                        outputToClient.writeUTF("301 message format error, you must only put a radius for the circle");
                        continue;
                    }

                    //Done checking possible errors/formatting issues
                    //Checks if we need to solve a circle
                    if(arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                    {
                        outputToClient.writeUTF(circle(Integer.parseInt(arrayOfStrReceived[2])));
                    }
                    //outputToClient.writeUTF("You are in solve");
                }

                //When there is an invalid command (no if statements are called)
                else
                {
                    outputToClient.writeUTF("300 invalid command");
                }
                //FOR LOGGING OUT USER LATER
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
