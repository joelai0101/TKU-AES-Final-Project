import java.util.Objects;

public class ECB implements CipherMode{

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
        int length = paddingBytes.length;
        System.out.println("Bytes length:" + length);

        String outputString = "";
        byte[] subBytes = new byte[16];
        for (int i=0; i<length; i+=16) {
            System.arraycopy(paddingBytes, i, subBytes, 0, 16);
            String substring = encryptor.cipher(subBytes, keyBytes);
            outputString = outputString + substring;
            System.out.println(outputString);
            System.out.println();
        }

        return outputString;
    }

    @Override
    public String Decrypt(byte[] cipherTextBytes, byte[] keyBytes) {

        int length = cipherTextBytes.length;
        System.out.println("Bytes length:" + length);

        String outputString = "";
        byte[] subBytes = new byte[16];
        for (int i=0; i<length; i+=16) {
            System.arraycopy(cipherTextBytes, i, subBytes, 0, 16);
            String substring = decryptor.cipher(subBytes, keyBytes);
            outputString = outputString + substring;
            System.out.println(outputString);
            System.out.println();
        }

        byte[] origin = utils.PKCS7_UnPadding(utils.HexStringToByteArray(outputString));

        return utils.ByteArrayToHexString(origin);
    }
}
