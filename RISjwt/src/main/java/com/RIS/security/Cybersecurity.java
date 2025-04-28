
package com.RIS.security;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Date;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.ws.rs.core.MultivaluedMap;

public class Cybersecurity {
    
    private String llavePublica;
    private RSAPrivateKey privateKey;
    
    public Cybersecurity(){
        
    }
    
    public void generarLlaves() throws NoSuchAlgorithmException{
        
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        llavePublica = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded()).replaceAll("(.{64})", "$1\n") +
                "\n-----END PUBLIC KEY-----";
    }
    
    public String getLlavePublica(){
        
        return llavePublica;
    }
    
    public String encriptarDatos(String Datos){
        
        String token = Jwts.builder()
                .setIssuer("Marco Antonio Velasco")
                .setSubject(Datos)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(privateKey, SignatureAlgorithm.RS256) // Firmar con la clave privada
                .compact();  
        return token;
    }  
    public Claims desencriptarDatos(String llavepublica,String Token) throws NoSuchAlgorithmException, InvalidKeySpecException{
        
        System.out.println("El token es:"+ Token);
        String publicKeyPEMFormatted = llavepublica.replace("-----BEGIN PUBLIC KEY-----", "")
                                                       .replace("-----END PUBLIC KEY-----", "")
                                                       .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEMFormatted);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Verificar y obtener los claims
        Claims claims = Jwts.parser()
            .setSigningKey(publicKey)
            .parseClaimsJws(Token)
            .getBody();
        
        return claims;
    }
    
    
    
    
}
