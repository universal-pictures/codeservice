package com.dreamworks.uddcs.contents;

import com.dreamworks.uddcs.retailers.Retailer;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by dsherman on 2/27/17.
 */
@Entity
public class Content
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String title;
    private String eidr;
    private String gtm;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "content_retailer", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "retailer_id", referencedColumnName = "id"))
    private Set<Retailer> retailers;

    public Content() {}

    public Content(String id, String title, String eidr, String gtm)
    {
        this.id = id;
        this.title = title;
        this.eidr = eidr;
        this.gtm = gtm;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEidr()
    {
        return eidr;
    }

    public void setEidr(String eidr)
    {
        this.eidr = eidr;
    }

    public String getGtm()
    {
        return gtm;
    }

    public void setGtm(String gtm)
    {
        this.gtm = gtm;
    }

    public Set<Retailer> getRetailers()
    {
        return retailers;
    }

    public void setRetailers(Set<Retailer> retailers)
    {
        this.retailers = retailers;
    }
}
