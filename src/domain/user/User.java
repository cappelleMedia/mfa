package domain.user;

import domain.DaoObject;
import domain.DomainException;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "\"User\"")
public class User extends DaoObject implements Serializable {
//TODO make secure user with ecrypted pw and other more secure elements
//TODO add email (verification reasons + password change/forgotten)

    @NotNull @Size(min = 0)
    private String username;//TODO reject whitespace between characters!
    @NotNull @Size(min=0)
    private String pw; //TODO set min, max and numbers/capitals requirements
    boolean adminState;

    public User() {
    }

    public User(String username, String pw, boolean isAdmin) throws DomainException {
        this.setUsername(username);
        this.setPw(pw);
        this.setAdminState(isAdmin);
    }

    public String getUsername() {
        return username;
    }

    public String getPw() {
        return pw;
    }

    public boolean getAdminState() {
        return adminState;
    }

    public void setUsername(String username) throws DomainException {
        if (username == null || username.trim().isEmpty()) {
            throw new DomainException("A valid username is required");
        }
        this.username = username;
    }

    public void setPw(String pw) throws DomainException {
        if (pw == null || pw.trim().isEmpty()) {
            throw new DomainException("A valid password is required");
        }
        this.pw = pw;
    }

    public void setAdminState(boolean isAdmin) {
        this.adminState = isAdmin;
    }

    public boolean isCorrectPw(String pw) {
        if (pw != null && !pw.trim().isEmpty()) {
            if (pw.equals(this.pw)) {
                return true;
            }
        }
        return false;
    }

    public void updateUser(String prevPW, String username, String pw, boolean adminState) throws DomainException {
        if (!this.isCorrectPw(prevPW)) {
            throw new DomainException("You have no authentication to make changes to this user");
        }
        this.setPw(pw);
        this.setUsername(username);
        this.setAdminState(adminState);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.username);
        hash = 67 * hash + Objects.hashCode(this.pw);
        hash = 67 * hash + (this.adminState ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.pw, other.pw)) {
            return false;
        }
        if (this.adminState != other.adminState) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", pw=" + pw + ", isAdmin=" + adminState + '}';
    }

}
