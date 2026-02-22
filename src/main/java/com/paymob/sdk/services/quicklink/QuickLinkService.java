package com.paymob.sdk.services.quicklink;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Service for creating shareable payment links.
 * Pay-by-Link functionality for invoicing and quick payments.
 */
public class QuickLinkService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public QuickLinkService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Creates a shareable payment link.
     * @param request The quick link creation request
     * @return Payment link details
     */
    public QuickLinkResponse createPaymentLink(QuickLinkRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("amount_cents", String.valueOf(request.getAmountCents()))
                .addFormDataPart("payment_methods", String.valueOf(request.getPaymentMethods()))
                .addFormDataPart("is_live", String.valueOf(request.isLive()));

        if (request.getFullName() != null) {
            bodyBuilder.addFormDataPart("full_name", request.getFullName());
        }
        if (request.getCustomerEmail() != null) {
            bodyBuilder.addFormDataPart("email", request.getCustomerEmail());
        }
        if (request.getCustomerPhone() != null) {
            bodyBuilder.addFormDataPart("phone_number", request.getCustomerPhone());
        }
        if (request.getDescription() != null) {
            bodyBuilder.addFormDataPart("description", request.getDescription());
        }
        if (request.getReferenceId() != null) {
            bodyBuilder.addFormDataPart("reference_id", request.getReferenceId());
        }
        if (request.getNotificationUrl() != null) {
            bodyBuilder.addFormDataPart("notification_url", request.getNotificationUrl());
        }
        if (request.getExpiresAt() != null) {
            bodyBuilder.addFormDataPart("expires_at", request.getExpiresAt());
        }

        File image = request.getPaymentLinkImage();
        if (image != null) {
            MediaType mediaType = MediaType.get("application/octet-stream");
            RequestBody fileBody = RequestBody.create(image, mediaType);
            bodyBuilder.addFormDataPart("payment_link_image", image.getName(), fileBody);
        }

        RequestBody body = bodyBuilder.build();
        return httpClient.post("/api/ecommerce/payment-links", body, QuickLinkResponse.class, authStrategy);
    }

    public QuickLinkResponse cancel(int paymentLinkId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("payment_link_id", String.valueOf(paymentLinkId))
                .build();
        return httpClient.post("/api/ecommerce/payment-links/cancel", body, QuickLinkResponse.class, authStrategy);
    }
}
