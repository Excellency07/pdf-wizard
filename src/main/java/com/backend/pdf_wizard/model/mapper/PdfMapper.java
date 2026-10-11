package com.backend.pdf_wizard.model.mapper;

import com.backend.pdf_wizard.dao.entity.PdfMetaData;
import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.PdfResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PdfMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "storageName", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PdfMetaData toEntity(GeneratePdfRequestDto requestDto);


    PdfResponseDto toDto(PdfMetaData pdfMetaData);
}
