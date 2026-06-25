package com.bank.phishaid.initialization.serviceLayer.interfaces;

import java.util.List;
import java.util.UUID;

import com.bank.phishaid.initialization.dto.BodyExtraction;
import com.bank.phishaid.initialization.dto.EmailDTO;


public interface IBodyInitService {

    //extracts both urls and attachments and saves them in separate arrays for the initialization
    BodyExtraction ExtractUrlsAndAttachments(EmailDTO dto);

    //Initialize urls
    void InitUrl(List<String> urls, UUID phMailId);

    //Initialize attachments
    void InitAttachments(List<String> attachmentHashes, UUID phMailId);
}
