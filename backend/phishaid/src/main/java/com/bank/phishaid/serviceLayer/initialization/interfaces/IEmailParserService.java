package com.bank.phishaid.serviceLayer.initialization.interfaces;

import com.bank.phishaid.dto.EmailDTO;

//Parses the raw MIME email into DTO
public interface IEmailParserService {
    EmailDTO Parse(String rawMail);
}
