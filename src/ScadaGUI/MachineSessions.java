package ScadaGUI;

import java.util.Date;

public class MachineSessions {

    String name;
    String timeStart;
    String timeStop;
    String dateStart;
    String dateStop;
    int upTimeStart;
    int upTimeStop;
    int session;
    int sessions;
    String sessionLong;

    public String getSessionLong() {
        long hour = session / 3600,
                min = session / 60 % 60,
                sec = session / 1 % 60;
        sessionLong = (String.format("%02d:%02d:%02d", hour, min, sec));

        return sessionLong;
    }

    public int getSessions() {
        return sessions;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(String timeStop) {
        this.timeStop = timeStop;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateStop() {
        return dateStop;
    }

    public void setDateStop(String dateStop) {
        this.dateStop = dateStop;
    }

    public int getUpTimeStart() {
        return upTimeStart;
    }

    public void setUpTimeStart(int upTimeStart) {
        this.upTimeStart = upTimeStart;
    }

    public int getUpTimeStop() {
        return upTimeStop;
    }

    public void setUpTimeStop(int upTimeStop) {
        this.upTimeStop = upTimeStop;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

}
