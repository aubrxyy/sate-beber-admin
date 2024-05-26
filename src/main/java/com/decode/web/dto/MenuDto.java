package com.decode.web.dto;

import com.decode.web.models.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private Long id;
    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Price is required")
    private String price;
    private String description;
    private String imageUrl;
    private Category category;
}
