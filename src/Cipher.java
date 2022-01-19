import java.util.Objects;

public class Cipher {
    private static final int KEY_SIZE_128 = 128;
    private static final int KEY_SIZE_192 = 192;
    private static final int KEY_SIZE_256 = 256;
    private static final int NB_VALUE = 4;
    private static final int LENGTH_DATA    = 16000000;                      // Maximum plain text length in bytes
    private int KEY_SIZE = KEY_SIZE_128;
    private int Nb = NB_VALUE;
    private int Nk = KEY_SIZE/32;
    private int Nr = 6 + Math.max(Nb, Nk);
    private int LENGTH_KEY     = Nk * 4;                              // Key length in bytes
    private int LENGTH_EXP_KEY = Nb * (Nr + 1);                       // Expanded key length in int32
    private final int LENGTH_STATE   = Nb * Nb;                      // Length of states in bytes
    private byte[] keyByteArray             = new byte[LENGTH_KEY];         // Encryption key
    private byte[] plainTextByteArray       = new byte[LENGTH_DATA];        // Unencrypted bytes
    private byte[] cipherTextByteArray      = new byte[LENGTH_DATA];        // Encrypted bytes
    private byte[][] state                  = new byte[Nb][Nb];             // State matrix

    private boolean LookupTableQuickly = false;

    public byte[] getKeyByteArray() {
        return keyByteArray;
    }

    public void setKeyByteArray(byte[] keyByteArray) {
        this.keyByteArray = keyByteArray;
    }

    public byte[] getPlainTextByteArray() {
        return plainTextByteArray;
    }

    public void setPlainTextByteArray(byte[] plainTextByteArray) {
        this.plainTextByteArray = plainTextByteArray;
    }

    public byte[] getCipherTextByteArray() {
        return cipherTextByteArray;
    }

    public void setCipherTextByteArray(byte[] cipherTextByteArray) {
        this.cipherTextByteArray = cipherTextByteArray;
    }

    public byte[][] getState() {
        return state;
    }

    public void setState(byte[][] state) {
        this.state = state;
    }

    protected void outputState() {
        for (byte[] bytes : state) {
            for (byte aByte : bytes) System.out.print(String.format("%02X", aByte) + " ");
            System.out.println();
        }
    }

    protected void outputMatrix(byte[][] matrix) {
        for (byte[] bytes : matrix) {
            for (byte aByte : bytes) System.out.print(String.format("%02X", aByte) + " ");
            System.out.println();
        }
    }

    public int getKeySize() {
        return KEY_SIZE;
    }

    public void setKeySize(String keySize) {
        if (Objects.equals(keySize, "128")) {
            KEY_SIZE = KEY_SIZE_128;
        } else if (Objects.equals(keySize, "192")) {
            KEY_SIZE = KEY_SIZE_192;
        } else if (Objects.equals(keySize, "256")) {
            KEY_SIZE = KEY_SIZE_256;
        }
    }

    public boolean isLookupTableQuickly() {
        return LookupTableQuickly;
    }

    public void setLookupTableQuickly(boolean lookupTableQuickly) {
        LookupTableQuickly = lookupTableQuickly;
    }

    public int getNr() {
        return Nr;
    }
}
