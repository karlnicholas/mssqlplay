package org.example.mssqlplay;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="RPTCHILD")
public class RptChild {
    @Id
    @Column("pghNr")
    int pghNr;
    @Column("rptNr")
    int rptNr;
    @Column("pghName")
    String pghName;

}
