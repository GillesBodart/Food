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
@Table(name = "ALIMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aliment.findAll", query = "SELECT a FROM Aliment a"),
    @NamedQuery(name = "Aliment.findByAliid", query = "SELECT a FROM Aliment a WHERE a.aliid = :aliid"),
    @NamedQuery(name = "Aliment.findByAliname", query = "SELECT a FROM Aliment a WHERE a.aliname = :aliname"),
    @NamedQuery(name = "Aliment.findByAliprix", query = "SELECT a FROM Aliment a WHERE a.aliprix = :aliprix")})
public class Aliment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aliid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Aliment")
    @TableGenerator(name = "Aliment", allocationSize = 1)
    private Long aliid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "aliname")
    private String aliname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "aliprix")
    private BigDecimal aliprix;
    @JoinColumn(name = "aliprovider", referencedColumnName = "proid")
    @ManyToOne
    private Provider aliprovider;
    @JoinColumn(name = "alicategory", referencedColumnName = "catid")
    @ManyToOne(optional = false)
    private Category alicategory;
    @JoinColumn(name = "aliunit", referencedColumnName = "unitid")
    @ManyToOne(optional = false)
    private Unit aliunit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingali")
    private List<Ingredient> ingredientList;

    public Aliment() {
    }

    public Aliment(Long aliid) {
        this.aliid = aliid;
    }

    public Aliment(Long aliid, String aliname, BigDecimal aliprix) {
        this.aliid = aliid;
        this.aliname = aliname;
        this.aliprix = aliprix;
    }

    public Long getAliid() {
        return aliid;
    }

    public void setAliid(Long aliid) {
        this.aliid = aliid;
    }

    public String getAliname() {
        return aliname;
    }

    public void setAliname(String aliname) {
        this.aliname = aliname;
    }

    public BigDecimal getAliprix() {
        return aliprix;
    }

    public void setAliprix(BigDecimal aliprix) {
        this.aliprix = aliprix;
    }

    public Provider getAliprovider() {
        return aliprovider;
    }

    public void setAliprovider(Provider aliprovider) {
        this.aliprovider = aliprovider;
    }

    public Category getAlicategory() {
        return alicategory;
    }

    public void setAlicategory(Category alicategory) {
        this.alicategory = alicategory;
    }

    public Unit getAliunit() {
        return aliunit;
    }

    public void setAliunit(Unit aliunit) {
        this.aliunit = aliunit;
    }

    @XmlTransient
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aliid != null ? aliid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aliment)) {
            return false;
        }
        Aliment other = (Aliment) object;
        if ((this.aliid == null && other.aliid != null) || (this.aliid != null && !this.aliid.equals(other.aliid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Aliment[ aliid=" + aliid + " ]";
    }

}
