package com.bank.phishaid.serviceLayer.initialization.interfaces;

public interface IOrchestrationInitService {

    //initialization of the email
    void ProcessInitializationMail(String hash, String rawEmail);
}
