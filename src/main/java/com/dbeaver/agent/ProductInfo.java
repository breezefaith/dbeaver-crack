package com.dbeaver.agent;

import com.dbeaver.lm.api.LMLicense;
import com.dbeaver.lm.api.LMLicenseType;
import com.dbeaver.lm.api.LMProductType;

public record ProductInfo(String id, String prefix, String name, String description, String version,
                          LMProductType productType, LMLicenseType licenseType, Long flag) {
    public static final Long DESKTOP_FLAG = LMLicense.FLAG_UNLIMITED_USERS + LMLicense.FLAG_UNLIMITED_TIME + LMLicense.FLAG_UNLIMITED_SERVERS;
    public static final Long SERVER_FLAG = LMLicense.FLAG_ROLE_BASED + LMLicense.FLAG_SERVER_LICENSE + LMLicense.FLAG_UNLIMITED_USERS + LMLicense.FLAG_UNLIMITED_TIME + LMLicense.FLAG_UNLIMITED_SERVERS;
}
