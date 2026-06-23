package com.bank.phishaid.serviceLayer.initialization.interfaces;

import com.bank.phishaid.dto.BodyExtraction;
import com.bank.phishaid.dto.EmailDTO;

import java.util.List;
import java.util.UUID;


public interface IBodyInitService {

    //extracts both urls and attachments and saves them in separate arrays for the initialization
    BodyExtraction ExtractUrlsAndAttachments(EmailDTO dto);

    //Initialize urls
    void InitUrl(List<String> urls, UUID phMailId);

    //Initialize attachments
    void InitAttachments(List<String> attachmentHashes, UUID phMailId);
}
