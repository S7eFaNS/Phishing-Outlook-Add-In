package com.bank.phishaid.repository.initialization.interfaces;

import java.util.UUID;

public interface ILinkRepo {

    UUID Create(String linkUrl);

    // Links a linkLst row to a phMail by inserting a row into linkRL
    void CreateJunctionColumn(UUID phMailId, UUID linkLstId);
}
