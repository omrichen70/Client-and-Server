package bgu.spl.net.api.bidi;

public class LOGOUTMsg implements Message{

    private short opcode = 3;

    public LOGOUTMsg(){

    }

    @Override
    public short getOpCode() {
        return opcode;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public void act(BidiMessagingProtocolImpl protocol) {
        protocol.logout(opcode);
    }
}
