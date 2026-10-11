package com.backend.pdf_wizard.dao.repository.jpa;

import com.backend.pdf_wizard.dao.entity.PdfMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PdfDocumentRepository extends JpaRepository<PdfMetaData, UUID>, JpaSpecificationExecutor<PdfMetaData> {
    Optional<PdfMetaData> findByIdAndAccountId(UUID id, UUID accountId);

    List<PdfMetaData> findByAccountId(UUID accountId);
}
