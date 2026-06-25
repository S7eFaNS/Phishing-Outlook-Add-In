package com.bank.phishaid.initialization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    private EmailHeaderDTO head;
    private String body;
    private List<EmailAttachmentDTO> attachments;
}
