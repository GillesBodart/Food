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
@Table(name = "RECIPELIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recipelist.findAll", query = "SELECT r FROM Recipelist r"),
    @NamedQuery(name = "Recipelist.findByLstid", query = "SELECT r FROM Recipelist r WHERE r.lstid = :lstid"),
    @NamedQuery(name = "Recipelist.findByLstrecqty", query = "SELECT r FROM Recipelist r WHERE r.lstrecqty = :lstrecqty")})
public class Recipelist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "lstid")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Recipelist")
    @TableGenerator(name = "Recipelist", allocationSize = 1)
    private Long lstid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lstrecqty")
    private long lstrecqty;
    @JoinColumn(name = "lstord", referencedColumnName = "ordid")
    @ManyToOne(optional = false)
    private Orders lstord;
    @JoinColumn(name = "lstrec", referencedColumnName = "recid")
    @ManyToOne(optional = false)
    private Recipe lstrec;

    public Recipelist() {
    }

    public Recipelist(Long lstid) {
        this.lstid = lstid;
    }

    public Recipelist(Long lstid, long lstrecqty) {
        this.lstid = lstid;
        this.lstrecqty = lstrecqty;
    }

    public Long getLstid() {
        return lstid;
    }

    public void setLstid(Long lstid) {
        this.lstid = lstid;
    }

    public long getLstrecqty() {
        return lstrecqty;
    }

    public void setLstrecqty(long lstrecqty) {
        this.lstrecqty = lstrecqty;
    }

    public Orders getLstord() {
        return lstord;
    }

    public void setLstord(Orders lstord) {
        this.lstord = lstord;
    }

    public Recipe getLstrec() {
        return lstrec;
    }

    public void setLstrec(Recipe lstrec) {
        this.lstrec = lstrec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lstid != null ? lstid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recipelist)) {
            return false;
        }
        Recipelist other = (Recipelist) object;
        if ((this.lstid == null && other.lstid != null) || (this.lstid != null && !this.lstid.equals(other.lstid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Recipelist[ lstid=" + lstid + " ]";
    }

}
