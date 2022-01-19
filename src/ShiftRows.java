public class ShiftRows {
    /**
     * 旋轉位移函數
     * @param state 狀態矩陣
     * @param decrypt 是否解密
     * @return 傳回轉換過的狀態矩陣
     */
    public byte[][] shiftRows(byte[][] state, boolean decrypt) {
        byte[] temp = new byte[4];
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (decrypt) {
                    temp[(j + i) % 4] = state[i][j];
                } else {
                    temp[j] = state[i][(j + i) % 4];
                }
            }
            System.arraycopy(temp, 0, state[i], 0, 4);
        }
        return state;
    }
}
