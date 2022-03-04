package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class LOGINMsg implements Message{

    private short opcode = 2;
    private String username;
    private String password;
    private int captcha;

    public LOGINMsg(byte[] bytes){
        int[] zeros = findZeros(bytes);
        username = new String(bytes, 0, zeros[0], StandardCharsets.UTF_8);
        password = new String(bytes, zeros[0]+1, zeros[1]-zeros[0]-1, StandardCharsets.UTF_8);
        captcha = bytes[zeros[1]+1]-'0';
    }

    public int[] findZeros(byte[] bytes){
        int[] zeros = new int[2];
        int index = 0;
        for(int i = 0; i < bytes.length; i++){
            if(bytes[i] == 0 && index < zeros.length){
                zeros[index] = i;
                index++;
            }
        }
        return zeros;
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
        protocol.login(opcode, username, password, captcha);
    }
}
