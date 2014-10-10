package org.gnk.selectintrigue

import org.gnk.tag.Tag

class PlotHasTag {

    Integer id
    Integer version

    Date dateCreated
    Date lastUpdated

    Integer weight

    public PlotHasTag() {

    }

    public PlotHasTag(Plot plotPar, Tag tagPar, Integer weightPar) {
        plot = plotPar
        tag = tagPar
        weight = weightPar
        version = 1
        Date now = new Date()
        dateCreated = now
        lastUpdated = now
    }

    static belongsTo = [ plot: Plot, tag: Tag ]

    static constraints = {
    }

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
