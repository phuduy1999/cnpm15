/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Encrypt;

import java.io.UnsupportedEncodingException;

import org.apache.axis.encoding.Base64;

/**
 *
 * @author Administrator
 */
public class MaHoaPassword {

    // Mã hóa một đoạn text
    // Encode
    public static String encodeString(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("UTF-8");
        String encodeString = Base64.encode(bytes);
        return encodeString;
    }

    // Giải mã hóa một đoạn text (Đã mã hóa trước đó).
    // Decode
    public static String decodeString(String encodeText) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.decode(encodeText);
        String str = new String(decodeBytes, "UTF-8");
        return str;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = "456";

        System.out.println("Text before encode: " + text);

        String encodeText = encodeString(text);
        System.out.println("Encode text: " + encodeText);

        String decodeText = decodeString(encodeText);
        System.out.println("Decode text: " + decodeText);
    }
}
