package main.models;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "category"
})
public class Categories {

    @XmlElement(name = "category")
    private List<Category> category;
    public Categories() {
        category = new ArrayList<>();
    }
    public List<Category> getCategory() {
        return category;
    }
    public void setProduct(List<Category> product) {
        this.category = product;
    }
    public void addCategory(Category category) {
        this.category.add(category);
    }
    public void removeCategory(Category category) {
        this.category.remove(category);
    }
}
