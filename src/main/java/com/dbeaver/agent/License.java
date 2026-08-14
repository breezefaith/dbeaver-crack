package com.dbeaver.agent;

import com.dbeaver.lm.api.LMEncryption;
import com.dbeaver.lm.api.LMLicense;
import com.dbeaver.lm.api.LMProduct;
import com.dbeaver.lm.api.LMUtils;
import org.jkiss.utils.Base64;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Date;

public class License {
    public static void generateLicense(ProductInfo productInfo, OwnerInfo ownerInfo) throws Exception {
        if (productInfo == null) {
            throw new IllegalArgumentException("ProductInfo Required");
        }
        if (ownerInfo == null) {
            ownerInfo = OwnerInfo.DEFAULT;
        }
        LMProduct TEST_PRODUCT = new LMProduct(
                productInfo.id(),
                productInfo.prefix(),
                productInfo.name(),
                productInfo.description(),
                productInfo.version(),
                productInfo.productType(),
                new Date(),
                new String[0]
        );
        String licenseID = LMUtils.generateLicenseId(TEST_PRODUCT);
        LMLicense license = new LMLicense(
                licenseID,
                productInfo.licenseType(),
                new Date(),
                new Date(),
                null,
                productInfo.flag(),
                productInfo.id(),
                productInfo.version(),
                ownerInfo.id(),
                ownerInfo.company(),
                ownerInfo.name(),
                ownerInfo.email()
        );
        byte[] licenseData = license.getData();
        byte[] licenseEncrypted = LMEncryption.encrypt(licenseData, (new Cipher()).getPrivateKey());
        String licenseContent = Base64.splitLines(Base64.encode(licenseEncrypted), 76);
        System.out.println("\n--- LICENSE ---");
        System.out.println(licenseContent);
        System.out.println("--- Generation completed. Please open the software and use the above license (copied to the clipboard) ---");
        copyToClipboard(licenseContent);
    }

    /**
     * 将指定字符串复制到剪切板
     *
     * @param text 待复制的文本
     */
    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
