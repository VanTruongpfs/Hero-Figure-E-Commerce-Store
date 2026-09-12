package com.example.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoleRequest {

    @NotBlank(message = "Tên role không được để trống")
    @Size(max = 30, message = "Tên role tối đa 30 ký tự")
    @Pattern(
            regexp = "^[A-Za-z_]+$",
            message = "Tên role chỉ được chứa chữ cái và dấu gạch dưới"
    )
    private String name;
}
