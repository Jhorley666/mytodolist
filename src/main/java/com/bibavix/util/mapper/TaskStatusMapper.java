package com.bibavix.util.mapper;

import com.bibavix.dto.TaskStatusDTO;
import com.bibavix.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskStatusMapper {

    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "name", source = "name")
    TaskStatus toEntity(TaskStatusDTO taskStatusDTO);

    TaskStatusDTO toDTO(TaskStatus taskStatus);

    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateTaskStatusFromDTO(TaskStatusDTO taskStatusDTO, @MappingTarget TaskStatus taskStatus);
}
