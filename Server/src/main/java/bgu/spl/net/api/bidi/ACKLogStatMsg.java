package bgu.spl.net.api.bidi;

import java.util.ArrayList;

public class ACKLogStatMsg implements Message{

    private short opcode = 10;
    private short messageOpcode;
    private ArrayList<short[]> stats;

    public ACKLogStatMsg(short messageOpcode, ArrayList<short[]> stats){
        this.stats = stats;
        this.messageOpcode = messageOpcode;
    }

    @Override
    public short getOpCode() {
        return opcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] result = new byte[(12*stats.size())+1];
        byte[] thisOpcode = shortToBytes(opcode);
        byte[] otherOpcode = shortToBytes(messageOpcode);
        for (int i = 0, j = 0; j < stats.size(); i=i+12, j++){
            result[i] = thisOpcode[0];
            result[i+1] = thisOpcode[1];
            result[i+2] = otherOpcode[0];
            result[i+3] = otherOpcode[1];

            short[] userStats = stats.get(j);
            byte[] age = shortToBytes(userStats[0]);
            result[i+4] = age[0];
            result[i+5] = age[1];

            byte[] numPosts = shortToBytes(userStats[1]);
            result[i+6] = numPosts[0];
            result[i+7] = numPosts[1];

            byte[] numFollowers = shortToBytes(userStats[2]);
            result[i+8] = numFollowers[0];
            result[i+9] = numFollowers[1];

            byte[] numFollowing = shortToBytes(userStats[3]);
            result[i+10] = numFollowing[0];
            result[i+11] = numFollowing[1];
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
