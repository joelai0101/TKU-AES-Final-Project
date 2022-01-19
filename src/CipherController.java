import java.util.Objects;

/**
 * 加密控制器
 * */

public class CipherController {

    public static String ECB = "ECB";
    public static String CBC = "CBC";
    public static String CTR = "CTR";
    public static String CFB8 = "CFB-8";
    public static String OFB8 = "OFB-8";
    public String output;

    ECB ecb = new ECB();
    CBC cbc = new CBC();
    CTR ctr = new CTR();
    CFB8 cfb8 = new CFB8();
    OFB8 ofb8 = new OFB8();

    String currentMode = "ECB";

    /**
     * 加密控制器
     * @param mode 加密模式
     * @param KeySize 金鑰長度
     * @param lookupTableQuickly 是否使用查表加速
     */
    public CipherController(String mode, String KeySize, boolean lookupTableQuickly) {
        currentMode = mode;
        if (Objects.equals(currentMode, ECB)) {
            ecb.setKeySize(KeySize);
            ecb.setLookupTableQuickly(lookupTableQuickly);
        } else if (Objects.equals(currentMode, CBC)) {
            cbc.setKeySize(KeySize);
            cbc.setLookupTableQuickly(lookupTableQuickly);
        } else if (Objects.equals(currentMode, CTR)) {
            ctr.setKeySize(KeySize);
            ctr.setLookupTableQuickly(lookupTableQuickly);
        } else if (Objects.equals(currentMode, CFB8)) {
            cfb8.setKeySize(KeySize);
            cfb8.setLookupTableQuickly(lookupTableQuickly);
        } else if (Objects.equals(currentMode, OFB8)) {
            ofb8.setKeySize(KeySize);
            ofb8.setLookupTableQuickly(lookupTableQuickly);
        }
    }

    /**
     * 設定加密模式
     * @param mode 加密模式
     */
    public void setCurrentMode(String mode) {
        currentMode = mode;
        System.out.println("set Current Mode to " + currentMode);
    }

    /**
     * 設定金鑰長度
     * @param KeySize 金鑰長度
     */
    public void setKeySize(String KeySize) {
        if (Objects.equals(currentMode, ECB)) {
            ecb.setKeySize(KeySize);
            System.out.println(ECB + ": set Key Size to " + KeySize);
        } else if (Objects.equals(currentMode, CBC)) {
            cbc.setKeySize(KeySize);
            System.out.println(CBC + ": set Key Size to " + KeySize);
        } else if (Objects.equals(currentMode, CTR)) {
            ctr.setKeySize(KeySize);
            System.out.println(CTR + ": set Key Size to " + KeySize);
        } else if (Objects.equals(currentMode, CFB8)) {
            cfb8.setKeySize(KeySize);
            System.out.println(CFB8 + ": set Key Size to " + KeySize);
        } else if (Objects.equals(currentMode, OFB8)) {
            ofb8.setKeySize(KeySize);
            System.out.println(OFB8 + ": set Key Size to " + KeySize);
        }
    }

    /**
     * 設定查表加速
     * @param lookupTableQuickly 是否使用查表加速法
     */
    public void setLookupTableQuickly(boolean lookupTableQuickly) {
        if (Objects.equals(currentMode, ECB)) {
            ecb.setLookupTableQuickly(lookupTableQuickly);
            System.out.println(ECB + ": set LookupTableQuickly to " + lookupTableQuickly);
        } else if (Objects.equals(currentMode, CBC)) {
            cbc.setLookupTableQuickly(lookupTableQuickly);
            System.out.println(CBC + ": set LookupTableQuickly to " + lookupTableQuickly);
        } else if (Objects.equals(currentMode, CTR)) {
            ctr.setLookupTableQuickly(lookupTableQuickly);
            System.out.println(CTR + ": set LookupTableQuickly to " + lookupTableQuickly);
        } else if (Objects.equals(currentMode, CFB8)) {
            cfb8.setLookupTableQuickly(lookupTableQuickly);
            System.out.println(CFB8 + ": set LookupTableQuickly to " + lookupTableQuickly);
        } else if (Objects.equals(currentMode, OFB8)) {
            ofb8.setLookupTableQuickly(lookupTableQuickly);
            System.out.println(OFB8 + ": set LookupTableQuickly to " + lookupTableQuickly);
        }
    }

    /**
     * 進行加密
     * @param input 十六進位數字
     * @param key 金鑰
     * @return
     */
    public String encrypt(String input, String key) {
        byte[] inputBytes = utils.HexStringToByteArray(input);
        byte[] keyBytes = utils.HexStringToByteArray(key);
        if (Objects.equals(currentMode, ECB)) {
            output =  ecb.Encrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CBC)) {
            output =  cbc.Encrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CTR)) {
            output =  ctr.Encrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CFB8)) {
            output =  cfb8.Encrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, OFB8)) {
            output =  ofb8.Encrypt(inputBytes, keyBytes);
        }
        return output;
    }

    /**
     * 進行解密
     * @param input 十六進位數字
     * @param key 金鑰
     * @return
     */
    public String decrypt(String input, String key) {
        byte[] inputBytes = utils.HexStringToByteArray(input);
        byte[] keyBytes = utils.HexStringToByteArray(key);
        if (Objects.equals(currentMode, ECB)) {
            output =  ecb.Decrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CBC)) {
            output =  cbc.Decrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CTR)) {
            output =  ctr.Decrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, CFB8)) {
            output =  cfb8.Decrypt(inputBytes, keyBytes);
        } else if (Objects.equals(currentMode, OFB8)) {
            output =  ofb8.Decrypt(inputBytes, keyBytes);
        }

        return output;
    }
}
