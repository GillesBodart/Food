

package bodart.food.db.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gilles
 */
@Entity
@Table(name = "INGREDIENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingredient.findAll", query = "SELECT i FROM Ingredient i"),
    @NamedQuery(name = "Ingredient.findByIngid", query = "SELECT i FROM Ingredient i WHERE i.ingid = :ingid"),
    @NamedQuery(name = "Ingredient.findByIngqty", query = "SELECT i FROM Ingredient i WHERE i.ingqty = :ingqty")})
public class Ingredient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ingid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator="Ingredient")
    @TableGenerator(name="Ingredient", allocationSize=1)
    private Long ingid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ingqty")
    private long ingqty;
    @JoinColumn(name = "ingunit", referencedColumnName = "unitid")
    @ManyToOne(optional = false)
    private Unit ingunit;
    @JoinColumn(name = "ingali", referencedColumnName = "aliid")
    @ManyToOne(optional = false)
    private Aliment ingali;
    @JoinColumn(name = "ingrec", referencedColumnName = "recid")
    @ManyToOne(optional = false)
    private Recipe ingrec;

    public Ingredient() {
    }

    public Ingredient(Long ingid) {
        this.ingid = ingid;
    }

    public Ingredient(Long ingid, long ingqty) {
        this.ingid = ingid;
        this.ingqty = ingqty;
    }

    public Long getIngid() {
        return ingid;
    }

    public void setIngid(Long ingid) {
        this.ingid = ingid;
    }

    public long getIngqty() {
        return ingqty;
    }

    public void setIngqty(long ingqty) {
        this.ingqty = ingqty;
    }

    public Unit getIngunit() {
        return ingunit;
    }

    public void setIngunit(Unit ingunit) {
        this.ingunit = ingunit;
    }

    public Aliment getIngali() {
        return ingali;
    }

    public void setIngali(Aliment ingali) {
        this.ingali = ingali;
    }

    public Recipe getIngrec() {
        return ingrec;
    }

    public void setIngrec(Recipe ingrec) {
        this.ingrec = ingrec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ingid != null ? ingid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingredient)) {
            return false;
        }
        Ingredient other = (Ingredient) object;
        if ((this.ingid == null && other.ingid != null) || (this.ingid != null && !this.ingid.equals(other.ingid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Ingredient[ ingid=" + ingid + " ]";
    }

}
