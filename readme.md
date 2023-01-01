# 淡江大學 資訊安全導論 AES 期末程式
作業需求：
1. 基本功能：AES 32-bit 基本版(70%) (無法執行者分數至多25%，執行不正確者分數至多40%)
   1. 輸出入：輸入檔案格式無限制(5%)。(有限制者分數一律3%以下)
   2. Secret key的可以達到128/192/256 (10%) 。(未達者分數一律3%以下)
   3. AES-ECB(10%)、AES-CBC(10%)、AES-CTR(10%)
   4. AES-CFB-8(10%) 、AES-OFB-8(10%)
   5. 使用介面的親和性(5%)
2. 原始檔與註解(20%)。
   1. 註解(10%)
   2. 模組化或物件導向(10%)：須附文件說明。
3. 額外功能：加分項
   1. AES 32-bit查表加速版，正確執行加20% 。
   2. 3AES(EDE) with 3 keys，正確執行加20%。

該專案包含：
1. Cipher
   1. 加密器、解密器的父類別：
      1. Decryptor
         1. 處理密文解密
      2. Encryptor
         1. 處理明文加密
2. CipherMode
   1. 加解密模式的介面，包含：
      1. ECB
      2. CBC
      3. CTR
      4. CFB8
      5. OFB8
3. CipherController
   1. 加解密控制，包含設定參數的函式
4. AES_form
   1. 加密器應用程式介面的實作
5. AddRoundKey
   1. 加入回合金鑰給 byte 二維陣列的 class，並處理金鑰排程、金鑰擴充。
6. MixColumns
   2. 混行運算、混行反運算
7. ShiftRows
   1. 左旋位移與右旋位移
8. SubBytes
   1. 位元組替代、S-box
9. utils
   1. 包含一些轉換用的函式

如何執行、輸出資料：
1. 按下 Import File 將檔案輸入，會自動轉換位元組並以十六進位顯示在 input Text (hex) 右方 jTextHexArea ，
   1. 或是將一段十六進位明文加入在 jTextHexArea 當中，
2. 將一段字串作為金鑰輸入在 key 右方的 keyArea，會自動轉換位元組並以十六進位顯示在 key (hex) 右方 keyHexArea， 
   1. 必須為 128-bit、192-bit 或 256-bit。
   2. 金鑰會預先處理，並顯示回合金鑰。
3. 加密完成會以十六進位顯示在 output Text (hex) 右方 outputTextHexArea。
4. 可選擇加密模式、金鑰長度和是否使用查表加速法。
5. 3AES(EDE) with 3 keys 並沒有實作。
