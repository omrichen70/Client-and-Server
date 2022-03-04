package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;

public class REGISTERMsg implements Message{

    private short opcode = 1;
    String username;
    String password;
    String birthday;

    public REGISTERMsg(byte[] bytes){
        int[] zeros = findZeros(bytes);
        username = new String(bytes, 0, zeros[0], StandardCharsets.UTF_8);
        password = new String(bytes, zeros[0]+1, zeros[1]-zeros[0]-1, StandardCharsets.UTF_8);
        birthday = new String(bytes, zeros[1]+1, zeros[2]-zeros[1]-1, StandardCharsets.UTF_8);
    }

    public int[] findZeros(byte[] bytes){
        int[] zeros = new int[3];
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
        protocol.register(opcode, username, password, birthday);
    }


}
