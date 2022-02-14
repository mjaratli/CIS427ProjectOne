# CIS427 Project One
https://github.com/mjaratli/CIS427ProjectOne

###How to build and run the program
First, open the project in an IDE. I used IntelliJ IDEA Edu.
To run the program, first build the 
server class and then build the client class.
This is because the server listens for 
a connection, not the client. After doing that, you 
can start using the commands : login, solve, 
list, logout, and shutdown.

###The five commands
1. Login
- Allows the user to login
- The user must log in with a proper password to use 
the commands solve, list, and logout
2. Solve
- Use the solve command to get information on a circle, square, or rectangle
- Format 1: solve -c #
- Format 2: solve -r #
- Format 3: solve -r # #
3. List
- List gives you all of the results of the solve commands that you have done
- If you are the root user, list -all gives you all users results from 
previous solve command calls
4. Logout
- Logout to login with a different user, there is no need to reconnect
5. Shutdown
- Shutdown terminates the programs and closes all sockets

##Problems or bugs
There are no known problems or bugs in the program.

##Sample output

###Run #1
- C: login
- S: 301 message format error
- C: login jake jake22
- S: Failure: Please provide correct username and password. Try again.
- C: login john john22
- S: success
- C: solve -c a b c
- S: 301 message format error
- C: solve -c a
- S: 301 message format error, only input numbers
- C: solve -c
- S: Error : No radius found
- C: solve -r
- S: Error : No sides found
- C: solve -c 4
- S: Circle's circumference is 25.13 and area is 50.27
- C: solve -r 2
- S: Rectangle's perimeter is 8.00 and area is 4.00
- C: solve -r 2 6
- S: Rectangle's perimeter is 16.00 and area is 12.00
- C: list
- S: john
- Error : No radius found
- Error : No sides found
- Radius 4: Circle's circumference is 25.13 and area is 50.27
- side 2: Rectangle's perimeter is 8.00 and area is 4.00
- sides 2 6 : Rectangle's perimeter is 16.00 and area is 12.00

- C: list -all
- S: Error: you are not the root user
- C: logout
- S: 200 OK
- C: login root root22
- S: success
- C: solve -r 4 8
- S: Rectangle's perimeter is 24.00 and area is 32.00
- C: solve -r 1
- S: Rectangle's perimeter is 4.00 and area is 1.00
- C: solve -c 9
- S: Circle's circumference is 56.55 and area is 254.47
- C: list
- S: root
- sides 4 8 : Rectangle's perimeter is 24.00 and area is 32.00
- side 1: Rectangle's perimeter is 4.00 and area is 1.00
- Radius 9: Circle's circumference is 56.55 and area is 254.47

- C: list -all
- S: root
- sides 4 8 : Rectangle's perimeter is 24.00 and area is 32.00
- side 1: Rectangle's perimeter is 4.00 and area is 1.00
- Radius 9: Circle's circumference is 56.55 and area is 254.47
- john
- Error : No radius found
- Error : No sides found
- Radius 4: Circle's circumference is 25.13 and area is 50.27
- side 2: Rectangle's perimeter is 8.00 and area is 4.00
- sides 2 6 : Rectangle's perimeter is 16.00 and area is 12.00
- sally
- No interactions yet
- qiang
- No interactions yet

- C: logout
- S: 200 OK
- C: shutdown
- S: 200 OK

- Process finished with exit code 0

###Run #2
- C: solve
- S: 300 invalid command because you are not logged in or not a valid user
- C: logout
- S: 300 invalid command because you are not logged in or not a valid user
- C: list
- S: 300 invalid command because you are not logged in or not a valid user
- C: shutdown
- S: 200 OK

- Process finished with exit code 0
