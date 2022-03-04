package bgu.spl.net.api.bidi;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PMMsg implements Message{

    private short opcode = 6;
    private String username;
    private String content;
    private String dateAndTime;

    public PMMsg(byte[] bytes){
        int[] zeros = findZeros(bytes);
        username = new String(bytes, 0, zeros[0], StandardCharsets.UTF_8);
        content = new String(bytes, zeros[0]+1, zeros[1]-zeros[0]-1, StandardCharsets.UTF_8);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        dateAndTime = dtf.format(now);
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
        protocol.pm(opcode, username, content, dateAndTime);
    }
}
