package org.gnk.substitution.data

import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.tag.Tag
import java.util.List;

class GnInformation {
    Integer id
	String name
	Date dateCreated
	Date lastUpdated
	List<Tag> tagList
	Integer nbPlayers
    String universe
    Date t0Date
    Integer duration
	String GanttData
}
