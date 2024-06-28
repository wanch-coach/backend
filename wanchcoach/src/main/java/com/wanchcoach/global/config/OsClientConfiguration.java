package com.wanchcoach.global.config;

import java.io.IOException;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OsClientConfiguration {
    String configurationFilePath = "wanchcoach/src/main/java/com/wanchcoach/global/config/bucketConfig";
    String profile = "DEFAULT";

    public ObjectStorage getObjectStorage() throws IOException {

        //load config file
        final ConfigFileReader.ConfigFile
                configFile = ConfigFileReader
                .parse(configurationFilePath, profile);

        final ConfigFileAuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);

        //build and return client
        return ObjectStorageClient.builder()
                .build(provider);
    }
}
