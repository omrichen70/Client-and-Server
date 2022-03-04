//
// Created by Omri on 31/12/2021.
//

#ifndef ASS3_CLIENT_READFROMSERVER_H
#define ASS3_CLIENT_READFROMSERVER_H

#include "connectionHandler.h"

class readFromServer{
private:
    ConnectionHandler& handler;
    int* shouldTerminate;

public:
    readFromServer(ConnectionHandler& c, int* shouldT);
    void prsMessage(std::string msg);
    void run();
    short bytesToShort(char* bytesArr);
};
#endif //ASS3_CLIENT_READFROMSERVER_H
