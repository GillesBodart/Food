

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
@Table(name = "UNIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Unit.findAll", query = "SELECT u FROM Unit u"),
    @NamedQuery(name = "Unit.findByUnitid", query = "SELECT u FROM Unit u WHERE u.unitid = :unitid"),
    @NamedQuery(name = "Unit.findByUnitname", query = "SELECT u FROM Unit u WHERE u.unitname = :unitname"),
    @NamedQuery(name = "Unit.findByUnitreport", query = "SELECT u FROM Unit u WHERE u.unitreport = :unitreport")})
public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "unitid")
                @GeneratedValue(strategy = GenerationType.TABLE, generator="Unit")
    @TableGenerator(name="Unit", allocationSize=1)
    private Long unitid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "unitname")
    private String unitname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "unitreport")
    private BigDecimal unitreport;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aliunit")
    private List<Aliment> alimentList;
    @OneToMany(mappedBy = "unitparent")
    private List<Unit> unitList;
    @JoinColumn(name = "unitparent", referencedColumnName = "unitid")
    @ManyToOne
    private Unit unitparent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingunit")
    private List<Ingredient> ingredientList;

    public Unit() {
    }

    public Unit(Long unitid) {
        this.unitid = unitid;
    }

    public Unit(Long unitid, String unitname, BigDecimal unitreport) {
        this.unitid = unitid;
        this.unitname = unitname;
        this.unitreport = unitreport;
    }

    public Long getUnitid() {
        return unitid;
    }

    public void setUnitid(Long unitid) {
        this.unitid = unitid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public BigDecimal getUnitreport() {
        return unitreport;
    }

    public void setUnitreport(BigDecimal unitreport) {
        this.unitreport = unitreport;
    }

    @XmlTransient
    public List<Aliment> getAlimentList() {
        return alimentList;
    }

    public void setAlimentList(List<Aliment> alimentList) {
        this.alimentList = alimentList;
    }

    @XmlTransient
    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public Unit getUnitparent() {
        return unitparent;
    }

    public void setUnitparent(Unit unitparent) {
        this.unitparent = unitparent;
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
        hash += (unitid != null ? unitid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Unit)) {
            return false;
        }
        Unit other = (Unit) object;
        if ((this.unitid == null && other.unitid != null) || (this.unitid != null && !this.unitid.equals(other.unitid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Unit[ unitid=" + unitid + " ]";
    }

}
