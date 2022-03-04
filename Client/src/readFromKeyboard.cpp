//
// Created by Omri on 31/12/2021.
//

#include "../include/readFromKeyboard.h"


readFromKeyboard::readFromKeyboard(ConnectionHandler &c, int *shouldT): handler(c), shouldTerminate(shouldT) {

}



void readFromKeyboard::prsMessage(char *chars, string message) {
    //find first gap in the message
    if(message == "LOGOUT"){
        prsLogout(chars, message);
    } else if(message == "LOGSTAT"){
        prsLogstat(chars, message);
    } else {
        int index = message.find_first_of(' ');
        string command = message.substr(0, index);
        message = message.substr(index+1, message.length());
        if(command == "REGISTER"){
            prsReg(chars, message);
        } else if(command == "LOGIN"){
            prsLogin(chars, message);
        } else if(command == "FOLLOW"){
            prsFol(chars, message);
        } else if(command == "POST"){
            prsPost(chars, message);
        } else if(command == "PM"){
            prsPM(chars, message);
        }else if(command == "STAT"){
            prsStat(chars, message);
        }else if(command == "BLOCK"){
            prsBlock(chars, message);
        }
    }

}

void readFromKeyboard::prsReg(char *chars, string message) {
    shortToBytes(1, chars);
    for(int i = 0; i < message.length(); i++){
        if(message[i] == ' '){
            chars[2 + i] = '\0';
        } else {
            chars[2+i] = message[i];
        }
    }
    chars[2+message.length()] = '\0';
    chars[2+ message.length()+1] = ';';
}

void readFromKeyboard::prsLogin(char *chars, string message) {
    shortToBytes(2, chars);
    for(int i = 0; i < message.length(); i++){
        if(message[i] == ' '){
            chars[2 + i] = '\0';
        } else {
            chars[2+i] = message[i];
        }
    }
    chars[2+ message.length()] = ';';
}

void readFromKeyboard::prsLogout(char *chars, string message) {
    shortToBytes(3, chars);
    chars[2] = ';';
    *shouldTerminate = 0;
}

void readFromKeyboard::prsFol(char *chars, string message) {
    shortToBytes(4, chars);
    chars[2] = message[0];
    for(int i = 1; i < message.length(); i++){
        chars[2 + i] = message[i];
    }
    chars[2+ message.length()] = ';';
}

void readFromKeyboard::prsPost(char *chars, string message) {
    shortToBytes(5, chars);
    for(int i = 0; i < message.length(); i++){
        chars[2 + i] = message[i];
    }
    chars[2 + message.length()] = '\0';
    chars[2+ message.length()+1] = ';';
}

void readFromKeyboard::prsPM(char *chars, string message) {
    shortToBytes(6, chars);
    int firstZero = message.find_first_of(' ');
    for(int i = 0; i < message.length(); i++){
        chars[2 + i] = message[i];
    }

    chars[firstZero+2] = '\0';
    chars[2+ message.length()] = '\0';
    chars[2+ message.length()+1] = ';';
}

void readFromKeyboard::prsLogstat(char *chars, string message) {
    shortToBytes(7, chars);
    chars[2] = ';';
}

void readFromKeyboard::prsStat(char *chars, string message) {
    shortToBytes(8, chars);
    for(int i = 0; i < message.length(); i++){
        if(message[i] == ' '){
            chars[2+i] = '|';
        } else {
            chars[2+i] = message[i];
        }
    }
    chars[2 + message.length()] = '|';
    chars[3 + message.length()] = ';';
}

void readFromKeyboard::prsBlock(char *chars, string message) {
    shortToBytes(12, chars);
    for(int i = 0; i < message.length(); i++){
        chars[2 + i] = message[i];
    }
    chars[2 + message.length()] = '\0';
    chars[2 + message.length() + 1] = ';';
}

void readFromKeyboard::shortToBytes(short num, char *bytesArr) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

int readFromKeyboard::getMessageSize(string message) {
    if(message == "LOGOUT" || message == "LOGSTAT"){
        return 3;
    }
    int index = message.find_first_of(' ');
    string command = message.substr(0, index);
    string rest = message.substr(index+1);
    if(command == "REGISTER"){
       return (4 + rest.length());
    } else if(command == "LOGIN"){
        return (3 + rest.length());
    } else if(command == "FOLLOW"){
        return (3 + rest.length());
    } else if(command == "POST"){
        return (4 + rest.length());
    } else if(command == "PM"){
        return (4 + rest.length());
    }else if(command == "STAT"){
        return (4 + rest.length());
    }else if(command == "BLOCK"){
        return (4 + rest.length());
    }
}











