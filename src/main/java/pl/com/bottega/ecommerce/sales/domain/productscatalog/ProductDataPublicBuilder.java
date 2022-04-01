package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataPublicBuilder {
    private static ProductData productData = new ProductData(Id.generate(),new Money(1),"testName",ProductType.DRUG,new Date());

    public static ProductData getProductData() {
        return productData;
    }
}
