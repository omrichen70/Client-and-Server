package bgu.spl.net.api.bidi;

public interface Message {
    short getOpCode();
    byte[] getBytes();
    void act(BidiMessagingProtocolImpl protocol);
}
