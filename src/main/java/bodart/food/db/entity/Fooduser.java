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
@Table(name = "FOODUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fooduser.findAll", query = "SELECT f FROM Fooduser f"),
    @NamedQuery(name = "Fooduser.findByUsrid", query = "SELECT f FROM Fooduser f WHERE f.usrid = :usrid"),
    @NamedQuery(name = "Fooduser.findByUsrfirstname", query = "SELECT f FROM Fooduser f WHERE f.usrfirstname = :usrfirstname"),
    @NamedQuery(name = "Fooduser.findByUsrlastname", query = "SELECT f FROM Fooduser f WHERE f.usrlastname = :usrlastname"),
    @NamedQuery(name = "Fooduser.findByUsrpassword", query = "SELECT f FROM Fooduser f WHERE f.usrpassword = :usrpassword"),
    @NamedQuery(name = "Fooduser.login", query = "SELECT f FROM Fooduser f WHERE f.usremail = :usremail and f.usrpassword = :usrpassword"),
    @NamedQuery(name = "Fooduser.findByUsraddress", query = "SELECT f FROM Fooduser f WHERE f.usraddress = :usraddress"),
    @NamedQuery(name = "Fooduser.findByUsremail", query = "SELECT f FROM Fooduser f WHERE f.usremail = :usremail"),
    @NamedQuery(name = "Fooduser.findByUsrtel", query = "SELECT f FROM Fooduser f WHERE f.usrtel = :usrtel")})

public class Fooduser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usrid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "FoodUser")
    @TableGenerator(name = "FoodUser", allocationSize = 1)
    private Long usrid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usrfirstname")
    private String usrfirstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usrlastname")
    private String usrlastname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usrpassword")
    private String usrpassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usraddress")
    private String usraddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usremail")
    private String usremail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usrtel")
    private String usrtel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordowner")
    private List<Orders> ordersList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recowner")
    private List<Recipe> recipeList;

    public Fooduser() {
    }

    public Fooduser(Long usrid) {
        this.usrid = usrid;
    }

    public Fooduser(Long usrid, String usrfirstname, String usrlastname, String usrpassword, String usraddress, String usremail, String usrtel) {
        this.usrid = usrid;
        this.usrfirstname = usrfirstname;
        this.usrlastname = usrlastname;
        this.usrpassword = usrpassword;
        this.usraddress = usraddress;
        this.usremail = usremail;
        this.usrtel = usrtel;
    }

    public Long getUsrid() {
        return usrid;
    }

    public void setUsrid(Long usrid) {
        this.usrid = usrid;
    }

    public String getUsrfirstname() {
        return usrfirstname;
    }

    public void setUsrfirstname(String usrfirstname) {
        this.usrfirstname = usrfirstname;
    }

    public String getUsrlastname() {
        return usrlastname;
    }

    public void setUsrlastname(String usrlastname) {
        this.usrlastname = usrlastname;
    }

    public String getUsrpassword() {
        return usrpassword;
    }

    public void setUsrpassword(String usrpassword) {
        this.usrpassword = usrpassword;
    }

    public String getUsraddress() {
        return usraddress;
    }

    public void setUsraddress(String usraddress) {
        this.usraddress = usraddress;
    }

    public String getUsremail() {
        return usremail;
    }

    public void setUsremail(String usremail) {
        this.usremail = usremail;
    }

    public String getUsrtel() {
        return usrtel;
    }

    public void setUsrtel(String usrtel) {
        this.usrtel = usrtel;
    }

    @XmlTransient
    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    @XmlTransient
    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usrid != null ? usrid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fooduser)) {
            return false;
        }
        Fooduser other = (Fooduser) object;
        if ((this.usrid == null && other.usrid != null) || (this.usrid != null && !this.usrid.equals(other.usrid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Fooduser[ usrid=" + usrid + " ]";
    }

}
