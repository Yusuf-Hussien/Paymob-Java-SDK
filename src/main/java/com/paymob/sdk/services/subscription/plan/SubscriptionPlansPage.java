package com.paymob.sdk.services.subscription.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Paginated response for subscription plans.
 */
public class SubscriptionPlansPage {

    @JsonProperty("next")
    private String next;

    @JsonProperty("previous")
    private String previous;

    @JsonProperty("results")
    private List<SubscriptionPlanResponse> results;

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

    public List<SubscriptionPlanResponse> getResults() {
        return results;
    }

    public void setResults(List<SubscriptionPlanResponse> results) {
        this.results = results;
    }
}
