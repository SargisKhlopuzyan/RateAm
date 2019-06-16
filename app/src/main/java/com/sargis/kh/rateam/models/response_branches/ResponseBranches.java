package com.sargis.kh.rateam.models.response_branches;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class ResponseBranches implements Serializable {

    @SerializedName("date") public Float date;

    @SerializedName("list")
    @Expose
    public Map<String, Branch> branches;

}