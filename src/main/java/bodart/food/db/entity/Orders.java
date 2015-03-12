

package bodart.food.db.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ORDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
    @NamedQuery(name = "Orders.findByOrdid", query = "SELECT o FROM Orders o WHERE o.ordid = :ordid"),
    @NamedQuery(name = "Orders.findByOrdname", query = "SELECT o FROM Orders o WHERE o.ordname = :ordname"),
    @NamedQuery(name = "Orders.findByOrdprice", query = "SELECT o FROM Orders o WHERE o.ordprice = :ordprice")})
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ordid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator="Orders")
    @TableGenerator(name="Orders", allocationSize=1)
    private Long ordid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ordname")
    private String ordname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ordprice")
    private BigDecimal ordprice;
    @JoinColumn(name = "ordowner", referencedColumnName = "usrid")
    @ManyToOne(optional = false)
    private Fooduser ordowner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lstord")
    private List<Recipelist> recipelistList;

    public Orders() {
    }

    public Orders(Long ordid) {
        this.ordid = ordid;
    }

    public Orders(Long ordid, String ordname) {
        this.ordid = ordid;
        this.ordname = ordname;
    }

    public Long getOrdid() {
        return ordid;
    }

    public void setOrdid(Long ordid) {
        this.ordid = ordid;
    }

    public String getOrdname() {
        return ordname;
    }

    public void setOrdname(String ordname) {
        this.ordname = ordname;
    }

    public BigDecimal getOrdprice() {
        return ordprice;
    }

    public void setOrdprice(BigDecimal ordprice) {
        this.ordprice = ordprice;
    }

    public Fooduser getOrdowner() {
        return ordowner;
    }

    public void setOrdowner(Fooduser ordowner) {
        this.ordowner = ordowner;
    }

    @XmlTransient
    public List<Recipelist> getRecipelistList() {
        return recipelistList;
    }

    public void setRecipelistList(List<Recipelist> recipelistList) {
        this.recipelistList = recipelistList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordid != null ? ordid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.ordid == null && other.ordid != null) || (this.ordid != null && !this.ordid.equals(other.ordid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Orders[ ordid=" + ordid + " ]";
    }

}
