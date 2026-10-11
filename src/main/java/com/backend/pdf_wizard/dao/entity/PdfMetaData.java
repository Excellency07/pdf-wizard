package com.backend.pdf_wizard.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pdf_metadata")
public class PdfMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_name", unique = true)
    private String filename;

    @Column(name = "storage_name", nullable = false, unique = true)
    private String storageName;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "account_id")
    private UUID accountId;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public PdfMetaData(String filename, String storageName, long fileSize, UUID accountId) {
        this.filename = filename;
        this.storageName = storageName;
        this.fileSize = fileSize;
        this.accountId = accountId;
    }
}
