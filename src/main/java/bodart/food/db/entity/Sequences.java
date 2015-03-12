package bodart.food.db.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gilles
 */
@Entity
@Table(name = "SEQUENCES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sequences.findAll", query = "SELECT s FROM Sequences s"),
    @NamedQuery(name = "Sequences.findBySeqName", query = "SELECT s FROM Sequences s WHERE s.seqName = :seqName"),
    @NamedQuery(name = "Sequences.findBySeqCount", query = "SELECT s FROM Sequences s WHERE s.seqCount = :seqCount")})
public class Sequences implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "seq_name")
    private String seqName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq_count")
    private long seqCount;

    public Sequences() {
    }

    public Sequences(String seqName) {
        this.seqName = seqName;
    }

    public Sequences(String seqName, long seqCount) {
        this.seqName = seqName;
        this.seqCount = seqCount;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public long getSeqCount() {
        return seqCount;
    }

    public void setSeqCount(long seqCount) {
        this.seqCount = seqCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seqName != null ? seqName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sequences)) {
            return false;
        }
        Sequences other = (Sequences) object;
        if ((this.seqName == null && other.seqName != null) || (this.seqName != null && !this.seqName.equals(other.seqName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bodart.food.db.entity.Sequences[ seqName=" + seqName + " ]";
    }

}
