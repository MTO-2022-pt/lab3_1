package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id productId = new Id("testId");
    private Money price = new Money(100);
    private String name = "testProduct";
    private Date snapshotDate = new Date();
    private ProductType type = ProductType.FOOD;

    public ProductDataBuilder withID(Id id){
        this.productId = id;
        return this;
    }

    public ProductDataBuilder withPrice(Money price){
        this.price = price;
        return this;
    }

    public ProductDataBuilder withName(String name){
        this.name = name;
        return this;
    }

    public ProductDataBuilder withSnapshotDate(Date date){
        this.snapshotDate = date;
        return this;
    }

    public ProductDataBuilder withType(ProductType type){
        this.type = type;
        return this;
    }

    public ProductData build(){
        return new ProductData(this.productId, this.price, this.name, this.type, this.snapshotDate);
    }
}
