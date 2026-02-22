package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubscriptionTransactionsPage {

    @JsonProperty("next")
    private String next;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("results")
    private List<SubscriptionTransactionResponse> results;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<SubscriptionTransactionResponse> getResults() {
        return results;
    }

    public void setResults(List<SubscriptionTransactionResponse> results) {
        this.results = results;
    }
}
