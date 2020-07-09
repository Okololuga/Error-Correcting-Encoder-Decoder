import java.util.Random;

public class ByteMessage {
    protected String text;
    protected String errorText;

    String encodeExpand = "";

    String decodeCorrect = "";


    public String changeOneBit(String inputByteMessage) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(inputByteMessage);
        for (int i = 0; i < inputByteMessage.length(); i+=8) {
            int position =  i + rand.nextInt(7);
            if (sb.charAt(position) == '1') {
                sb.setCharAt(position, '0');
            } else {
                sb.setCharAt(position, '1');
            }
        }
        return sb.toString();
    }

    public String decode (byte[] inputMessage) {
        final int SIZE = (inputMessage.length / 8 );

        StringBuilder fixedMessage = new StringBuilder();
        StringBuilder correctMessage = new StringBuilder();
        String decodeMessage = "";

        for (byte input : inputMessage) {
            byte a = (byte) (input >> 0b110 & 0b11 );
            byte b = (byte) (input >> 0b100 & 0b11);
            byte c = (byte) (input >> 0b10 & 0b11);
            byte d = (byte) (input & 0b11);
            byte[] mas = {a,b,c,d};

            for (int i = 0; i < 4; i++) {
                if ( mas[i] == 0b01 || mas[i] == 0b10) { //если 10 или 01
                    if (i == 0) {
                        mas[i] = (byte) (mas[1] ^ mas[2] ^ mas[3]);
                    } else if (i == 1) {
                        mas[i] = (byte) (mas[0] ^ mas[2] ^ mas[3]);
                    } else if (i == 2) {
                        mas[i] = (byte) (mas[0] ^ mas[1] ^ mas[3]);
                    } else if (i == 3) {
                        mas[i] = (byte) (mas[0] ^ mas[1] ^ mas[2]);
                    }
                    mas[i] = (byte) (mas[i] & 0b11);
                }
            }

            /*для заполнения поля correct*/
            for (byte digit : mas){
                if ((digit & 0b11) == 0b00) {
                    correctMessage.append("00");
                } else {
                    correctMessage.append("11");
                }
            }

            //убираю вторую цифру, остается 0 или 1, и пихаю все в стрингбилдер
            for (int i = 0; i < 3; i++) {
                mas[i] = (byte) (mas[i] & 0b01);
                if (mas[i] == 0) {
                    fixedMessage.append(0);
                } else {
                    fixedMessage.append(1);
                }
            }
        }
        decodeCorrect = correctMessage.toString();
        return fixedMessage.toString();
    }

    public  String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    public byte[] fromBinary(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++)
            if ((c = s.charAt(i)) == '1')
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if (c != '0')
                throw new IllegalArgumentException();
        return toReturn;
    }

    public String encodeFile(String binaryString) {

        String encoded = "";

        for (int i = 0; i < binaryString.length(); i += 3) {

            int startSubIndex = i;
            int stopSubIndex = Math.min(i+3, binaryString.length());

            String currSub = binaryString.substring(startSubIndex, stopSubIndex);

            String encodedPart;
            String expandPart;

            if (currSub.length() == 3) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) +
                                currSub.substring(1, 2).repeat(2) +
                                currSub.substring(2, 3).repeat(2);
                expandPart = encodedPart;
            } else if (currSub.length() == 2) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) +
                                currSub.substring(1, 2).repeat(2) + "00";
                expandPart =
                        currSub.substring(0, 1).repeat(2) +
                                currSub.substring(1, 2).repeat(2) + "..";
            } else if (currSub.length() == 1) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) + "0000";
                expandPart =
                        currSub.substring(0, 1).repeat(2) + "0000";
            } else {
                encodedPart = "000000";
                expandPart = "......";
            }

            int parityCounts = 0;

            if (encodedPart.charAt(0) == '1') {
                parityCounts++;
            }

            if (encodedPart.charAt(2) == '1') {
                parityCounts++;
            }

            if (encodedPart.charAt(4) == '1') {
                parityCounts++;
            }

            if (parityCounts % 2 == 1) {
                encodedPart += "11";
                expandPart +="..";
            } else {
                encodedPart += "00";
                expandPart += "..";
            }

            encoded += encodedPart;
            encodeExpand += expandPart;
        }

        return encoded;
    }
}
