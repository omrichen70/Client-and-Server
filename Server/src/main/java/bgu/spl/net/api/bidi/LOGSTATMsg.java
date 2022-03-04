package bgu.spl.net.api.bidi;

public class LOGSTATMsg implements Message{

    private short opcode = 7;

    public LOGSTATMsg(){

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
        protocol.logStat(opcode);
    }
}
