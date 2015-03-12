

package bodart.food.db.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Gilles
 */
@Entity
@Table(name = "CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findByCatid", query = "SELECT c FROM Category c WHERE c.catid = :catid"),
    @NamedQuery(name = "Category.findByCatname", query = "SELECT c FROM Category c WHERE c.catname = :catname")})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "catid")
        @GeneratedValue(strategy = GenerationType.TABLE, generator="Category")
    @TableGenerator(name="Category", allocationSize=1)
    private Long catid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "catname")
    private String catname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alicategory")
    private List<Aliment> alimentList;
    @OneToMany(mappedBy = "catparent")
    private List<Category> categoryList;
    @JoinColumn(name = "catparent", referencedColumnName = "catid")
    @ManyToOne
    private Category catparent;

    public Category() {
    }

    public Category(Long catid) {
        this.catid = catid;
    }

    public Category(Long catid, String catname) {
        this.catid = catid;
        this.catname = catname;
    }

    public Long getCatid() {
        return catid;
    }

    public void setCatid(Long catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    @XmlTransient
    public List<Aliment> getAlimentList() {
        return alimentList;
    }

    public void setAlimentList(List<Aliment> alimentList) {
        this.alimentList = alimentList;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getCatparent() {
        return catparent;
    }

    public void setCatparent(Category catparent) {
        this.catparent = catparent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catid != null ? catid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.catid == null && other.catid != null) || (this.catid != null && !this.catid.equals(other.catid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Category[ catid=" + catid + " ]";
    }

}
