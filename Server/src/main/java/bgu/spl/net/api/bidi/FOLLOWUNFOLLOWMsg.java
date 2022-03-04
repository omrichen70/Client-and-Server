package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class FOLLOWUNFOLLOWMsg implements Message{

    private short opcode = 4;
    private int whatToDo;
    private String username;

    public FOLLOWUNFOLLOWMsg(byte[] bytes){
        whatToDo = bytes[0]-'0';
        username = new String(bytes, 2, bytes.length-2, StandardCharsets.UTF_8);
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
        protocol.follow(opcode, whatToDo, username);
    }
}
