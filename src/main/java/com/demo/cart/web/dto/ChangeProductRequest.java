package com.demo.cart.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Md Mahmud Hasan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductRequest {
    @NotNull
    @Positive
    Long productId;

    @NotNull
    @Positive
    Float newUnit;

    @NotNull
    String cartUUID;
}
