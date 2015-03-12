

package bodart.food.db.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "PROVIDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provider.findAll", query = "SELECT p FROM Provider p"),
    @NamedQuery(name = "Provider.findByProid", query = "SELECT p FROM Provider p WHERE p.proid = :proid"),
    @NamedQuery(name = "Provider.findByProname", query = "SELECT p FROM Provider p WHERE p.proname = :proname"),
    @NamedQuery(name = "Provider.findByProaddress", query = "SELECT p FROM Provider p WHERE p.proaddress = :proaddress")})
public class Provider implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "proid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator="Provider")
    @TableGenerator(name="Provider", allocationSize=1)
    private Long proid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "proname")
    private String proname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 130)
    @Column(name = "proaddress")
    private String proaddress;
    @OneToMany(mappedBy = "aliprovider")
    private List<Aliment> alimentList;

    public Provider() {
    }

    public Provider(Long proid) {
        this.proid = proid;
    }

    public Provider(Long proid, String proname, String proaddress) {
        this.proid = proid;
        this.proname = proname;
        this.proaddress = proaddress;
    }

    public Long getProid() {
        return proid;
    }

    public void setProid(Long proid) {
        this.proid = proid;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getProaddress() {
        return proaddress;
    }

    public void setProaddress(String proaddress) {
        this.proaddress = proaddress;
    }

    @XmlTransient
    public List<Aliment> getAlimentList() {
        return alimentList;
    }

    public void setAlimentList(List<Aliment> alimentList) {
        this.alimentList = alimentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proid != null ? proid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provider)) {
            return false;
        }
        Provider other = (Provider) object;
        if ((this.proid == null && other.proid != null) || (this.proid != null && !this.proid.equals(other.proid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Provider[ proid=" + proid + " ]";
    }

}
