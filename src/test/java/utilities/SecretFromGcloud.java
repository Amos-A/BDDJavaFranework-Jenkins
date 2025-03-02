package utilities;

import com.google.cloud.secretmanager.v1.*;

import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

public class SecretFromGcloud {

    public  static String accessSecretVersion(String projectId, String secretId, String versionId) throws IOException {

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            byte[] data = response.getPayload().getData().toByteArray();
            Checksum checksum = new CRC32C();
            checksum.update(data, 0, data.length);
            if (response.getPayload().getDataCrc32C() != checksum.getValue())
                System.out.println("Data corruption detected.");
            String payload = response.getPayload().getData().toStringUtf8();
//            System.out.printf("Plaintest: %s\n", payload);
            return payload;
        }
    }
}
