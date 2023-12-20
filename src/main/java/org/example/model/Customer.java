package org.example.model;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class Customer {
    private Integer id;
    private String name;
}
