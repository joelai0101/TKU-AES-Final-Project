import java.util.Objects;

public class OFB8 implements CipherMode {
    @Override
    public void setLookupTableQuickly(boolean lookupTableQuickly) {
        encryptor.setLookupTableQuickly(lookupTableQuickly);
        decryptor.setLookupTableQuickly(lookupTableQuickly);
    }

    @Override
    public void setKeySize(String keySize) {
        if (Objects.equals(keySize, "128")) {
            encryptor.setKeySize(keySize);
            decryptor.setKeySize(keySize);
        } else if (Objects.equals(keySize, "192")) {
            encryptor.setKeySize(keySize);
            decryptor.setKeySize(keySize);
        } else if (Objects.equals(keySize, "256")) {
            encryptor.setKeySize(keySize);
            decryptor.setKeySize(keySize);
        }
    }

    @Override
    public String Encrypt(byte[] plainTextBytes, byte[] keyBytes) {
        byte[] paddingBytes = utils.PKCS7_Padding(plainTextBytes);
        byte[] iv = {
                (byte)0xC3, (byte)0x92, (byte)0x43, (byte)0xF5,
                (byte)0xD4, (byte)0xE2, (byte)0x66, (byte)0x21,
                (byte)0x47, (byte)0xB8, (byte)0xE7, (byte)0x14,
                (byte)0x59, (byte)0x87, (byte)0x87, (byte)0x32
        };

        int length = paddingBytes.length;
        System.out.println("Bytes length:" + length);

        String outputString = "";
        byte[] subBytes = new byte[16];
        byte[] iv2 = new byte[16];

        for (int i=0; i<length; i+=16) {
            System.arraycopy(paddingBytes, i, subBytes, 0, 16);
            System.arraycopy(iv, 0, iv2, 0, 16);

            String ivString = encryptor.cipher(iv, keyBytes);
            byte[] ivBytes = utils.HexStringToByteArray(ivString);

            for (int shift=1; shift<16; shift++) {
                iv2[shift] = iv2[shift-1];
            }
            iv2[15] = ivBytes[15];

            xor2State(ivBytes, subBytes);

            String substring = utils.ByteArrayToHexString(ivBytes);
            outputString = outputString + substring;

            System.arraycopy(iv2, 0, iv, 0, 16);
        }

        return outputString;
    }

    @Override
    public String Decrypt(byte[] cipherTextBytes, byte[] keyBytes) {
        byte[] iv = {
                (byte)0xC3, (byte)0x92, (byte)0x43, (byte)0xF5,
                (byte)0xD4, (byte)0xE2, (byte)0x66, (byte)0x21,
                (byte)0x47, (byte)0xB8, (byte)0xE7, (byte)0x14,
                (byte)0x59, (byte)0x87, (byte)0x87, (byte)0x32
        };

        int length = cipherTextBytes.length;
        System.out.println("Bytes length:" + length);

        String outputString = "";
        byte[] subBytes = new byte[16];
        byte[] iv2 = new byte[16];

        for (int i=0; i<length; i+=16) {
            System.arraycopy(cipherTextBytes, i, subBytes, 0, 16);
            System.arraycopy(iv, 0, iv2, 0, 16);

            String ivString = encryptor.cipher(iv, keyBytes);
            byte[] ivBytes = utils.HexStringToByteArray(ivString);

            for (int shift=1; shift<16; shift++) {
                iv2[shift] = iv2[shift-1];
            }
            iv2[15] = ivBytes[15];

            xor2State(ivBytes, subBytes);

            String substring = utils.ByteArrayToHexString(ivBytes);
            outputString = outputString + substring;

            System.arraycopy(iv2, 0, iv, 0, 16);
        }

        byte[] origin = utils.PKCS7_UnPadding(utils.HexStringToByteArray(outputString));

        return utils.ByteArrayToHexString(origin);
    }

    private void xor2State(byte[] dest, byte[] src) {
        for (int i=0 ; i<src.length ; i++)
            dest[i] ^= src[i];
    }
}
