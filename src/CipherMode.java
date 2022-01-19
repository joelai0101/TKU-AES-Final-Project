/***
 * 加密模式介面
 */

public interface CipherMode {

    Encryptor encryptor = new Encryptor();
    Decryptor decryptor = new Decryptor();

    int blockSizeInBytes = 16;

    void setLookupTableQuickly(boolean lookupTableQuickly);

    void setKeySize(String keySize);

    String Encrypt(byte[] plainTextBytes, byte[] keyBytes);

    String Decrypt(byte[] cipherTextBytes, byte[] keyBytes);

}
