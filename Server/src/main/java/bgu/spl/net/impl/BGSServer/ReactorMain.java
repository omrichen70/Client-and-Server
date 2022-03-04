package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.DataBase;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Server;

import java.util.ArrayList;
import java.util.Arrays;

public class ReactorMain {
    public static void main(String args[]) {
        ArrayList<String> forbiddenWords = new ArrayList<String>(Arrays.asList("Kaki","Pipi"));
        DataBase data = new DataBase(forbiddenWords);
        int port = Integer.parseInt(args[0]);
        int threadsNumber = Integer.parseInt(args[1]);
        Server s1 = Server.reactor(threadsNumber, port, () -> new BidiMessagingProtocolImpl(data), () -> new MessageEncoderDecoderImpl<Message>());
        s1.serve();
    }
}
