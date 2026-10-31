package com.dbeaver.agent;

import com.dbeaver.lm.api.LMLicenseType;
import com.dbeaver.lm.api.LMProductType;

import java.util.Scanner;
import java.util.Set;

public class Main {
    public static Set<String> productIds = Set.of("dbeaver-ue", "dbeaver-ee", "dbeaver-te");
    public static Set<String> productVersions = Set.of("26.0", "26.1", "26.2");

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.printf("Product Id %s：", productIds);
            String productId = scanner.nextLine().trim();
            if (!productIds.contains(productId)) {
                throw new IllegalArgumentException("Invalid Product ID");
            }
            System.out.printf("Product Version %s: ", productVersions);
            String productVersion = scanner.nextLine().trim();
            if (!productVersions.contains(productVersion)) {
                throw new IllegalArgumentException("Invalid Product Version");
            }
            ProductInfo productInfo = switch (productId) {
                case "dbeaver-ue" ->
                        new ProductInfo("dbeaver-ue", "DB", "DBeaver Ultimate " + productVersion, "DBeaver Ultimate Edition", productVersion, LMProductType.DESKTOP, LMLicenseType.ULTIMATE, ProductInfo.DESKTOP_FLAG);
                case "dbeaver-ee" ->
                        new ProductInfo("dbeaver-ee", "DB", "DBeaver Enterprise " + productVersion, "DBeaver Enterprise Edition", productVersion, LMProductType.DESKTOP, LMLicenseType.ULTIMATE, ProductInfo.DESKTOP_FLAG);
                case "dbeaver-te" ->
                        new ProductInfo("cloudbeaver-dc", "DB", "DBeaver Team " + productVersion, "DBeaver Team Edition", productVersion, LMProductType.SERVER, LMLicenseType.TEAM, ProductInfo.SERVER_FLAG);
                default -> null;
            };
            OwnerInfo ownerInfo = OwnerInfo.DEFAULT;
            System.out.print("Custom Owner Info? [yN]");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("y")) {
                System.out.printf("Owner Id: [%s]", ownerInfo.id());
                String id = scanner.nextLine().trim();
                if (id.isEmpty()) {
                    throw new IllegalArgumentException("Invalid Owner ID");
                }
                System.out.printf("Owner Company: [%s]", ownerInfo.company());
                String company = scanner.nextLine().trim();
                if (company.isEmpty()) {
                    throw new IllegalArgumentException("Invalid Owner Company");
                }
                System.out.printf("Owner Name: [%s]", ownerInfo.name());
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Invalid Owner Name");
                }
                System.out.printf("Owner Email: [%s]", ownerInfo.email());
                String email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    throw new IllegalArgumentException("Invalid Owner Email");
                }
                ownerInfo = new OwnerInfo(id, company, name, email);
            }
            License.generateLicense(productInfo, ownerInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}