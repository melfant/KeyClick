import java.util.ArrayList;
import java.util.List;

public class Result {
    private String lastName;
    private String firstName;
    private String middleName;
    private String periods;
    private String result;

    public Result(String lastName, String firstName, String middleName, String periods, String result) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.periods = periods;
        this.result = result;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
