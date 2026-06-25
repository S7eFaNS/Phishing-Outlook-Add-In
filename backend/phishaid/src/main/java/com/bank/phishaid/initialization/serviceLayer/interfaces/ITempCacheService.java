package com.bank.phishaid.initialization.serviceLayer.interfaces;


public interface ITempCacheService {

    //persists the temp table till completion
    void Save(String hash, String rawMail);

    //drops upon completion
    void Drop(String hash);

    //retry on failure to initialize
    void Retry(String hash);
}
