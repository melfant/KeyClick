import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Results implements Serializable {
    private volatile static Results results;

    private Integer maxPeriods;
    private List<Result> resultList = new ArrayList<>();

    Settings settings = Settings.getInstance();


    private Results(){
        this.maxPeriods = settings.getPeriodsNumber();
    }

    public static Results getInstance(){
        if (results == null){
            synchronized (Settings.class){
                if(results == null){
                    results = new Results();
                }
            }
        }
        return results;
    }


    public Integer getMaxPeriods() {
        return maxPeriods;
    }

    public void setMaxPeriods(Integer maxPeriods) {
        this.maxPeriods = maxPeriods;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }
}




