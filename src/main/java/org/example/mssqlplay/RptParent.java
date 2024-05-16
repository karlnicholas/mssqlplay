package org.example.mssqlplay;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "RPTPARENT")
public class RptParent {

    @Id
    @Column("rptNr")
    private int rptNr;

    @Column("rptName")
    private String rptName;

    public int getRptNr() {
        return rptNr;
    }

    public void setRptNr(int rptNr) {
        this.rptNr = rptNr;
    }

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }
}