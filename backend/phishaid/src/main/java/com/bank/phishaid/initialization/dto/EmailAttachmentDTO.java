package com.bank.phishaid.initialization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachmentDTO {
    private String fileName;
    private byte[] content;
}
