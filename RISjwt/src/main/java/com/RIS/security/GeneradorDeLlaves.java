/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.RIS.security;

import com.nimbusds.jose.JOSEException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
/**
 *
 * @author MARCO ANTONIO VELASCO
 */
public class GeneradorDeLlaves {
    
    private RSAKey JWKeyPrv, JWKeyPub;
  
    
    public GeneradorDeLlaves() throws NoSuchAlgorithmException, JOSEException{
        KeyPairGenerator KeyGenerator = KeyPairGenerator.getInstance("RSA");
        KeyGenerator.initialize(2048);
        KeyPair kp = KeyGenerator.genKeyPair();
        
        RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
        
        JWKeyPub = new RSAKey.Builder(rsaPublicKey).build();
        JWKeyPrv = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
        
        System.out.println("La llave publica es" + JWKeyPub.toPublicKey());
        System.out.println("La llave privada es" + JWKeyPrv.toPrivateKey());
        System.out.println("La llave publica es" + JWKeyPrv.toPublicKey());
        System.out.println("publica es" + rsaPublicKey);
        System.out.println("privada es" + rsaPrivateKey);
    }
     
    public static void main(String[] args) throws Exception {
        GeneradorDeLlaves pruebagenerador = new GeneradorDeLlaves();
    }
    
}
