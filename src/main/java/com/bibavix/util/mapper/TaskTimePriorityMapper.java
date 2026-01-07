package com.bibavix.util.mapper;

import com.bibavix.dto.TaskTimePriorityDTO;
import com.bibavix.model.TaskTimePriority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskTimePriorityMapper {
    @Mapping(target = "taskTimePriorityId", ignore = true)
    @Mapping(target = "time", source = "time")
    @Mapping(target = "priorityId", source = "priorityId")
    TaskTimePriority toEntity(TaskTimePriorityDTO taskTimePriorityDTO);

    @Mapping(target = "time", source = "time")
    TaskTimePriorityDTO toDTO(TaskTimePriority taskTimePriority);

    @Mapping(target = "taskTimePriorityId", ignore = true)
    @Mapping(target = "time", source = "time")
    @Mapping(target = "priorityId", source = "priorityId")
    void updateTaskTimePriorityFromDTO(TaskTimePriorityDTO taskTimePriorityDTO,
                                       @MappingTarget TaskTimePriority taskTimePriority);
}
