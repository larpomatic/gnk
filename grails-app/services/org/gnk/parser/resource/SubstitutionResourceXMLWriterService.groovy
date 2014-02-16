package org.gnk.parser.resource

import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class SubstitutionResourceXMLWriterService {

    /* Exposed Methods */
    public Element getResourcesElement(Document doc, Set<Plot> selectedPlotSet) {
        Element resourcesE = doc.createElement("RESOURCES")
        resourcesE.setAttribute("step_id", "3")

        // Iterate generic resources
        for(plot in selectedPlotSet) {
            for(Role role : plot.roles) {
                if (role.roleHasEvents) {
                    for (RoleHasEvent roleHasEvent : role.roleHasEvents) {
                        if (roleHasEvent.roleHasEventHasGenericResources) {
                            for (RoleHasEventHasGenericResource roleHasEventHasGenericResource : roleHasEvent.roleHasEventHasGenericResources) {
                                resourcesE.appendChild(getGenericResourceElement(doc, roleHasEventHasGenericResource.genericResource, plot))
                            }
                        }
                    }
                }
            }
        }

        return resourcesE
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getGenericResourceElement(Document doc, GenericResource genericResource, Plot plot){
        Element genericResourceE = doc.createElement("GENERIC_RESOURCE")

        if (!genericResource.DTDId)
            genericResource.DTDId = genericResource.id
        genericResourceE.setAttribute("generic_resource_id", genericResource.DTDId as String)
        genericResourceE.setAttribute("plot_id", plot.DTDId as String)

        if (genericResource.selectedResource != null){
            genericResourceE.appendChild(getResourceElement(doc, genericResource.selectedResource, "selected"))
        }

        for(resource in genericResource.proposedResources) {
            genericResourceE.appendChild(getResourceElement(doc, resource, "proposed"))
        }

        for(resource in genericResource.bannedResources) {
            genericResourceE.appendChild(getResourceElement(doc, resource, "banned"))
        }

        return genericResourceE
    }

    private Element getResourceElement(Document doc, Resource resource, String status){
        Element resourceE = doc.createElement("RESOURCE")

        if (!resource.DTDId)
            resource.DTDId = resource.id
        resourceE.setAttribute("resource_id", resource.DTDId as String)
        resourceE.setAttribute("status", status)

        return resourceE
    }
    /* !Construction Methods */

    /* Exposed Methods */
    public Element getResourcesDataElement(Document doc, Set<Resource> resourceSet) {
        Element resourcesE = doc.createElement("RESOURCES")
        if (resourceSet != null && !resourceSet.isEmpty()) {
            resourcesE.setAttribute("step_id", "3")
            for (Resource resource : resourceSet) {
                Element resourceE = doc.createElement("RESOURCE")
                resourceE.setAttribute("id", resource.DTDId as String)
                resourceE.setAttribute("value", resource.name)
                resourceE.setAttribute("gender", resource.gender)

                Element descriptionE = doc.createElement("DESCRIPTION")
                Element tagsE = doc.createElement("TAGS")

                // Description
                if (resource.description) {
                    CDATASection descriptionData = doc.createCDATASection("DESCRIPTION")
                    descriptionData.setData(resource.description)
                    descriptionE.appendChild(descriptionData)
                }

                // Tags
                for(ResourceHasTag resourceHasTag : resource.extTags) {
                    Element tagE = doc.createElement("TAG")
                    tagE.setAttribute("value", resourceHasTag.tag.name)
                    tagE.setAttribute("type", resourceHasTag.tag.tagFamily.value)
                    tagE.setAttribute("weight", resourceHasTag.weight as String)

                    tagsE.appendChild(tagE)
                }


                resourceE.appendChild(descriptionE)
                resourceE.appendChild(tagsE)
                resourcesE.appendChild(resourceE)
            }
        }
        return resourcesE
    }
    /* !Exposed Methods */
}
