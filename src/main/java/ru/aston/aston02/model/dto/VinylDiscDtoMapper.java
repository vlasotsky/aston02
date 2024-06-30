package ru.aston.aston02.model.dto;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import ru.aston.aston02.model.VinylDisc;
import ru.aston.aston02.util.DtoMapperUtil;

import java.util.List;

@Mapper
public interface VinylDiscDtoMapper {
    default VinylDiscDto toDto(VinylDisc disc, @Context List<VinylDisc> discList) {
        return DtoMapperUtil.getDto(disc, discList);
    }

    default List<VinylDiscDto> toDtoAll(List<VinylDisc> discList) {
        return DtoMapperUtil.getAllDto(discList);
    }

    VinylDisc toEntity(VinylDiscDto discDto);
}
