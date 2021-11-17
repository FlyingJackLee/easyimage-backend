package bank;

import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:29 am
 * 4
 */
@Entity
@Table
public class UserProfile {
    @Id
    @GenericGenerator(name="uuidGenerator", strategy="uuid")
    @GeneratedValue(generator="uuidGenerator")
    private String id;

    @Column(name = "locale")
    private Locale locale;

    @Column(name = "email")
    private String email;

    @OneToOne(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "userProfile")
    private List<ImageLibrary> libraries;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setLibraries(List<ImageLibrary> libraries) {
        this.libraries = libraries;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

