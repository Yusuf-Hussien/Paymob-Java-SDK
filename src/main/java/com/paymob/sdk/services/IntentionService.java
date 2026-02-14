package com.paymob.sdk.services;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.intention.IntentionRequest;
import com.paymob.sdk.models.intention.IntentionResponse;

public class IntentionService {
    private final PaymobClient client;

    public IntentionService() {
        this(new PaymobClient());
    }

    public IntentionService(PaymobClient client) {
        this.client = client;
    }

    public IntentionResponse createIntention(IntentionRequest request) {
        return client.postWithSecretKey("/v1/intention/", request, IntentionResponse.class);
    }

    public IntentionResponse retrieveIntention(String clientSecret) {
        // The retrieve endpoint pattern seems to be GET /v1/intention/{client_secret}
        // based on some docs or pattern,
        // but the Postman collection shows: GET
        // /v1/intention/element/{Public_key}/{client_secret}/
        // Let's implement that one as it's explicit in the collection for "Retrieve
        // intention".
        String publicKey = Paymob.getPublicKey();
        return client.get("/v1/intention/element/" + publicKey + "/" + clientSecret + "/", IntentionResponse.class);
    }

    /**
     * Generates the Unified Checkout URL for the client.
     * 
     * @param clientSecret The client secret obtained from createIntention.
     * @return The URL to redirect the user to.
     */
    public String getUnifiedCheckoutUrl(String clientSecret) {
        String publicKey = Paymob.getPublicKey();
        String checkoutUrl = Paymob.getCheckoutUrl();
        return checkoutUrl + "/?publicKey=" + publicKey + "&clientSecret=" + clientSecret;
    }
}
