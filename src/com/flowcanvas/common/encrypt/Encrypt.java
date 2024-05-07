package com.flowcanvas.common.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Encrypt { 

    public static String generateHash(char[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(new String(data).getBytes());
            Arrays.fill(data, '0'); // 데이터 사용 후 배열 초기화
            
            return bytesToHex(encodedhash);
            
        } catch (NoSuchAlgorithmException e1) {
            System.err.println("암호화 알고리즘이 존재하지 않습니다: " + e1.getMessage());
            return null;
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}