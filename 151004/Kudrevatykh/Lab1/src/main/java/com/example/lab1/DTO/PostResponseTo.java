package com.example.lab1.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostResponseTo {
    int id;
    int storyId;
    @Size(min = 2, max = 2048)
    String content;
}