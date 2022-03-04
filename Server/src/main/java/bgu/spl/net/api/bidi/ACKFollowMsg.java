package bgu.spl.net.api.bidi;

public class ACKFollowMsg implements Message{

    private short opcode = 10;
    private short messageOpcode;
    private String otherUserName;

    public ACKFollowMsg(short messageOpcode, String otherUserName){
        this.otherUserName = otherUserName;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public short getOpCode() {
        return opcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] userNameBytes = otherUserName.getBytes();
        byte[] result = new byte[6 + userNameBytes.length];
        byte[] thisOpcode = shortToBytes(opcode);
        byte[] otherOpcode = shortToBytes(messageOpcode);
        result[0] = thisOpcode[0];
        result[1] = thisOpcode[1];
        result[2] = otherOpcode[0];
        result[3] = otherOpcode[1];
        for(int i = 0; i < userNameBytes.length; i++){
            result[4+i] = userNameBytes[i];
        }
        result[result.length-2] = 0;
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
