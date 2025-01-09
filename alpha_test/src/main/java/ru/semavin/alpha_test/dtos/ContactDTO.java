package ru.semavin.alpha_test.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для представления контакта")
public class ContactDTO {

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
            regexp = "^\\+7[0-9]{10}$",
            message = "Phone number must start with +7 and contain 10 digits"
    )
    @Schema(description = "Номер телефона клиента", example = "+79991234567")
    private String phone;

    @Email(message = "Email should be not empty")
    @Schema(description = "Электронная почта клиента", example = "example@mail.com")
    private String email;

    @NotNull
    @Schema(description = "Идентификатор клиента", example = "1")
    private Long client_id;
}
