import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class utils {
    public static byte[] copyByteArray(byte[] toCopy) {
        byte[] output = new byte[toCopy.length];
        System.arraycopy(toCopy, 0, output, 0, toCopy.length);
        return output;
    }

    public static byte[][] copyByteMatrix(byte[][] toCopy) {
        byte[][] output = new byte[toCopy.length][toCopy[0].length];

        System.out.println(toCopy.length);
        System.out.println(toCopy[0].length);

        //  for (int i = 0; i < toCopy.length; i++) {
        //      for (int j = 0; j < toCopy[i].length; j++)
        //          System.out.print(String.format("%02X", toCopy[i][j]) + " ");
        //      System.out.println();
        //  }


        for (int i = 0; i < toCopy.length; i++) {
            System.arraycopy(toCopy[i], 0, output[i], 0, toCopy[0].length);
        }
        return output;
    }

    private static String readLine(Path inputFile, int lineNbr) {
        String line;
        try {
            List<String> lines = Files.readAllLines(inputFile);
            if (lines.size() > lineNbr) {
                // Remove optional whitespaces that might be in the input file
                line = lines.get(lineNbr).replaceAll(" ", "");
                if (line.length() != 128) {
                    throw new Exception(
                            "The text file must contain 128 0s and 1s!");
                }
            } else {
                throw new Exception("The file does not contain "
                        + (lineNbr + 1) + " lines");
            }
        } catch (Exception e) {
            line = null;
            System.err.println("Could not read text from file: "
                    + e.getMessage());
            e.printStackTrace();
        }
        return line;
    }

    /**
     * Reads a plain text from an input file. The cipher text must have a length
     * of 128 characters containing 0s and 1s and whitespaces.
     *
     * @param inputFile
     *            path to a file
     * @return cipher text as a String
     */
    public static String readPlainText(Path inputFile) {
        return readLine(inputFile, 0);
    }

    /**
     * Reads a cipher text from an input file. The cipher text must have a
     * length of 128 characters containing 0s and 1s and whitespaces.
     *
     * @param inputFile
     *            path to a file
     * @return cipher text as a String
     */
    public static String readCipherText(Path inputFile) {
        return readLine(inputFile, 0);
    }

    /**
     * Reads the key from a given file. The key must be in the second line in
     * the file.
     *
     * @param inputFile
     *            path to a file
     * @return key as String
     */
    public static String readKey(Path inputFile) {
        return readLine(inputFile, 1);
    }

    /**
     * Parses a string to a byte array
     *
     * @param binaryString
     *            containing 0s and 1s, and it must have a length of 128
     *            characters
     * @return byte array containing the data parsed from the string
     */
    public static byte[] stringToByteArray(String binaryString) {
        byte[] output = new byte[16];
        for (int i = 0; i < 16; i++) {
            String part = binaryString.substring(i * 8, (i + 1) * 8);
            output[i] = (byte) Integer.parseInt(part, 2);
        }
        return output;
    }

    /**
     * Converts a byte array to a 4x4 matrix
     *
     * @param array
     *            to be converted
     * @return byte matrix with data of the array
     */
    public static byte[][] arrayToMatrix(byte[] array) {
        byte[][] hexMatrix = new byte[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                hexMatrix[j][i] = array[k];
                k++;
            }
        }
        return hexMatrix;
    }

    /**
     * Converts a byte matrix to a byte array
     *
     * @param matrix
     *            to be converted
     * @return byte array with the data of the matrix
     */
    public static byte[] matrixToArray(byte[][] matrix) {
        byte[] array = new byte[16];
        int k = 0;
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                array[k] = matrix[j][i];
                k++;
            }
        }
        return array;
    }

    /**
     * Formats a byte matrix to a string. Each row in the matrix will be
     * presented by a single line in the string.
     *
     * @param matrix
     *            to be converted
     * @param pretty
     *            if true, a whitespace will be added after each 8 bits.
     * @return String representing the matrix
     */
    public static String matrixToString(byte[][] matrix, boolean pretty) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) { // i = row #
            for (int j = 0; j < 4; j++) { // j = column #
                byte b = matrix[j][i];
                sb.append(byteToString(b));
                if (pretty)
                    sb.append(" ");
            }
            if (pretty)
                sb.append("\n");
        }
        return sb.toString();
    }

    public static String byteStringToHexString(String input) {
        return matrixToHexString(arrayToMatrix(stringToByteArray(input)));
    }

    /**
     * Formats a byte array to a string. each byte will be presented by 8
     * characters. 2 Arrays of the same length will always have the same output
     * length
     *
     * @param array
     *            input array
     * @param pretty
     *            if true, a whitespace will be inserted after each byte
     * @return String
     */
    public static String arrayToString(byte[] array, boolean pretty) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 16; j++) {
            byte b = array[j];
            sb.append(byteToString(b));
            if (pretty)
                sb.append(" ");
        }
        return sb.toString();
    }

    public static String matrixToHexString(byte[][] matrix) {
        byte[] array = matrixToArray(matrix);
        StringBuilder sb = new StringBuilder();
        for (byte b : array)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString().toUpperCase();
    }

    public static byte[][] hexStringToMatrix(String input) {
        byte[] output = new byte[16];
        for (int i = 0; i < 16; i++) {
            String part = input.substring(i * 2, (i + 1) * 2);
            output[i] = (byte) Integer.parseInt(part, 16);
        }
        return arrayToMatrix(output);
    }

    /**
     * Formats a byte to a string with a length of 8. If the byte is smaller
     * than 128 (=7 bytes) the string will be padded with leading 0s.
     *
     * @param b
     *            byte to be formatted
     * @return output string with a length of 8
     */
    public static String byteToString(byte b) {
        StringBuilder value = new StringBuilder(Integer.toBinaryString(Byte.toUnsignedInt(b)));
        int missingZeros = 8 - value.length();
        for (int i = 0; i < missingZeros; i++) {
            value.insert(0, "0");
        }
        return value.toString();
    }

    /**
     * Compares to byte arrays and returns the number of bits that are different
     *
     * @return int > 0
     */
    public static int numberOfDifferentBits(byte[] original, byte[] newText) {
        // It is very easy to compare to strings
        String org = arrayToString(original, false);
        String newTex = arrayToString(newText, false);
        int diff = 0;
        for (int i = 0; i < org.length(); i++) {
            if (org.charAt(i) != newTex.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        // int pad = 0;
        System.out.println("Hex string length is " + len);
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i< len; i += 2) {
            // Convert each character into an integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String StringToHexString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] charArray = str.toCharArray();

        for (char c : charArray) {

            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }
        return stringBuilder.toString();
    }

    public static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }

    /**
     * 填充模式，使用 PKCS7 Padding
     * */
    public static byte[] PKCS7_Padding(byte[] bytes) {
        int length = bytes.length;

        if (length%CipherMode.blockSizeInBytes != 0) {
            int Padding = CipherMode.blockSizeInBytes - length%CipherMode.blockSizeInBytes;
            System.out.println("Need padding " + Padding + " times");
            byte[] newBytes = new byte[length+Padding];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            System.out.println("New Bytes length is:" + newBytes.length);

            for (int i = 0; i<Padding; i++) {
                newBytes[bytes.length+i] = (byte) Padding;
            }
            return newBytes;
        } else {
            byte[] newBytes = new byte[length+CipherMode.blockSizeInBytes];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            for (int i = 0; i<CipherMode.blockSizeInBytes; i++) {
                newBytes[bytes.length+i] = (byte) CipherMode.blockSizeInBytes;
            }
            return newBytes;
        }
    }

    //  去除填充
    public static byte[] PKCS7_UnPadding(byte[] bytes) {
        System.out.println(bytes.length - (bytes[bytes.length -1]));
        byte[] origin = new byte[bytes.length - (bytes[bytes.length -1])];
        System.arraycopy(bytes, 0, origin, 0, origin.length);
        return origin;
    }
}
