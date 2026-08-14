package com.dbeaver.agent;

public record OwnerInfo(String id, String company, String name, String email) {
    public static final OwnerInfo DEFAULT = new OwnerInfo("080601", "DBeaver Corporation", "Ultimate User", "developer@dbeaver.com");
}
