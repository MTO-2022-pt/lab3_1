package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataPublicBuilder {
    private static Money money = new Money(1);
    private static String name = "testName";
    private static ProductType productType = ProductType.DRUG;
    private static ProductData productData = new ProductData(Id.generate(),money,name,productType,new Date());

    public static ProductData getProductData() {
        return productData;
    }

    public static void setMoney(Money money) {
        ProductDataPublicBuilder.money = money;
    }

    public static void setName(String name) {
        ProductDataPublicBuilder.name = name;
    }

    public static void setProductType(ProductType productType) {
        ProductDataPublicBuilder.productType = productType;
    }

    public static void setProductData(ProductData productData) {
        ProductDataPublicBuilder.productData = productData;
    }
}
