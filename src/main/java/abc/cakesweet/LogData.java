//ログイン用データモデル
package abc.cakesweet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "logdata")
public class LogData {
    @Id
    @Column
    protected Long logid;
    @Column
    private String logname;
    @Column
    private String logpsw;

    public Long getLogid() {
        return logid;
    }

    public void setLogid(Long logid) {
        this.logid = logid;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getLogpsw() {
        return logpsw;
    }

    public void setLogpsw(String logpsw) {
        this.logpsw = logpsw;
    }
}
