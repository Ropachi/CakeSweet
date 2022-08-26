//個人データモデル
package abc.cakesweet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotEmpty;

//顧客データ作成
@Entity
@Table(name = "mydata")
public class MyData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(nullable = false)
    //@NotEmpty(message = "名前を入力してください。")
    private String name;

    @Column(nullable = false)
    //@Email(message = "メール形式で入力してください")
    private String mail;

    @Column(nullable = false)
    //@NotEmpty(message = "パスワードを入力してください。")
    private String psw;

    private Long login_id;
    private String login_name;
    private String login_psw;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public Long getLogin_id() {
        return login_id;
    }

    public void setLogin_id(Long login_id) {
        this.login_id = login_id;
    }

    public String getLogin_name() {
        return login_name;
    }

//login時、名前とパスワード入力時用変数セット
    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getLogin_psw() {
        return login_psw;
    }

    public void setLogin_psw(String login_psw) {
        this.login_psw = login_psw;
    }
}
