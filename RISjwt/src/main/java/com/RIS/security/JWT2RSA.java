package com.RIS.security;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.InvalidKeyException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author DDT1
 */
public class JWT2RSA {

    public void testJWTWithRsa() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(2048);

        KeyPair kp = keyGenerator.genKeyPair();
        PublicKey publicKey = (PublicKey) kp.getPublic();
        PrivateKey privateKey = (PrivateKey) kp.getPrivate();

        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("Public Key:");
        System.out.println(convertToPublicKey(encodedPublicKey));
        String token = generateJwtToken(privateKey);
        System.out.println("TOKEN:");
        System.out.println(token);
        printStructure(token, publicKey);
    }

    @SuppressWarnings("deprecation")
    public String generateJwtToken(PrivateKey privateKey) {
        String token = Jwts.builder().setSubject("EL BERDUGO")
                .setExpiration(new Date(2023, 6, 1))
                .setIssuer("DVG@CORREO.com")
                .claim("groups", new String[]{"user", "admin"})
                .claim("APPS", new String[]{"RIS", "PACS"})
                // RS256 with privateKey
                .signWith(SignatureAlgorithm.RS256, privateKey).compact();
        return token;
/*
Instant now = Instant.now();
String jwtToken = Jwts.builder()
        .claim("name", "Jane Doe")
        .claim("email", "jane@example.com")
        .setSubject("jane")
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
        .signWith(hmacKey)
        .compact();        
        */ 
/*
String jws = Jwts.builder()
  .setIssuer("Stormpath")
  .setSubject("msilverman")
  .claim("name", "Micah Silverman")
  .claim("scope", "admins")
  // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
  .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
  // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
  .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
  .signWith(
    SignatureAlgorithm.HS256,
    TextCodec.BASE64.decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")
  )
  .compact();
*/
        
        
    }
    

    //Print structure of JWT
    public void printStructure(String token, PublicKey publicKey) {
        Jws parseClaimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
        System.out.println("Header     : " + parseClaimsJws.getHeader());
        System.out.println("Body       : " + parseClaimsJws.getBody());
        System.out.println("Signature  : " + parseClaimsJws.getSignature());
        
        //boolean valido=verify(publicKey,parseClaimsJws.getHeader().toString(),parseClaimsJws.getBody().toString(),parseClaimsJws.getSignature());
        //System.out.println("firma valida: "+valido);
    }
    
//Verify signature with public key (analizar la instancia el algoritmo: SHA256withRSA)        
   public boolean verify(PublicKey publicKey, String header, String payload, String signature) {
        try {
            String data = header + "." + payload;
            System.out.println("Probando validación");
            System.out.println(data);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());
            byte[] decodedSignature = Base64.getUrlDecoder().decode(signature);
            return sig.verify(decodedSignature);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
   }    

    // Add BEGIN and END comments
    private String convertToPublicKey(String key) {
        StringBuilder result = new StringBuilder();
        result.append("-----BEGIN PUBLIC KEY-----\n");
        result.append(key);
        result.append("\n-----END PUBLIC KEY-----");
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        JWT2RSA jwtrsa = new JWT2RSA();
        jwtrsa.testJWTWithRsa();
    }
}
