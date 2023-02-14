package com.autocall.autoanswercalls;

public class Model {
    String ID;
    String STARTTIME;
    String ENDTIME;
    String AUDIO;
    String DESCRIPTION;
    String STARTDATE;
    String ENDDATE;

    public Model(String ID, String STARTTIME, String ENDTIME, String AUDIO, String DESCRIPTION, String STARTDATE, String ENDDATE) {
        this.ID = ID;
        this.STARTTIME = STARTTIME;
        this.ENDTIME = ENDTIME;
        this.AUDIO = AUDIO;
        this.DESCRIPTION = DESCRIPTION;
        this.STARTDATE = STARTDATE;
        this.ENDDATE = ENDDATE;
    }

    public String getID() {
        return ID;
    }

    public String getSTARTTIME() {return STARTTIME;}

    public String getENDTIME() {
        return ENDTIME;
    }

    public String getSTARTDATE() {return STARTDATE;}

    public String getENDDATE() {return ENDDATE;}

    public String getAUDIO() {
        return AUDIO;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSTARTTIME(String STARTTIME) {
        this.STARTTIME = STARTTIME;
    }

    public void setENDTIME(String ENDTIME) {
        this.ENDTIME = ENDTIME;
    }

    public void setSTARTDATE(String STARTDATE) {this.STARTDATE = STARTDATE;}

    public void setENDDATE(String ENDDATE) {this.ENDDATE = ENDDATE;}

    public void setAUDIO(String AUDIO) {
        this.AUDIO = AUDIO;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

}
