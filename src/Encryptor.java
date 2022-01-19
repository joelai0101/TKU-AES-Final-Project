public class Encryptor extends Cipher {

    /**
     * 加密
     * @param plainTextBytes 要加密的位元組陣列
     * @param keyTextBytes 金鑰位元組陣列
     * @return
     */
    public String cipher (byte[] plainTextBytes, byte[] keyTextBytes) {
        setPlainTextByteArray(plainTextBytes);
        System.out.println("plainText bytes is:" + utils.byteToHex(plainTextBytes));
        setState(utils.arrayToMatrix(getPlainTextByteArray()));
        outputState();
        System.out.println();

        setKeyByteArray(keyTextBytes);
        System.out.println("key bytes is:" + utils.byteToHex(keyTextBytes));
        setState(cipher(getState(), getKeyByteArray()));
        System.out.println();

        String encoded = utils.matrixToHexString(getState());
        System.out.println("encoded: " + encoded);

        return encoded;
    }

    protected byte[][] cipher (byte[][] inputBytesMessage, byte[] key) {
        byte[][] state = inputBytesMessage;

        AddRoundKey roundKeyAdder = new AddRoundKey(utils.copyByteArray(key),false);
        SubBytes byteSubstituter = new SubBytes();
        ShiftRows rowShifter = new ShiftRows();
        MixColumns columnMixer = new MixColumns(false);
        state = roundKeyAdder.addRoundKey(state, 0);
        System.out.println("0th addRoundKey:");
        outputMatrix(state);
        for (int round = 1; round <= getNr() - 1; round++) {
            state = byteSubstituter.substituteBytes(state, false);
            System.out.println(round + "th substituteBytes:");
            outputMatrix(state);

            state = rowShifter.shiftRows(state, false);
            System.out.println(round + "th shiftRows:");
            outputMatrix(state);

            state = columnMixer.mixColumns(state, isLookupTableQuickly());
            System.out.println(round + "th mixColumns:");
            outputMatrix(state);

            state = roundKeyAdder.addRoundKey(state, round);
            System.out.println(round + "th addRoundKey:");
            outputMatrix(state);
        }

        state = byteSubstituter.substituteBytes(state, false);
        System.out.println(getNr() + "th substituteBytes:");
        outputMatrix(state);
        state = rowShifter.shiftRows(state, false);
        System.out.println(getNr() + "th shiftRows:");
        outputMatrix(state);

        state = roundKeyAdder.addRoundKey(state, getNr());
        System.out.println(getNr() + "th addRoundKey:");
        outputMatrix(state);

        return state;
    }
}
