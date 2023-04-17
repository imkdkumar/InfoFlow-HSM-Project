/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kumarkhadka
 */
public class InsulinProgress implements Serializable{
    private String patientName;
    private int insulinLevel;
    private Date date_time;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getInsulinLevel() {
        return insulinLevel;
    }

    public void setInsulinLevel(int insulinLevel) {
        this.insulinLevel = insulinLevel;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }
    
}
