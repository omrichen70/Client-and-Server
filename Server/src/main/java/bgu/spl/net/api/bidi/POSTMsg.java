package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class POSTMsg implements Message{

    private short opcode = 5;
    private String content;

    public POSTMsg(byte[] bytes){
        content = new String(bytes, 0, bytes.length-1, StandardCharsets.UTF_8);
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
        protocol.post(opcode, content);
    }
}
