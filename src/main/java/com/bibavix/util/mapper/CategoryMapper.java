package com.bibavix.util.mapper;

import com.bibavix.dto.CategoryDTO;
import com.bibavix.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "name", source = "name")
        @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd")
    Category toEntity(CategoryDTO categoryDTO);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd")
    CategoryDTO toDTO(Category category);

    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "createdAt", ignore = true)
    void updateCategoryFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);
}