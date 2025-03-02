package utilities;

import com.google.cloud.kms.v1.AsymmetricSignResponse;
import com.google.cloud.kms.v1.CryptoKeyVersionName;
import com.google.cloud.kms.v1.Digest;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.protobuf.ByteString;
import io.restassured.response.Response;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;

public class Tokens {

    private Encoder<byte[], String> base64UrlEncoder = Encoders.BASE64URL;
    public static final String JWT_ISSUER = "BOOMERANG";
    public static final Long JWT_EXPIRATION = 6000L;

    public String getKORToken(String baseUrl, String clientId, String clientSecret) {
        baseURI = baseUrl;
        Response res = given().relaxedHTTPSValidation()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when().post("oauth/token")
                .then().assertThat().statusCode(200)
                .extract().response();
        String responseString = res.asString();
        String token = UtilityClass.getJsonParameterByPath(responseString, "access_token");
        System.out.println("Access token for KOR successfully generated " + token + "\n\n");
        return token;
    }


//    public String getKMSToken(String aud, String issue) {
//        final String projectId = "service-nebula-kms-testc90b48b";
//        final String locationId = "global";
//        final String keyRingId = "service-nebula-kms-test-nebul-kms";
//        final String keyId = "service-nebula-kms-test-nebula-auth-0";
//        final String keyVersionId = "1";
//        final String message = getMessage(issue, aud);
//
//        return signAsymmetric(projectId, locationId, keyRingId, keyId, keyVersionId, message);
//    }

    public String signAsymmetric(final String projectId, final String locationId, final String keyRingId, final String keyId, final String keyVersionId, final String message) {

        try (KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            final CryptoKeyVersionName keyVersionName = CryptoKeyVersionName.of(projectId, locationId, keyRingId, keyId, keyVersionId);
            final byte[] plaintext = message.getBytes(StandardCharsets.UTF_8);

            final MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            final byte[] hash = sha512.digest(plaintext);

            final Digest digest = Digest.newBuilder().setSha512(ByteString.copyFrom(hash)).build();

            final AsymmetricSignResponse result = client.asymmetricSign(keyVersionName, digest);
            final byte[] signature = result.getSignature().toByteArray();

            client.close();
            return message + "." + base64UrlEncoder.encode(signature);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    private String getMessage(String issuer, String aud){
//
////        final String jws = Jwts.builder()
////                .setHeaderParam("typ", "JWT")
////                .setIssuer(issuer)
////                .setAudience(aud)
////                .setIssuedAt(Date.from(Instant.now()))
////                .setNotBefore(Date.from(Instant.now()))
////                .setExpiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION)))
////                .claim("data", "")
////                .compact();
////        return StringUtils.subString(jws, 0, jws.length() -1);
//
//    }

    public String generateGoogleAccessToken(String baseUrl, String endpoint, String grantType, String jwtToken){
        baseURI = baseUrl;
        Response response = given().relaxedHTTPSValidation()
                .queryParam("grant_type", grantType)
                .queryParam("assertion", jwtToken)
                .when().post(endpoint)
                .then().assertThat().statusCode(200)
                .extract().response();
        String responseString = response.asString();
        String token = UtilityClass.getJsonParameterByPath(responseString, "access_token");
        System.out.println("Access token for Google sheets successfully generated " + token + "\n\n");
        return token;
    }
}