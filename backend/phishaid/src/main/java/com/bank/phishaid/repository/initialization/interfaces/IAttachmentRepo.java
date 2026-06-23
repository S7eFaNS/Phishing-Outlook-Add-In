package com.bank.phishaid.repository.initialization.interfaces;

import java.util.UUID;

public interface IAttachmentRepo {

    UUID Create(String attName);

    //Links an attachmentLst row to a phMail by inserting a row into attachmentRL.
    void CreateJunctionColumn(UUID phMailId, UUID attLstId);
}
