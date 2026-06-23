package com.bank.phishaid.repository.initialization.interfaces;

import com.bank.phishaid.entity.MailPathLst;
import com.bank.phishaid.entity.MailRelayLst;
import com.bank.phishaid.entity.MailRelayRL;

public interface IMailRelayRepo {

    // Persists a single relay hop and returns it with its generated id populated
    
    MailRelayLst Create(MailRelayLst mailRelay);

    //Links a persisted relay hop to its receiving path by inserting a row into mailRelayRL

    MailRelayRL CreateJunctionColumn(MailPathLst mailPath, MailRelayLst mailRelay);
}
