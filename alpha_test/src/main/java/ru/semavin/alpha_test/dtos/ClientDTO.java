package ru.semavin.alpha_test.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для представления клиента")
public class ClientDTO {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    @Schema(description = "Имя клиента", example = "Иван", maxLength = 50)
    private String name;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Schema(description = "Фамилия клиента", example = "Иванов", maxLength = 50)
    private String lastName;

    @NotEmpty(message = "Contacts list must not be empty")
    @Schema(description = "Список контактов клиента")
    private List<@Valid ContactDTO> contacts;
}