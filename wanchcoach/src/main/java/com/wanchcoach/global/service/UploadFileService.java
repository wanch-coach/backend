package com.wanchcoach.global.service;

import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;
import com.wanchcoach.global.config.OsClientConfiguration;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadFileService {

    private final OsClientConfiguration configuration;
    private final String bucketName = "bucket-20240609-2259";
    private final String namespaceName = "axor3mocw47x";

    public void uploadPrescriptionImage(MultipartFile file, String fileName) throws Exception {

        log.info("fileName: "+ fileName);
        InputStream inputStream = file.getInputStream();

        //build upload request
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(fileName)
                .contentLength(file.getSize())
                .putObjectBody(inputStream)
                .build();

        configuration.getObjectStorage().putObject(putObjectRequest);
        configuration.getObjectStorage().close();
    }

    public CreatePreauthenticatedRequestResponse getFileObject(String fileName) throws Exception {

        // build request details
        CreatePreauthenticatedRequestDetails createPreauthenticatedRequestDetails =
                CreatePreauthenticatedRequestDetails.builder()
                        .name("request")
                        .bucketListingAction(PreauthenticatedRequest.BucketListingAction.Deny)
                        .objectName(fileName)
                        .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectRead)
                        .timeExpires(Date.from(Instant.now().plusSeconds(3600))).build();

        // build request
        CreatePreauthenticatedRequestRequest createPreauthenticatedRequestRequest =
                CreatePreauthenticatedRequestRequest.builder()
                        .namespaceName(namespaceName)
                        .bucketName(bucketName)
                        .createPreauthenticatedRequestDetails(createPreauthenticatedRequestDetails)
                        .opcClientRequestId(UUID.randomUUID().toString()).build();

        // send request to oci
        CreatePreauthenticatedRequestResponse createPreauthenticatedRequestResponse =
                configuration.getObjectStorage().createPreauthenticatedRequest(createPreauthenticatedRequestRequest);
        configuration.getObjectStorage().close();
        return createPreauthenticatedRequestResponse;
    }
}
