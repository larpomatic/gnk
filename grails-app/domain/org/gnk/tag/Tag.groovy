package org.gnk.tag

import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.PlotHasTag

class Tag{

    Integer id
    Integer version
    boolean relevantPlot
    boolean relevantRole
    boolean relevantResource
    boolean relevantPlace
    boolean relevantFirstname
    boolean relevantLastname
    Date lastUpdated
    Date dateCreated
    String name
    Tag parent



    String value
    String family
    Integer weight
    String status
    static transients = ["value", "family", "weight", "status"]

    static hasMany = [
    extPlaceTags: PlaceHasTag,
    extResourceTags: ResourceHasTag,
    extPlotTags: PlotHasTag,
    extRoleTags: RoleHasTag,
    children: Tag]

    static constraints = {
        name maxSize: 45
    }


    @Override
    public String toString() {
        return name;
    }

    static mapping = {
        id type: 'integer'
        version type: 'integer'
        parent cascade: 'all'
    }


    String getterName() {
        return name;
    }

    //TagRelevant getTagRelevant(){
      //   return TagRelevant.findByTag(this)
    //}

}
