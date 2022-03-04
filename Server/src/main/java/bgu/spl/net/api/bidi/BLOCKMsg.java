package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class BLOCKMsg implements Message{

    private short opcode = 12;
    private String username;

    public BLOCKMsg(byte[] bytes){
        username = new String(bytes, 0, bytes.length -1, StandardCharsets.UTF_8);
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
        protocol.block(opcode, username);
    }
}
