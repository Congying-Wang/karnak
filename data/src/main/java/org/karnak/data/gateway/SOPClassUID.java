package org.karnak.data.gateway;

import org.hibernate.validator.group.GroupSequenceProvider;
import org.karnak.data.profile.ActionTable;
import org.karnak.data.profile.ProfileTable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "SOPClassUID")
@Table(name = "sop_class_uid")
public class SOPClassUID {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String ciod;
    private String uid;
    private String name;

    @ManyToMany(mappedBy = "SOPClassUIDFilters", fetch = FetchType.EAGER)
    private List<Destination> Destinations = new ArrayList<>();

    public SOPClassUID(){
    }

    public SOPClassUID(String ciod){
        this.ciod = ciod;
    }

    public String getCiod() {
        return ciod;
    }

    public void setCiod(String ciod) {
        this.ciod = ciod;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Destination> getDestinations() {
        return Destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        Destinations = destinations;
    }
}