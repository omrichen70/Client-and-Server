#include <stdlib.h>
#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/readFromKeyboard.h"
#include "../include/readFromServer.h"
#include <boost/thread.hpp>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    int* shouldTerminate = new int(1);
    readFromKeyboard readFromKeyboard(connectionHandler, shouldTerminate);
    readFromServer readFromServer(connectionHandler, shouldTerminate);

    boost::thread serverReader(&readFromServer::run, &readFromServer);

    //From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len=line.length();

        int size = readFromKeyboard.getMessageSize(line);
        char chars[size];
        readFromKeyboard.prsMessage(chars, line);

        if (!connectionHandler.sendBytes(chars, size)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        while(*shouldTerminate == 0){

        }

        if(*shouldTerminate == -1){
            break;
        }


    }

    serverReader.join();
    delete(shouldTerminate);
    return 0;
}
