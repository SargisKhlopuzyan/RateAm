package com.sargis.kh.rateam.helpers;


import com.sargis.kh.rateam.models.organizations_data.Organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HelperOrganization {

    public static List<Organization> getOrganizationList(Map<String, Organization> organizationMap) {

        if (organizationMap == null)
            return new ArrayList<>();

        List<Organization> organizationList = new ArrayList(organizationMap.values());
        return organizationList;
    }

    public static List<String> getOrganizationIdList(Map<String, Organization> organizationMap) {

        if (organizationMap == null)
            return new ArrayList<>();

        List<String> organizationIdList = new ArrayList(organizationMap.keySet());
        return organizationIdList;
    }
}