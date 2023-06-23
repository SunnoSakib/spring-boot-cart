package com.demo.cart.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * @author Md Mahmud Hasan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {

    @NotNull
    @Positive
    Long productId;

    @NotNull
    @Positive
    Float unit;

    @Positive
    Long customerId;

    String cartUUID;
}
