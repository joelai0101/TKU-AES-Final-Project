import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AES_form {
    private JButton importFileButton;
    private JTextField filePath;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea jTextHexArea;
    private JTextArea keyArea;
    private JTextArea keyHexArea;
    private JTextArea outputTextHexArea;
    private JComboBox cipherMode;
    String default_mode = "ECB";
    private JComboBox keySize;
    String default_size = "128";
    private JCheckBox lookupTableQuicklyCheckBox;
    boolean lookupTableQuickly = false;
    private JPanel headerPanel;
    private JPanel bottomPanel;
    private JPanel lowerPanel;
    private JPanel formPanel;


    public AES_form() {
        CipherController cipherController = new CipherController(default_mode, default_size, false);

        cipherMode.addItem("ECB");
        cipherMode.addItem("CBC");
        cipherMode.addItem("CTR");
        cipherMode.addItem("CFB-8");
        cipherMode.addItem("OFB-8");

        keySize.addItem("128");
        keySize.addItem("192");
        keySize.addItem("256");

        importFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog((Component)e.getSource());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        String fileName = file.toString();
                        filePath.setText(fileName);
                        byte[] bytes = Files.readAllBytes(Paths.get(fileName));
                        String jTextHexString = utils.byteToHex(bytes); // '\n' 轉 hex 不會漏 0
                        jTextHexArea.setText(jTextHexString);
                    } catch (Exception ex) {
                        System.out.println("problem accessing file " + file.getAbsolutePath());
                        System.out.println(ex);
                    }
                } else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });

        /**
         * 加密按鈕
         */
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jTextHexString = jTextHexArea.getText();
                String keyHexString = keyHexArea.getText();
                String mode = String.valueOf(cipherMode.getSelectedItem());
                String size = String.valueOf(keySize.getSelectedItem());
                cipherController.setKeySize(mode);
                cipherController.setKeySize(size);
                cipherController.setLookupTableQuickly(lookupTableQuickly);
                String outputTextHexString = "";
                outputTextHexString = cipherController.encrypt(jTextHexString, keyHexString);
                //  System.out.println("encryptButton: cipherController.encrypt failed");
                outputTextHexArea.setText(outputTextHexString);
            }
        });

        /**
         * 解密按鈕
         */
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jTextHexString = jTextHexArea.getText();
                String keyHexString = keyHexArea.getText();
                String mode = String.valueOf(cipherMode.getSelectedItem());
                String size = String.valueOf(keySize.getSelectedItem());
                cipherController.setKeySize(mode);
                cipherController.setKeySize(size);
                cipherController.setLookupTableQuickly(lookupTableQuickly);
                String outputTextHexString = "";
                outputTextHexString = cipherController.decrypt(jTextHexString, keyHexString);
                //  System.out.println("decryptButton: cipherController.decrypt failed");
                outputTextHexArea.setText(outputTextHexString);
            }
        });

        /**
         * 設定金鑰長度
         */
        keySize.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object item = e.getItem();
                    keySize.setSelectedItem(item);
                    System.out.println(keySize.getSelectedItem());
                    cipherController.setKeySize(keySize.getSelectedItem().toString());
                }
            }
        });

        /**
         * 設定加解密模式
         */
        cipherMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Object item = e.getItem();
                    cipherMode.setSelectedItem(item);
                    System.out.println(cipherMode.getSelectedItem());
                    cipherController.setCurrentMode(cipherMode.getSelectedItem().toString());
                }
            }
        });

        /**
         * 設定是否使用查表加速法
         */
        lookupTableQuicklyCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lookupTableQuickly = !lookupTableQuickly;
                System.out.println("LookupTableQuickly is " + lookupTableQuickly);
                cipherController.setLookupTableQuickly(lookupTableQuickly);
            }
        });

        /**
         * 輸入金鑰字串，自動轉成十六進位
         */
        keyArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                keyHexArea.setText(utils.StringToHexString(keyArea.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                keyHexArea.setText(utils.StringToHexString(keyArea.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                keyHexArea.setText(utils.StringToHexString(keyArea.getText()));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AES_form");
        frame.setContentPane(new AES_form().formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1280, 1024));
        frame.pack();
        frame.setVisible(true);
    }
}
