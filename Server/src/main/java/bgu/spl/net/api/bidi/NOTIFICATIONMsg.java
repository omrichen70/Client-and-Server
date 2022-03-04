package bgu.spl.net.api.bidi;

public class NOTIFICATIONMsg implements Message{

    private short opcode = 9;
    private Integer type;
    private String postingUser;
    private String content;

    public NOTIFICATIONMsg(int type, String postingUser, String content){
        this.type = type;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public short getOpCode() {
        return opcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] thisOpcode = shortToBytes(opcode);
        byte[] posBytes = postingUser.getBytes();
        byte[] conBytes = content.getBytes();
        byte[] result = new byte[5 + posBytes.length + content.length()];

        result[0] = thisOpcode[0];
        result[1] = thisOpcode[1];
        result[2] = type.byteValue();

        for(int i = 0; i < posBytes.length; i++){
            result[3+i] = posBytes[i];
        }

        int index = 3 + posBytes.length;
        result[index] = ' ';
        index++;

        for(int i = 0; i < conBytes.length; i++){
            result[index+i] = conBytes[i];
        }
        result[result.length-1] = ';';
        return result;

    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    @Override
    public void act(BidiMessagingProtocolImpl protocol) {
    }
}
