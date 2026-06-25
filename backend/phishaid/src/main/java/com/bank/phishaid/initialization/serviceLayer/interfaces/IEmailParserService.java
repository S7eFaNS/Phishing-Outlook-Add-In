package com.bank.phishaid.initialization.serviceLayer.interfaces;

import com.bank.phishaid.initialization.dto.EmailDTO;

//Parses the raw MIME email into DTO
public interface IEmailParserService {
    EmailDTO Parse(String rawMail);
}
