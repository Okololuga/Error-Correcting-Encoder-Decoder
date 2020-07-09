import java.util.Random;

public class Message {
    protected String inputStr;
    protected String encodeStr;
    protected String strWithErrors;
    protected String decodeStr;

    protected final String simbols = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    Random random = new Random();

    Message() {}
    Message(String inputText) {
        this.inputStr = inputText;
        this.encodeStr = encodingMessage(inputStr);
        this.strWithErrors = replaceCharacter(encodeStr);
        this.decodeStr = inputStr;
    }

    public String getInputStr() {
        return inputStr;
    }

    public String getEncodeStr() {
        return encodeStr;
    }

    public String getStrWithErrors() {
        return strWithErrors;
    }

    public String getDecodeStr() {
        return decodeStr;
    }

    public static String encodingMessage (String inputStr) {
        char[] strArr = new char[inputStr.length() * 3];
        for (int i = 0; i < inputStr.length(); i++) {
            for (int j = 0; j < 3; j++) {
                strArr[i*3+j] = inputStr.charAt(i);
            }
        }
        return String.valueOf(strArr);
    }

    /*simulate errors function*/
    public  String replaceCharacter (String str) {
        char[] strArr = str.toCharArray();
        for (int i = 0; i < strArr.length; i+=3) {
            char randomCharacter = simbols.charAt(random.nextInt(simbols.length()));
            /*если редомный символ получился такой же как заменяемый*/
            if (randomCharacter == str.charAt(i)) {
                randomCharacter += 1;
            }
            strArr[i] = randomCharacter;
        }
        return String.valueOf(strArr);
    }

    public  String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    public   byte[] fromBinary(String s) {
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

            if (currSub.length() == 3) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) +
                                currSub.substring(1, 2).repeat(2) +
                                currSub.substring(2, 3).repeat(2);
            } else if (currSub.length() == 2) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) +
                                currSub.substring(1, 2).repeat(2) + "00";
            } else if (currSub.length() == 1) {
                encodedPart =
                        currSub.substring(0, 1).repeat(2) + "0000";
            } else {
                encodedPart = "000000";
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
            } else {
                encodedPart += "00";
            }

            encoded += encodedPart;
        }

        return encoded;
    }
}