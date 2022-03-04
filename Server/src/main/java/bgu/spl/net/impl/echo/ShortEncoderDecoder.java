package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;

public class ShortEncoderDecoder {
    public short bytesToShort(byte[] byteArr)

    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    //Encode short to 2 bytes

    public byte[] shortToBytes(short num)

    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}
