package com.bank.phishaid.analysis.repository.interfaces;

import com.bank.phishaid.initialization.entity.AttachmentLst;

import java.util.List;
import java.util.UUID;

// Read-only
public interface IAnalysisAttReadRepo {

    // The attachments (attName hash) stored for this email (attachmentRL → attachmentLst),
    List<AttachmentLst> findAttNames(UUID phMailId);
}
