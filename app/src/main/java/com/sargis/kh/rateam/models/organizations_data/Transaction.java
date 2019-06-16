package com.sargis.kh.rateam.models.organizations_data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transaction implements Serializable {

    @SerializedName("buy") public Float buy;

    @SerializedName("sell") public Float sell;
}