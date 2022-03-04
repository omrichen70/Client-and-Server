CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: link

link: compile
	g++ -o bin/main bin/echoClient.o bin/connectionHandler.o bin/readFromKeyboard.o bin/readFromServer.o $(LDFLAGS)

compile: src/echoClient.cpp src/connectionHandler.cpp src/readFromKeyboard.cpp src/readFromServer.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/readFromKeyboard.o src/readFromKeyboard.cpp
	g++ $(CFLAGS) -o bin/readFromServer.o src/readFromServer.cpp

clean:
	rm -f bin/*
