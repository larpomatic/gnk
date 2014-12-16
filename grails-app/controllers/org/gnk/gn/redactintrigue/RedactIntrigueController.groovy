package org.gnk.gn.redactintrigue

import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasRelationWithRole
import org.gnk.roletoperso.RoleHasTag
import org.gnk.roletoperso.RoleRelationType
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService;

import org.gnk.user.User
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class RedactIntrigueController {
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def index() {
		redirect(action: "list", params: params)
	}

	def list(Integer max) {
		[plotInstanceList: Plot.list(), plotInstanceTotal: Plot.count()]
	}

	def create() {
		[plotInstance: new Plot(params)]
	}

	def saveAndEdit() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = user.getUsername();
        User currentUser = User.findByUsername(currentUsername);
		def plotInstance = new Plot(params)
		plotInstance.creationDate = new Date();
		plotInstance.description = "";
		plotInstance.isEvenemential = false;
		plotInstance.isMainstream = false;
		plotInstance.isPublic = false;
		plotInstance.isDraft = true;
		plotInstance.updatedDate = new Date();
		plotInstance.user = currentUser;
		if (!plotInstance.save(flush: true)) {
			render(view: "create", model: [plotInstance: plotInstance])
			return
		}
		redirect(action: "edit", id: plotInstance.id)
	}

	def save() {
		Object obj = params
		def plotInstance = new Plot(params)
		if (!plotInstance.save(flush: true)) {
			render(view: "create", model: [plotInstance: plotInstance])
			return
		}
		redirect(action: "show", id: plotInstance.id)
	}

	def edit(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
			redirect(action: "list")
			return
		}
		int screen = 0
		if (params.screenStep)
		{
			screen = params.screenStep 
			screen = (screen - 48)	 
		}
        TagService tagService = new TagService();
		[plotInstance: plotInstance,
                plotTagList: tagService.getPlotTagQuery(),
                plotUniversList: tagService.getUniversTagQuery(),
                roleTagList: tagService.getRoleTagQuery(),
                resourceTagList: tagService.getResourceTagQuery(),
                placeTagList: tagService.getPlaceTagQuery(),
                relationTypes: RoleRelationType.list(),
                screenStep: screen]
	}

	def update(Long id) {
        def isupdate = true;
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
            isupdate = false;
		}
		plotInstance.properties = params
		plotInstance.description = params.plotDescription
        plotInstance.pitchOrga = params.plotPitchOrga
        plotInstance.pitchPj = params.plotPitchPj
        plotInstance.pitchPnj = params.plotPitchPnj
        if (plotInstance.extTags) {
            HashSet<PlotHasTag> plotHasTags = plotInstance.extTags;
            PlotHasTag.deleteAll(plotHasTags);
            plotInstance.extTags.clear();
        }
        else {
            plotInstance.extTags = new HashSet<PlotHasTag>();
        }

        plotInstance.save(flush: true);

		params.each {
			if (it.key.startsWith("plotTags_")) {
				PlotHasTag plotHasPlotTag = new PlotHasTag();
				Tag plotTag = Tag.get((it.key - "plotTags_") as Integer);
				plotHasPlotTag.tag = plotTag;
				plotHasPlotTag.weight = params.get("plotTagsWeight_" + plotTag.id) as Integer;
				plotHasPlotTag.plot = plotInstance
				plotInstance.extTags.add(plotHasPlotTag)
			}
		}

		if (!plotInstance.save(flush: true)) {
            isupdate = false;
		}

        render(contentType: "application/json") {
            object(isupdate: isupdate)
        }
	}

	def delete(Long id) {
		def plotInstance = Plot.get(id)
		if (!plotInstance) {
            render(contentType: "application/json") {
                object(isdelete: true)
            }
		}
        for (GenericResource resource in plotInstance.genericResources) {
            GenericResourceHasTag.executeUpdate("delete GenericResourceHasTag r where r.genericResource = " + resource.id);
        }
        GenericResource.executeUpdate("delete GenericResource g where g.plot = " + plotInstance.id);
        for (Role role in plotInstance.roles) {
            RoleHasRelationWithRole.executeUpdate("delete RoleHasRelationWithRole r where r.role1 = " + role.id);
            RoleHasRelationWithRole.executeUpdate("delete RoleHasRelationWithRole r where r.role2 = " + role.id);
            RoleHasTag.executeUpdate("delete RoleHasTag r where r.role = " + role.id);
            for (RoleHasEvent roleHasEvent in role.roleHasEvents) {
                RoleHasEventHasGenericResource.executeUpdate("delete RoleHasEventHasGenericResource r where r.roleHasEvent = " + roleHasEvent.id);
            }
            RoleHasEvent.executeUpdate("delete RoleHasEvent r where r.role = " + role.id);
            RoleHasPastscene.executeUpdate("delete RoleHasPastscene r where r.role = " + role.id);
        }
        Role.executeUpdate("delete Role r where r.plot = " + plotInstance.id);
        Pastscene.executeUpdate("update Pastscene p set p.pastscenePredecessor=null where p.plot=" + plotInstance.id);
        Pastscene.executeUpdate("delete Pastscene p where p.plot = " + plotInstance.id);
        Event.executeUpdate("update Event e set e.eventPredecessor=null where e.plot=" + plotInstance.id);
        Event.executeUpdate("delete Event e where e.plot = " + plotInstance.id);
        for (GenericPlace place in plotInstance.genericPlaces) {
            GenericPlaceHasTag.executeUpdate("delete GenericPlaceHasTag r where r.genericPlace = " + place.id);
        }
        GenericPlace.executeUpdate("delete GenericPlace g where g.plot = " + plotInstance.id);
        PlotHasTag.executeUpdate("delete PlotHasTag p where p.plot = " + plotInstance.id)
        Plot.executeUpdate("delete Plot p where p.id = " + plotInstance.id)
        render(contentType: "application/json") {
            object(isdelete: true)
        }
	}
}
