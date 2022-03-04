/**/ HOW TO RUN THE CODE /**/

Server:
	1. mvn compile
	2. mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args="7777"

Client:
	1. make
	2. bin/main 127.0.0.1 7777
	             <host>   <port>

/**/ MESSAGE EXAMPLES /**/

Client to Server: Should be written in capital letters

1. REGISTER <username> <password> <birthday>
				  DD-MM-YYYY

2. LOGIN <username> <password> <captcha: 0/1>

3. LOGOUT <username>

4. FOLLOW <0 for follow / 1 for unfollow> <username>

5. POST <content> (each username tagged starts with '@')

6. PM <username> <content>

7. LOGSTAT

8. STAT <usernames seperated by space>

9. NOTIFICATION is created in the protocol only

12. BLOCK <username>

Server to Client:

10. ACK <message opcode> <optional content>

11. ERROR <message opcode>

/**/ FORBIDDEN WORDS /**/

Forbidden words list is sent as an argument on the creation of the database (in the main class)


