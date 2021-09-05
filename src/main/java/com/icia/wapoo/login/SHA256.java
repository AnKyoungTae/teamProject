package com.icia.wapoo.login;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;


@Component
public class SHA256 {

	public String hashCode(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        
        return bytesToHex(md.digest());
    }

	
    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        
        
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
            
        }
       
        return builder.toString();
    }

	
    
	
}
