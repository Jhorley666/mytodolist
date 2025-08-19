package com.bibavix.util.mapper;

import com.bibavix.dto.TaskDTO;
import com.bibavix.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true, dateFormat = "yyyy-MM-dd")
    @Mapping(target = "updatedAt", ignore = true, dateFormat = "yyyy-MM-dd")
    @Mapping(target = "dueDate", source = "dueDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "statusId", source = "statusId") // Long a Short
    @Mapping(target = "categoryId", source = "categoryId") // Long a Integer
    Task toEntity(TaskDTO taskDTO);

    @Mapping(target = "dueDate", source = "dueDate", dateFormat = "yyyy-MM-dd")
    TaskDTO toDTO(Task task);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true, dateFormat = "yyyy-MM-dd")
    @Mapping(target = "updatedAt", ignore = true, dateFormat = "yyyy-MM-dd")
    @Mapping(target = "dueDate", source = "dueDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "categoryId", source = "categoryId")
    void updateTaskFromDTO(TaskDTO taskDTO,@MappingTarget Task task);
}
