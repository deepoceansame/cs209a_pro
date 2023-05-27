package cse.java2.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StExThreadModel {

    @Id
    public long questionId;

    @Column(name="thread_json",columnDefinition="LONGTEXT")
    public String threadJson;
}
