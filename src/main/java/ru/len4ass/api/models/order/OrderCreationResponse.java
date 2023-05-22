package ru.len4ass.api.models.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCreationResponse {
    @JsonProperty("info")
    private String infoMessage;

    @JsonProperty("order_id")
    private Integer orderId;

    public OrderCreationResponse(String infoMessage, Integer orderId) {
        this.infoMessage = infoMessage;
        this.orderId = orderId;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public Integer getOrderId() {
        return orderId;
    }
}
