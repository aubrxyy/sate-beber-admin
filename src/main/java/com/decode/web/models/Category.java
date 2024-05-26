package com.decode.web.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String imageUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();
}