package utilities;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class JwtTokenBuilder {

    public  static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String key = "";
//        JwtTokenBuilder jwtTokenBuilder = new JwtTokenBuilder();
//        System.out.println(jwtTokenBuilder.createFBJwt(key));
    }

    public String createGoogleJwt(String key, String issue, String BaseURL, String endpoint) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String privateKeyString = key;
        final byte[] derKey = convertPemToDer(privateKeyString);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);

        final String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "RS256")
                .addClaims(Map.of("iss", issue))
                .addClaims(Map.of("scope", "https://www.googleapis.com/auth/drive"))
                .addClaims(Map.of("aud", BaseURL + endpoint))
//                .addClaims(Map.of("bodyHash", UtilityClass.getSha256Hash(rawJsonBodyString)))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return jws;

    }

    public String createFBJwt(String key, String xapitoken, String uri) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String privateKeyString = key;
        final byte[] derKey = convertPemToDer(privateKeyString);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);

        final String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(xapitoken)
                .addClaims(Map.of("uri", uri))
                .addClaims(Map.of("nonce", ThreadLocalRandom.current().nextInt(100, 100000000 + 1)))
//                .addClaims(Map.of("bodyHash", UtilityClass.getSha256Hash(rawJsonBodyString)))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return jws;
    }

    public String createFBJwt(String key, String xapitoken, String uri, String rawJsonBodyString) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String privateKeyString = key;
        final byte[] derKey = convertPemToDer(privateKeyString);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);

        final String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(xapitoken)
                .addClaims(Map.of("uri", uri))
                .addClaims(Map.of("nonce", ThreadLocalRandom.current().nextInt(100, 100000000 + 1)))
                .addClaims(Map.of("bodyHash", UtilityClass.getSha256Hash(rawJsonBodyString)))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(30)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return jws;
    }

    public String createPrimeJwt(String key, String xapitoken, String clientAPIToken, String uri, String rawJsonBodyString) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String privateKeyString = key;
        final byte[] derKey = convertPemToDer(privateKeyString);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(derKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);

        final String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(xapitoken)
                .addClaims(Map.of("uri", uri))
                .addClaims(Map.of("clientToken", clientAPIToken))
                .addClaims(Map.of("bodyHash", UtilityClass.getSha256Hash(rawJsonBodyString)))
                .setIssuedAt(Date.from(Instant.now()))
//                .setExpiration(Date.from(Instant.now().plusSeconds(30)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        return jws;
    }

    private byte[] convertPemToDer(String pem) {
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "");
        pem = pem.replace("-----END PRIVATE KEY-----", "");
//        System.out.println("\n"+pem);
        return Base64.getDecoder().decode(pem);
    }
}
