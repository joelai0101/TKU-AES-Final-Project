public class AddRoundKey extends Cipher{
    private final byte[][] subKeys;
    private final boolean decrypt;

    public void printAllSubKey() {
        for (int i = 0; i < subKeys.length; i++) {
            System.out.print("round "+ i +":");
            for (int j = 0; j < subKeys[i].length; j++)
                System.out.print(String.format("%02X", subKeys[i][j]) + " ");
            System.out.println();
        }
    }

    /**
     * AddRoundKey 建構子
     * @param key 金鑰
     * @param decrypt 是否解密
     */
    public AddRoundKey(byte[] key, boolean decrypt) {
        subKeys = utils.copyByteMatrix(generateSubKeys(key));
        printAllSubKey();
        this.decrypt = decrypt;
    }

    /**
     * 金鑰擴展，生成回合子金鑰
     * @param key 金鑰
     * @return 傳回子金鑰的二維陣列
     */
    private static byte[][] generateSubKeys(byte[] key) {
        if (key.length!=16 && key.length!=24 && key.length!=32) {
            throw new IllegalArgumentException (
                    "key length is not right. len:" +
                            key.length*8 + "bits");
        }
        System.out.println("key length is " + key.length + " words.");

        int KeyRows		= key.length/4; // 傳入的鎖鑰列數 4 6 8
        int ExpansionKeyAmount	= KeyRows+7;    // 拓展後的鎖鑰數量 11 13 15
        byte[] temp = new byte[4];
        byte[] keys = new byte[ExpansionKeyAmount*16];  // 11*16 = 176
        byte[][] _subKeys = new byte[ExpansionKeyAmount][16];
        int round = 1;
        int i = key.length; // first 16, 20, 24 bytes already set

        // set first bytes
        System.arraycopy(key, 0, keys, 0, key.length);

        while (i < ExpansionKeyAmount*16) { // need 176 bytes of keys
            // set temp bytes
            System.arraycopy(keys, i - 4, temp, 0, 4);
            // if new key, do main calculations
            if (i % 16 == 0) {
                // Rotate tempBytes
                temp = rotate(temp);
                // Apply S-box
                for (int j = 0; j < 4; j++) {   //  相當於 SubWord 傳入一個 4-byte 的字組
                    // We always want the encryption SBox. for decryption, we use
                    // the same keys, but in inverse order.
                    temp[j] = applySBox(temp[j], false);
                }
                // XOR 1st bit of temp with round constant
                temp[0] ^= rCon(round);
                round++;
            }
            byte newValue;
            // assign temp bytes to keys
            for (int j = 0; j < 4; j++) {
                // set new value
                newValue = (byte) (temp[j] ^ keys[i - key.length]);
                // store new value
                keys[i] = newValue;
                i++;
            }
        }

        // convert keys into subKeys arrays
        int total = 0;

        for (int rnd = 0; rnd < ExpansionKeyAmount; rnd++) {
            byte[] newRoundKey = new byte[16];
            // set new key value
            for (int j = 0; j < 16; j++) {
                newRoundKey[j] = keys[total];
                total++;
            }
            // set subKeys to new key value
            _subKeys[rnd] = utils.copyByteArray(newRoundKey);
        }

        return _subKeys;
    }

    /**
     *  傳入位元組，經過 S-box(Inverse S-Box) 的替代轉換
     * @param input 位元組輸入
     * @param decrypt 是否解密
     * @return 傳回轉換過的一位元組
     */
    private static byte applySBox(byte input, boolean decrypt) {
        byte[] matrix = SubBytes.SBox1D;
        if (decrypt) {
            // Use the inverted matrix when decrypting
            matrix = SubBytes.InverseSBox1D;
        }
        return matrix[input & 0xFF];
    }

    public static final byte[] Rcon = {
            (byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08, (byte)0x10, (byte)0x20,
            (byte)0x40, (byte)0x80, (byte)0x1B, (byte)0x36, (byte)0x6C, (byte)0xC3,
            (byte)0x9D, (byte)0x21, (byte)0x42
    };

    /**
     * 輸入該回合的回合數，根據此數計算出該回合的回合常數
     * @param input 回合數
     * @return 傳回一個 4-byte 的字組
     */
    private static int rCon(int input) {
        int x = 1;
        // if input is 0, return 0
        if (input == 0) {
            x = 0;
        } else {
            // until input = 1, multiply x by 2
            while (input != 1) {
                x = multiply((byte) x, (byte) 2);
                input--;
            }
        }
        return x;
    }

    /**
     * 位元組視為多項式，位於有限體 GF(2^8) 的運算
     * @return 傳回一位元組
     */
    private static byte multiply(byte a, byte b) {  //  Multiplies two bytes in galois field 2^8
        byte returnValue = 0;
        byte temp;
        while (a != 0) {
            if ((a & 1) != 0)
                returnValue = (byte) (returnValue ^ b);
            temp = (byte) (b & 0x80);
            b = (byte) (b << 1);
            if (temp != 0)
                b = (byte) (b ^ 0x1b);
            a = (byte) ((a & 0xff) >> 1);
        }
        return returnValue;
    }

    /**
     * addRoundKey 加入回合金鑰
     * @param state 狀態矩陣
     * @param round 回合數
     * @return 傳回運算過後的狀態矩陣
     */
    public byte[][] addRoundKey(byte[][] state, int round) {
        if (decrypt) {  //  是否解密
            round = getNr() - round;
        }
        byte[] key = subKeys[round];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                state[j][i] = (byte) (state[j][i] ^ key[i * 4 + j]);
        return state;
    }

    /**
     * 旋轉 4-byte 的字組，相當於 RotWord
     * @param input 位元組陣列、字組
     * @return 一樣傳回一個 4-byte 的字組
     */
    private static byte[] rotate(byte[] input) {
        byte[] output = new byte[input.length];
        byte a = input[0];
        System.arraycopy(input, 1, output, 0, 3);
        output[3] = a;
        return output;
    }
}
