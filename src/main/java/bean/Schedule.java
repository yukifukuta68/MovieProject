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
    public void setScheduleId(int scheduleId){
        this.scheduleId = scheduleId;
    }
    public int getMovieId(){
        return movieId;
    }
    public void setMovieId(int movieId){
        this.movieId = movieId;
    }
    public int getScreenNo(){
        return screenNo;
    }
    public void detScreenNo(int screenNo){
        this.screenNo = screenNo;
    }
    public Date getStartDatetime(){
        return startDatetime;
    }
    public void setStartDatetime(Date startDatetime){
        this.startDatetime = startDatetime;
    }
    public Date getEndDatetime(){
        return endDatetime;
    }
    public void getEndDatetime(Date endDatetime){
        this.endDatetime = endDatetime;
    }
    public String getScreeningFormat(){
        return screeningFormat;
    }
    public void setScreeningFormat(String screeningFormat){
        this.screeningFormat = screeningFormat;
    }
}
