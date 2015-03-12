

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
@Table(name = "RECIPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recipe.findAll", query = "SELECT r FROM Recipe r"),
    @NamedQuery(name = "Recipe.findByRecid", query = "SELECT r FROM Recipe r WHERE r.recid = :recid"),
    @NamedQuery(name = "Recipe.findByRecname", query = "SELECT r FROM Recipe r WHERE r.recname = :recname"),
    @NamedQuery(name = "Recipe.findByRecprice", query = "SELECT r FROM Recipe r WHERE r.recprice = :recprice"),
    @NamedQuery(name = "Recipe.findByRecserving", query = "SELECT r FROM Recipe r WHERE r.recserving = :recserving"),
    @NamedQuery(name = "Recipe.findByRecprivate", query = "SELECT r FROM Recipe r WHERE r.recprivate = :recprivate")})
public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "recid")
        @GeneratedValue(strategy = GenerationType.TABLE, generator="Recipe")
    @TableGenerator(name="Recipe", allocationSize=1)
    private Long recid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "recname")
    private String recname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "recprice")
    private BigDecimal recprice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recserving")
    private long recserving;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recprivate")
    private short recprivate;
    @JoinColumn(name = "recowner", referencedColumnName = "usrid")
    @ManyToOne(optional = false)
    private Fooduser recowner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingrec")
    private List<Ingredient> ingredientList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lstrec")
    private List<Recipelist> recipelistList;

    public Recipe() {
    }

    public Recipe(Long recid) {
        this.recid = recid;
    }

    public Recipe(Long recid, String recname, BigDecimal recprice, long recserving, short recprivate) {
        this.recid = recid;
        this.recname = recname;
        this.recprice = recprice;
        this.recserving = recserving;
        this.recprivate = recprivate;
    }

    public Long getRecid() {
        return recid;
    }

    public void setRecid(Long recid) {
        this.recid = recid;
    }

    public String getRecname() {
        return recname;
    }

    public void setRecname(String recname) {
        this.recname = recname;
    }

    public BigDecimal getRecprice() {
        return recprice;
    }

    public void setRecprice(BigDecimal recprice) {
        this.recprice = recprice;
    }

    public long getRecserving() {
        return recserving;
    }

    public void setRecserving(long recserving) {
        this.recserving = recserving;
    }

    public short getRecprivate() {
        return recprivate;
    }

    public void setRecprivate(short recprivate) {
        this.recprivate = recprivate;
    }

    public Fooduser getRecowner() {
        return recowner;
    }

    public void setRecowner(Fooduser recowner) {
        this.recowner = recowner;
    }

    @XmlTransient
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
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
        hash += (recid != null ? recid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recipe)) {
            return false;
        }
        Recipe other = (Recipe) object;
        if ((this.recid == null && other.recid != null) || (this.recid != null && !this.recid.equals(other.recid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Recipe[ recid=" + recid + " ]";
    }

}
