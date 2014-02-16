package org.gnk.parser.resource

import org.gnk.parser.GNKDataContainerService
import org.gnk.resplacetime.Resource
import org.gnk.resplacetime.GenericResource

class SubstitutionResourceXMLReaderService {

    String selectedStatus = "selected"

    /* Exposed Methods */
    def static getSubstitutedResourceFromNode(Node RESOURCES, GNKDataContainerService dataContainer) {

        Resource resource;
        GenericResource genericResource;

        RESOURCES.each { genericResourceNode ->
            Integer genericResourceId = genericResourceNode.attribute("generic_resource_id") as Integer
            genericResource = dataContainer.genericResourceMap.get(genericResourceId, null)

            if (genericResource) {
                genericResourceNode.each { resourceNode ->
                    String resourceStatus = resourceNode.attribute("status") as String

                    if (resourceStatus.equals("selected")) {
                        Integer resourceId = resourceNode.attribute("resource_id") as Integer
                        resource = dataContainer.resourceMap.get(resourceId, null)
                    }
                }

                if (resource) {
                    genericResource.selectedResource = resource
                }
            }
        }
    }
/* !Exposed Methods */
}
