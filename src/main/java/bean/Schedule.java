package bean;
import java.io.Serializable;
import java.sql.Date;

public class Schedule implements Serializable {
    private int scheduleId;
    private int movieId;
    private int screenNo;
    private Date startDatetime;
    private Date endDatetime;
    private String screeningFormat;

    public int getScheduleId(){
        return scheduleId;
    }
    public void setScheduleId(int id){
        this.id = id;
    } 
    public int getMovieId(){
        return movieId;
    }
    public void setMovieId(int id)
    public Date getStartDatetime(){
        return startDatetime;
    }
    public void setStartDatetime(Date date){
        this.date = date;
    }
}
