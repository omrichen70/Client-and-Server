//
// Created by Omri on 31/12/2021.
//

#include "../include/readFromServer.h"
using namespace std;

readFromServer::readFromServer(ConnectionHandler& c, int* shouldT): handler(c), shouldTerminate(shouldT) {

}

void readFromServer::run() {
    while(1){
        string message;
        if (!handler.getLine(message)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        prsMessage(message);
        if(*shouldTerminate == -1){
            break;
        }
    }
}

void readFromServer::prsMessage(string msg) {
    char opcodeArr[2]; //ACK/ERROR/NOTIFICATION
    opcodeArr[0] = msg[0];
    opcodeArr[1] = msg[1];
    short opcode = bytesToShort(opcodeArr);

    char otherOpcodeArr[2]; //MESSAGE OPCODE
    otherOpcodeArr[0] = msg[2];
    otherOpcodeArr[1] = msg[3];
    short otherOpcode = bytesToShort(otherOpcodeArr);

    //ERROR
    if(opcode == 11){
        if(otherOpcode == 3){ //LOGOUT failed
            *shouldTerminate = 1;
            cout << "ERROR " + to_string(otherOpcode) << endl;
        } else { //ANY OTHER ERROR
            cout << "ERROR " + to_string(otherOpcode) << endl;
        }
    }

    //ACK
    if(opcode == 10){
        if(otherOpcode == 3){ //LOGOUT
            cout << "ACK " + to_string(otherOpcode) << endl;
            *shouldTerminate = -1;
        } else if(otherOpcode == 4){ //FOLLOW
            string name = msg.substr(4, msg.length()-6);
            cout << "ACK " + to_string(otherOpcode) + " " + name<<endl;
        } else if(otherOpcode == 7 || otherOpcode == 8){ //LOGSTAT or STAT
            for(int i = 0; i < msg.length() && i+12 < msg.length(); i=i+12){

                string age = msg.substr(4+i, 2);
                char ageArr[] = {age[0], age[1]};
                short ageS = bytesToShort(ageArr);

                string numOfPosts = msg.substr(6+i, 2);
                char postArr[] = {numOfPosts[0], numOfPosts[1]};
                short postS = bytesToShort(postArr);

                string numFollowers = msg.substr(8+i, 2);
                char followingArr[] = {numFollowers[0], numFollowers[1]};
                short followingS = bytesToShort(followingArr);

                string numFollowing = msg.substr(10+i, 2);
                char followersArr[] = {numFollowing[0], numFollowing[1]};
                short followerS = bytesToShort(followersArr);

                cout << "ACK " + to_string(otherOpcode) + " " + to_string(ageS) + " " + to_string(postS) + " " +
                        to_string(followerS) + " " + to_string(followingS)<<endl;
            }
        } else{ //regular ACK
            cout << "ACK " + to_string(otherOpcode) << endl;
        }
    }

    if(opcode == 9){ //NOTIFICATION

        if(1 == static_cast<int>(msg[2])){
            cout << "NOTIFICATION PUBLIC " + msg.substr(3, msg.length()-4) << endl;
        } else {
            cout << "NOTIFICATION PM " + msg.substr(3, msg.length()-4) << endl;
        }
    }
}

short readFromServer::bytesToShort(char* bytesArr) {
        short result = (short)((bytesArr[0] & 0xff) << 8);
        result += (short)(bytesArr[1] & 0xff);
        return result;
}




