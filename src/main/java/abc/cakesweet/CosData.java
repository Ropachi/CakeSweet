package abc.cakesweet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;

//import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "cosdata")

public class CosData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long cosid;

    @Column
    private Long myid;

    @Column
    private String cosname;

    @Column
    private Integer cosmonth;

    public Long getCosid() {
        return cosid;
    }

    public void setCosid(Long cosid) {
        this.cosid = cosid;
    }

    public Long getMyid() {
        return myid;
    }

    public void setMyid(Long myid) {
        this.myid = myid;
    }

    public String getCosname() {
        return cosname;
    }

    public void setCosname(String cosname) {
        this.cosname = cosname;
    }

    public Integer getCosmonth() {
        return cosmonth;
    }

    public void setCosmonth(Integer cosmonth) {
        this.cosmonth = cosmonth;
    }
}
