package com.backend.pdf_wizard.service.storageServices;

import com.backend.pdf_wizard.exception.StorageException;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void initBucket() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize MinIO bucket", e);
        }
    }

    @Override
    public void upload(String storageName, InputStream inputStream, long size, String contentType) {
        initBucket();
        try {
            PutObjectArgs object = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storageName)
                    .stream(inputStream, size, -1L)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(object);
        } catch (MinioException e) {
            throw new StorageException("Failed to upload file");
        }
    }

    @Override
    public InputStream download(String StorageName, String originalFilename) {
        try {
            GetObjectArgs object = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(StorageName)
                    .build();
            return minioClient.getObject(object);
        } catch (Exception e) {
            throw new StorageException("Failed to download file");
        }
    }

    @Override
    public void delete(String StorageName) {
        try {
            RemoveObjectArgs object = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(StorageName)
                    .build();
            minioClient.removeObject(object);
        } catch (Exception e) {
            throw new StorageException("Failed to delete file");
        }
    }
}