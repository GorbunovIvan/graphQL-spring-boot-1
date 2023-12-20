package org.example.model;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode
@ToString
public class Profile {
    private Integer id;
    private Integer customerId;
}
