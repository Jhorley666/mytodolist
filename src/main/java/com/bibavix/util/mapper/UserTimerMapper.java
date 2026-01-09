package com.bibavix.util.mapper;

import com.bibavix.dto.UserTimerDTO;
import com.bibavix.model.UserTimer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserTimerMapper {
    @Mapping(target = "idUserTimer", ignore = true)
    UserTimer toEntity(UserTimerDTO userTimerDTO);

    UserTimerDTO toDTO(UserTimer userTimer);

    @Mapping(target = "idUserTimer", ignore = true)
    void updateUserTimerFromDTO(UserTimerDTO userTimerDTO, @MappingTarget UserTimer userTimer);
}
