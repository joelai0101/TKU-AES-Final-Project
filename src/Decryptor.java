public class Decryptor extends Cipher {

    /**
     * 解密
     * @param cipherTextBytes 要解密的位元組陣列
     * @param keyTextBytes 金鑰位元組陣列
     * @return
     */
    public String cipher (byte[] cipherTextBytes, byte[] keyTextBytes) {
        setCipherTextByteArray(cipherTextBytes);
        System.out.println("cipherText bytes is:" + utils.byteToHex(cipherTextBytes));
        setState(utils.arrayToMatrix(getCipherTextByteArray()));
        outputState();
        System.out.println();

        setKeyByteArray(keyTextBytes);
        System.out.println("key bytes is:" + utils.byteToHex(keyTextBytes));
        setState(cipher(getState(), getKeyByteArray()));
        System.out.println();

        // byte[] decodedBytes = utils.matrixToArray(getState());
        // String decoded = utils.ByteArrayToHexString(decodedBytes);
        String decoded = utils.matrixToHexString(getState());
        System.out.println("decoded: " + decoded);

        return decoded;
    }

    protected byte[][] cipher(byte[][] inputBytesMessage, byte[] key) {
        byte[][] state = inputBytesMessage;

        AddRoundKey roundKeyAdder = new AddRoundKey(utils.copyByteArray(key),true);
        SubBytes byteSubstituter = new SubBytes();
        ShiftRows rowShifter = new ShiftRows();
        MixColumns columnMixer = new MixColumns(true);
        state = roundKeyAdder.addRoundKey(state, 0);
        System.out.println(getNr() +"th addRoundKey:");
        outputMatrix(state);
        for (int round = 1; round <= getNr() - 1; round++) {
            state = rowShifter.shiftRows(state, true);
            System.out.println(getNr() - round + "th shiftRows:");
            outputMatrix(state);

            state = byteSubstituter.substituteBytes(state, true);
            System.out.println(getNr() - round + "th substituteBytes:");
            outputMatrix(state);

            state = roundKeyAdder.addRoundKey(state, round);
            System.out.println(getNr() - round + "th addRoundKey:");
            outputMatrix(state);

            state = columnMixer.mixColumns(state, isLookupTableQuickly());
            System.out.println(getNr() - round + "th mixColumns:");
            outputMatrix(state);
        }

        state = rowShifter.shiftRows(state, true);
        System.out.println(0 + "th shiftRows:");
        outputMatrix(state);

        state = byteSubstituter.substituteBytes(state, true);
        System.out.println(0 + "th substituteBytes:");
        outputMatrix(state);

        state = roundKeyAdder.addRoundKey(state, getNr());
        System.out.println(0 + "th addRoundKey:");
        outputMatrix(state);

        return state;
    }
}
