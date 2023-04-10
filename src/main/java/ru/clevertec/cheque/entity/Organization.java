package ru.clevertec.cheque.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {
    private String name;
    private String address;
    private String email;
    private String telephone;
}
