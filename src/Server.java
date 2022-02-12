import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
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

    //Method for calculating square perimeter and area
    public static String square(int side)
    {
        double area = Math.pow(side, 2);
        double perimeter = 4 * side;
        return "Rectangle's perimeter is " + String.format("%.2f",perimeter) + " and area is " + String.format("%.2f",area);
    }

    //Method for calculating rectangle perimeter and area
    public static String rectangle(int length, int width)
    {
        double area = length * width;
        double perimeter = 2 * (length + width);
        return "Rectangle's perimeter is " + String.format("%.2f",perimeter) + " and area is " + String.format("%.2f",area);
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
                    //If a different user tries to login, we tell the user they need to logout first
                    if((person.getUserID() != null && (!person.getUserID().equalsIgnoreCase(arrayOfStrReceived[1])) || (person.getUserPassword() != null && (!person.getUserPassword().equalsIgnoreCase(arrayOfStrReceived[2])))))
                    {
                        outputToClient.writeUTF("Failure, you need to logout before trying to sign in with a different user");
                        continue;
                    }
                    Scanner infile = null;
                    String input;
                    try {
                        File f = new File("logins.txt");
                        infile = new Scanner(f);
                        while(infile.hasNext()) {
                            input = infile.nextLine();
                            String[] arrayOfFile = input.split(" ");
                            //Traversing over the file to check if a correct username and password was provided
                            if(arrayOfStrReceived[1].equalsIgnoreCase(arrayOfFile[0]) && arrayOfStrReceived[2].equalsIgnoreCase(arrayOfFile[1]))
                            {
                                //
                                person.setUserID(arrayOfStrReceived[1]);
                                person.setUserPassword(arrayOfStrReceived[2]);
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
                    PrintWriter pw = null;
                    try
                    {
                        //Checks if the file already exists or not
                        boolean fileExists = new File(person.getUserID() + "_solutions.txt").exists();
                        //If the file doesn't exist we make a new one
                        if (!fileExists)
                        {
                            File file = new File(person.getUserID() + "_solutions.txt");
                        }
                        FileWriter filewriter = new FileWriter(person.getUserID() + "_solutions.txt", true);
                        pw = new PrintWriter(filewriter);

                    //If string is not length 3 or length 4
                    if (arrayOfStrReceived.length != 3 && arrayOfStrReceived.length != 4)
                    {
                        if (arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                        {
                            outputToClient.writeUTF("Error : No radius found");
                            pw.println("Error : No radius found");
                            continue;
                        }
                        if (arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-r"))
                        {
                            outputToClient.writeUTF("Error : No sides found");
                            pw.println("Error : No sides found");
                            continue;
                        }
                        else
                        {
                            outputToClient.writeUTF("301 message format error");
                        }
                        continue;
                    }
                    //If the string is a circle with more than one parameter
                    if (arrayOfStrReceived.length == 4 && arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                    {
                        outputToClient.writeUTF("301 message format error, you must only put a radius for the circle");
                        continue;
                    }

                    //Done checking possible errors/formatting issues
                    //Checks if we need to solve a circle
                    if (arrayOfStrReceived[1].equalsIgnoreCase("-c"))
                    {
                        outputToClient.writeUTF(circle(Integer.parseInt(arrayOfStrReceived[2])));
                        pw.println("Radius " + arrayOfStrReceived[2] + ": " + circle(Integer.parseInt(arrayOfStrReceived[2])));
                    }
                    //Checks if we need to solve a rectangle (square)
                    if (arrayOfStrReceived[1].equalsIgnoreCase("-r") && arrayOfStrReceived.length == 3)
                    {
                        outputToClient.writeUTF(square(Integer.parseInt(arrayOfStrReceived[2])));
                        pw.println("side " + arrayOfStrReceived[2] + ": " + square(Integer.parseInt(arrayOfStrReceived[2])));
                    }
                    //Checks if we need to solve a rectangle
                    if (arrayOfStrReceived[1].equalsIgnoreCase("-r") && arrayOfStrReceived.length == 4)
                    {
                        outputToClient.writeUTF(rectangle(Integer.parseInt(arrayOfStrReceived[2]), Integer.parseInt(arrayOfStrReceived[3])));
                        pw.println("sides " + arrayOfStrReceived[2] + " " + arrayOfStrReceived[3] + " : " + rectangle(Integer.parseInt(arrayOfStrReceived[2]), Integer.parseInt(arrayOfStrReceived[3])));
                    }
                    }

                     catch(FileNotFoundException ex)
                     {
                    System.out.println("Oops, can't open the file.");
                    }
                    finally {
                    if(pw != null) {
                        pw.close();
                    }
                }
                }

                //Checks if the command is list and a proper user is logged in
                else if(arrayOfStrReceived[0].equalsIgnoreCase("list") && person.getLoggedIN())
                {
                    Scanner infile = null;
                    String input = "";
                    try
                    {
                        //If the user only wants their own list
                        if (arrayOfStrReceived.length == 1)
                        {
                            boolean fileExists = new File(person.getUserID() + "_solutions.txt").exists();
                            input += System.lineSeparator() + person.getUserID() + System.lineSeparator();
                            if(!fileExists)
                            {
                                input += "No interactions yet" + System.lineSeparator();
                            }
                            if(fileExists)
                            {
                                File f = new File(person.getUserID() + "_solutions.txt");
                                infile = new Scanner(f);
                                input = person.getUserID() + System.lineSeparator();
                                while (infile.hasNext())
                                {
                                    input += infile.nextLine() + System.lineSeparator();
                                }
                            }
                            outputToClient.writeUTF(input);
                        }

                        //If the user is the root and wants the list for all users
                        else if(arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-all") && person.getUserID().equalsIgnoreCase("root"))
                        {
                            String[] usernameArray = new String[] {"root", "john", "sally", "qiang"};
                            for(int i = 0; i < 4; i++)
                            {
                                //Checks if the file already exists or not
                                boolean fileExists = new File(usernameArray[i] + "_solutions.txt").exists();
                                input += usernameArray[i] + System.lineSeparator();
                                if(!fileExists)
                                {
                                    input += "No interactions yet" + System.lineSeparator();
                                    continue;
                                }
                                File f = new File(usernameArray[i] + "_solutions.txt");
                                infile = new Scanner(f);
                                while (infile.hasNext())
                                {
                                    input += infile.nextLine() + System.lineSeparator();
                                }
                            }
                            outputToClient.writeUTF(input);
                        }
                        else if(arrayOfStrReceived.length == 2 && arrayOfStrReceived[1].equalsIgnoreCase("-all") && !person.getUserID().equalsIgnoreCase("root"))
                        {
                            outputToClient.writeUTF("Error: you are not the root user");
                        }
                        else
                        {
                            outputToClient.writeUTF("301 message format error");
                        }

                    }
                    catch(FileNotFoundException ex){
                        System.out.println("Ooops can't find the file.");
                    }
                    finally{
                        if (infile != null)
                        {
                            infile.close();
                        }
                    }
                }

                //Checks if the command is shutdown
                else if(arrayOfStrReceived[0].equalsIgnoreCase("shutdown") && person.getLoggedIN())
                {
                    if(arrayOfStrReceived.length != 1)
                    {
                        outputToClient.writeUTF("301 message format error");
                    }
                }

                //When there is an invalid command (no if statements are called)
                else if(person.getLoggedIN())
                {
                    outputToClient.writeUTF("300 invalid command");
                }
                else if(!(person.getLoggedIN()))
                {
                    outputToClient.writeUTF("300 invalid command because you are not logged in or not a valid user");
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
