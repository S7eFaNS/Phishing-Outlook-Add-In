package com.bank.phishaid.initialization.serviceLayer.interfaces;

public interface IOrchestrationInitService {

    //initialization of the email
    void ProcessInitializationMail(String hash, String rawEmail);
}
