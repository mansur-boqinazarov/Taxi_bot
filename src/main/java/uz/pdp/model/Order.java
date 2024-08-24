package uz.pdp.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private String direction;
    private String numberOfPeople;
    private String phoneNumber;
}
