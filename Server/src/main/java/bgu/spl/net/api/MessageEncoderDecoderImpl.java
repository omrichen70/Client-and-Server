package bgu.spl.net.api;

import bgu.spl.net.api.bidi.*;

import java.util.Arrays;

public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1024];
    private int length = 0;

    public MessageEncoderDecoderImpl(){}

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(nextByte == ';'){
            return getMessage();
        }
        addByte(nextByte);
        return null;
    }

    private void addByte(byte nextByte){ //add a new byte to the bytes array
        if(length >= bytes.length){
            bytes = Arrays.copyOf(bytes, length*2);
        }
        bytes[length++] = nextByte;
    }

    @Override
    public byte[] encode(Message message) {
        return message.getBytes();
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private Message getMessage(){
        Message message = null;
        byte[] opcodeArr = Arrays.copyOfRange(bytes, 0, 2);
        short opcode = bytesToShort(opcodeArr);
        byte[] restOfMessage = Arrays.copyOfRange(bytes, 2, length);
        switch (opcode){
            case 1:
                message = new REGISTERMsg(restOfMessage);
                break;
            case 2:
                message = new LOGINMsg(restOfMessage);
                break;
            case 3:
                message = new LOGOUTMsg();
                break;
            case 4:
                message = new FOLLOWUNFOLLOWMsg(restOfMessage);
                break;
            case 5:
                message = new POSTMsg(restOfMessage);
                break;
            case 6:
                message = new PMMsg(restOfMessage);
                break;
            case 7:
                message = new LOGSTATMsg();
                break;
            case 8:
                message = new STATMsg(restOfMessage);
                break;
            case 12:
                message = new BLOCKMsg(restOfMessage);
                break;
        }
        length = 0;
        return message;
    }
}
