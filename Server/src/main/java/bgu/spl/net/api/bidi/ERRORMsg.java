package bgu.spl.net.api.bidi;

public class ERRORMsg implements Message{

    private short opcode = 11;
    private short messageOpcode;

    public ERRORMsg(short messageOpcode){
        this.messageOpcode = messageOpcode;
    }
    @Override
    public short getOpCode() {
        return opcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] result = new byte[5];
        byte[] thisOpcode = shortToBytes(opcode);
        byte[] otherOpcode = shortToBytes(messageOpcode);
        result[0] = thisOpcode[0];
        result[1] = thisOpcode[1];
        result[2] = otherOpcode[0];
        result[3] = otherOpcode[1];
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
