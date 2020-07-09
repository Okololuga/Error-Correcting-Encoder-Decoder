//package correcter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    static ByteMessage byteMessage = new ByteMessage();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String inputText = "";

        System.out.print("Write a mode: ");
        switch (in.nextLine()) {
            case "encode": encode();
                break;
            case "send": send();
                break;
            case "decode": decode();
                break;
        }
        in.close();
    }

    /*для вывода в консоль bin view*/
    public static String consolBinFormat(String str) {
        StringBuilder bin = new StringBuilder();
        bin.append(str);
        bin.setLength(str.length());
        for (int i = 8; i < bin.length(); i+=9) {
            bin.insert(i,' ');
        }
        return bin.toString();
    }

    /*перевожу строку в массив байтов, чтобы сделать вывод в hex view*/
    public static String consolHexView(String str) {
        String view = "";
        byte[] forHexView = new byte[str.length() / 8];
        for (int i = 0, j = 0; i < str.length(); i+=8, j++) {
            String bytes = str.substring(i, i + 8);
            forHexView[j] = (byte) Integer.parseInt(bytes, 2);
        }
        for (byte n : forHexView) {
            String s = String.format("%2X", n).replace(' ', '0');
            view += String.format("%S ", s);
        }
        return view.substring(0,view.length()-1); //чтобы убрать пробел в конце
    }

    public static String readFile(String fileName)  {
        String text = "";
        try {
            File file = new File(fileName);
            Scanner read = new Scanner(file);
            while (read.hasNext()) {
                text += read.nextLine();
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found", fileName);
        }
        return text;
    }

    public static void saveFile(byte[] errorByteText, String fileName) {
        File file = new File(fileName);
        try {
            FileWriter writer = new FileWriter(file);
            for (byte b : errorByteText) {
                writer.write(b);
            }
            writer.close();
        }
         catch (Exception e) {
            e.printStackTrace();
        }
     }

    public static void saveFile(String str, String fileName) {
        File file = new File(fileName);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(str);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encode() {
       // ByteMessage byteMessage = new ByteMessage();
        String inputText = readFile("./send.txt");
        if (!inputText.isEmpty()) {

            /*перевожу строку в бин вид*/
            String inputTextBinary = byteMessage.toBinary(inputText.getBytes());

            System.out.println();
            System.out.println("send.txt:");
            System.out.println("text view: " + inputText);
            System.out.println("hex view: " + consolHexView(inputTextBinary));
            System.out.println("bin view: " + consolBinFormat(inputTextBinary));

            /*делаю encoded*/
            String inputTextEncoded = byteMessage.encodeFile(inputTextBinary);

            System.out.println();
            System.out.println("encoded.txt:");
            System.out.println("expand: " + consolBinFormat(byteMessage.encodeExpand));
            System.out.println("parity: " + consolBinFormat(inputTextEncoded));
            System.out.println("hex view: " + consolHexView(inputTextEncoded));

            saveFile(inputTextEncoded, "./encoded.txt");
        }
    }

    public static void send() {
        String inputText = readFile("./encoded.txt");
        if ( !inputText.isEmpty()) {

            byte[] textFromFile = byteMessage.fromBinary(inputText);
            inputText = byteMessage.toBinary(textFromFile);

            System.out.println();
            System.out.println("encoded.txt:");
            System.out.printf("hex view: %S\n", consolHexView(inputText));

            System.out.println("bin view: " + consolBinFormat(inputText));
            System.out.println();

            String errorText = byteMessage.changeOneBit(inputText);

            System.out.println("received.txt:");
            System.out.println("bin view: " + consolBinFormat(errorText));
            System.out.printf("hex view: %S", consolHexView(errorText));
            saveFile(errorText, "./received.txt");
        }
    }

    public static void decode() {

        String inputText = readFile("./received.txt");

        if (!inputText.isEmpty()) {

            byte[] encodeTextFromFile = byteMessage.fromBinary(inputText);

            String decodeText = byteMessage.decode(encodeTextFromFile);

            System.out.println();
            System.out.println("received.txt:");
            System.out.printf("hex view: %S\n", consolHexView(inputText));
            System.out.println("bin view: " + consolBinFormat(inputText));
            System.out.println();

            System.out.println("decoded.txt:");
            System.out.println("correct: " + consolBinFormat(byteMessage.decodeCorrect));
            System.out.println("decode: " + consolBinFormat(decodeText));
            String remove = "";
            int size = decodeText.length() / 8;
            remove = decodeText.substring(0, size * 8);
            System.out.println("remove: " + consolBinFormat(remove));
            System.out.printf("hex view: %S\n", consolHexView(remove));

            byte[] correctMessage = byteMessage.fromBinary(remove);
            String textView = "";

            for (byte letter : correctMessage) {
                textView += (char) letter;
            }
            System.out.println("text view: " + textView);
            saveFile(textView, "decoded.txt");
        }
    }

}


