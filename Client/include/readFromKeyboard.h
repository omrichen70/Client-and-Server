//
// Created by Omri on 31/12/2021.
//

#ifndef ASS3_CLIENT_READFROMKEYBOARD_H
#define ASS3_CLIENT_READFROMKEYBOARD_H
using namespace std;
#include "connectionHandler.h"

class readFromKeyboard{
private:
    ConnectionHandler& handler;
    int* shouldTerminate;

    void prsReg(char* chars, string message);
    void prsLogin(char* chars, string message);
    void prsLogout(char* chars, string message);
    void prsFol(char* chars, string message);
    void prsPost(char* chars, string message);
    void prsPM(char* chars, string message);
    void prsStat(char* chars, string message);
    void prsLogstat(char* chars, string message);
    void prsBlock(char* chars, string message);



    void shortToBytes(short num, char* bytesArr);

public:
    readFromKeyboard(ConnectionHandler& c, int* shouldT);

    void prsMessage(char* chars, string message);

    int getMessageSize(string message);


};
#endif //ASS3_CLIENT_READFROMKEYBOARD_H
