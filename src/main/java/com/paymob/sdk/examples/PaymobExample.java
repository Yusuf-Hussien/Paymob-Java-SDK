package com.paymob.sdk.examples;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.models.enums.LogLevel;

/**
 * Example usage of the refactored Paymob Java SDK.
 * Demonstrates all major API groups and features.
 */
public class PaymobExample {

    public static void main(String[] args) {
        // 1. Configure the SDK
        PaymobConfig config = PaymobConfig.builder()
                .secretKey("sk_test_...")
                .apiKey("ak_test_...")
                .publicKey("pk_test_...")
                .hmacSecret("hmac_secret_...")
                .region(PaymobRegion.EGYPT)
                .timeout(30)
                .logLevel(LogLevel.BODY)
                .build();

        // 2. Create the client (Note: HTTP client implementation needed)
        // PaymobClient client = PaymobClient.builder()
        // .config(config)
        // .httpClient(new OkHttpClientAdapter())
        // .build();

        // Example usage patterns (commented out until HTTP client is implemented):

        /*
         * // 3. Create a payment intention
         * BillingData billingData = new BillingData("John", "Doe", "john@example.com",
         * "+201234567890");
         * Item item1 = new Item("Product 1", 5000, "Description 1", 1); // 50.00 EGP
         * Item item2 = new Item("Product 2", 3000, "Description 2", 2); // 60.00 EGP
         * total
         * 
         * IntentionRequest intentionRequest = new IntentionRequest(11000, Currency.EGP,
         * Arrays.asList(item1, item2), billingData);
         * intentionRequest.setPaymentMethods(Arrays.asList(12345, 67890)); //
         * Integration IDs
         * intentionRequest.setSpecialReference("order-12345");
         * 
         * IntentionResponse intentionResponse =
         * client.intentions().createIntention(intentionRequest);
         * String checkoutUrl =
         * client.intentions().getUnifiedCheckoutUrl(intentionResponse.getClientSecret()
         * );
         * 
         * // 4. Process saved card payment
         * // CIT (Customer Initiated)
         * CitPaymentRequest citRequest = new CitPaymentRequest("card_token_123", "123",
         * 10000, "EGP");
         * TokenizedPaymentResponse citResponse =
         * client.savedCards().processCitPayment(citRequest);
         * 
         * // MIT (Merchant Initiated)
         * MitPaymentRequest mitRequest = new MitPaymentRequest("card_token_123", 10000,
         * "EGP", "order-67890");
         * TokenizedPaymentResponse mitResponse =
         * client.savedCards().processMitPayment(mitRequest);
         * 
         * // 5. Create subscription plan
         * PlanRequest planRequest = new PlanRequest("Monthly Plan",
         * BillingCycle.MONTHLY, 5000);
         * planRequest.setDescription("Monthly subscription plan");
         * planRequest.setRetrialCount(3);
         * SubscriptionResponse planResponse =
         * client.subscriptions().createPlan(planRequest);
         * 
         * // 6. Create customer subscription
         * SubscriptionRequest subRequest = new
         * SubscriptionRequest(planResponse.getPlanId(), "customer@example.com",
         * "+201234567890");
         * subRequest.setCardToken("card_token_123");
         * SubscriptionResponse subResponse =
         * client.subscriptions().createSubscription(subRequest);
         * 
         * // 7. Transaction operations
         * RefundRequest refundRequest = new RefundRequest(123456789L, 5000);
         * TransactionResponse refundResponse =
         * client.transactions().refundTransaction(refundRequest);
         * 
         * VoidRequest voidRequest = new VoidRequest(123456789L);
         * TransactionResponse voidResponse =
         * client.transactions().voidTransaction(voidRequest);
         * 
         * CaptureRequest captureRequest = new CaptureRequest(123456789L, 10000);
         * TransactionResponse captureResponse =
         * client.transactions().captureTransaction(captureRequest);
         * 
         * // 8. Transaction inquiry
         * InquiryResponse inquiryByMerchant =
         * client.inquiry().byMerchantOrderId("order-12345");
         * InquiryResponse inquiryByTransaction =
         * client.inquiry().byTransactionId(123456789L);
         * 
         * // 9. Create payment link
         * QuickLinkRequest linkRequest = new QuickLinkRequest(10000, 12345, false);
         * linkRequest.setCustomerEmail("customer@example.com");
         * linkRequest.setCustomerPhone("+201234567890");
         * QuickLinkResponse linkResponse =
         * client.quickLinks().createPaymentLink(linkRequest);
         * 
         * // 10. Webhook validation
         * WebhookValidator validator = new WebhookValidator("hmac_secret_...");
         * String payload = "{\"transaction_id\":123456,\"success\":true}";
         * String signature = "received_signature_from_header";
         * boolean isValid = validator.validateSignature(payload, signature);
         * 
         * if (isValid) {
         * WebhookEvent event = validator.validateAndParse(payload, signature);
         * // Process the webhook event
         * }
         */

        System.out.println("Paymob SDK refactored structure created successfully!");
        System.out.println("All services and models are ready for HTTP client implementation.");
    }
}
