package com.bank.phishaid.initialization.repository.interfaces;

import com.bank.phishaid.initialization.entity.MailPathLst;
import com.bank.phishaid.initialization.entity.MailRelayLst;
import com.bank.phishaid.initialization.entity.MailRelayRL;

public interface IMailRelayRepo {

    // Persists a single relay hop and returns it with its generated id populated
    
    MailRelayLst Create(MailRelayLst mailRelay);

    //Links a persisted relay hop to its receiving path by inserting a row into mailRelayRL

    MailRelayRL CreateJunctionColumn(MailPathLst mailPath, MailRelayLst mailRelay);
}
