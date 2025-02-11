package main.models;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "product"
})
public class Products {

    @XmlElement(name = "product")
    private List<Product> product;
    public Products() {
        product = new ArrayList<Product>();
    }
    public List<Product> getProducts() {
        return product;
    }
    public void setProducts(List<Product> products) {
        this.product = products;
    }
    public void addProduct(Product product) {
        this.product.add(product);
    }
    public void removeProduct(Product product) {
        this.product.remove(product);
    }

}
