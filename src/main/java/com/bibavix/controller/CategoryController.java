package com.bibavix.controller;

import com.bibavix.dto.CategoryDTO;
import com.bibavix.dto.ResponseCode;
import com.bibavix.model.User;
import com.bibavix.repository.UserRepository;
import com.bibavix.service.CategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "Categories management APIs")
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String TASK_NOT_FOUND = "Category not found";
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        Integer categoryId = categoryService.addCategory(categoryDTO, user.getUserId());
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesByUserId(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategoriesByUserId(user.getUserId());
        return ResponseEntity.ok(categoryDTOS);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseCode> deleteCategory(@Parameter(description = "Category ID", required = true) @PathVariable Integer id,
                                                       @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(id);
        categoryDTO.setUserId(user.getUserId());
        categoryService.deleteCategory(categoryDTO);
        return ResponseEntity.ok().build();
    }
}
