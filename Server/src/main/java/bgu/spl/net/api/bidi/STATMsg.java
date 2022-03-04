package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class STATMsg implements Message{

    private short opcode = 8;
    private String[] userNames;

    public STATMsg(byte[] bytes){
        String s = new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
        userNames = s.split("\\|");
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
        protocol.stat(opcode, userNames);
    }
}
