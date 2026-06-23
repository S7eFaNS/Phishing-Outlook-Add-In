package com.bank.phishaid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelayHopDTO {
    private Integer hopNumber;
    private String hopDescription;
}
