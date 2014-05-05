package org.gnk.parser.plot

import org.gnk.parser.event.EventXMLWriterService
import org.gnk.parser.pastscene.PastSceneXMLWriterService
import org.gnk.parser.place.GenericPlaceXMLWriterService
import org.gnk.parser.resource.GenericResourceXMLWriterService
import org.gnk.parser.role.RoleXMLWriterService
import org.gnk.parser.textualclue.GenericTextualClueXMLWriterService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.w3c.dom.Document
import org.w3c.dom.Element

class PlotXMLWriterService {

    /* Exposed Methods */

    def Element getPlotElement(Document doc, Plot plot) {
        Element plotElt = getPlotRootElement(doc, plot);

        Element plotDescription = doc.createElement("DESCRIPTION");
        plotDescription.appendChild(doc.createTextNode(plot.description));
        plotElt.appendChild(plotDescription);

        // Les 3 pitchs
        if (plot.pitchOrga != null) {
            Element plotPitchOrga = doc.createElement("PITCH_ORGA");
            plotPitchOrga.appendChild(doc.createTextNode(plot.pitchOrga));
            plotElt.appendChild(plotPitchOrga);
        }

        if (plot.pitchPnj != null) {
            Element plotPitchPnj = doc.createElement("PITCH_PNJ");
            plotPitchPnj.appendChild(doc.createTextNode(plot.pitchPnj));
            plotElt.appendChild(plotPitchPnj);
        }

        if (plot.pitchPj != null) {
            Element plotPitchPj = doc.createElement("PITCH_PJ");
            plotPitchPj.appendChild(doc.createTextNode(plot.pitchPj));
            plotElt.appendChild(plotPitchPj);
        }

        plotElt.appendChild(getTagsElement(doc, plot));
        plotElt.appendChild(getRolesElement(doc, plot));
        plotElt.appendChild(getPastscenesElement(doc, plot));
        plotElt.appendChild(getEventsElement(doc, plot));
        plotElt.appendChild(getGenericPlacesElement(doc, plot));
        plotElt.appendChild(getGenericResourcesElement(doc, plot));
//        plotElt.appendChild(getTextualCluesElement(doc, plot));

        return plotElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */

    private Element getPlotRootElement(Document doc, Plot plot) {
        Element rootElement = doc.createElement("PLOT");

        if (plot.DTDId == null || plot.DTDId < 0) {
            plot.DTDId = plot.id;
        }
        assert (plot.DTDId != null && plot.DTDId >= 0);
        rootElement.setAttribute("id", plot.DTDId.toString());
        rootElement.setAttribute("title", plot.name);
        rootElement.setAttribute("is_evenemential", plot.isEvenemential?.toString());
        rootElement.setAttribute("is_mainstream", plot.isMainstream?.toString());
        rootElement.setAttribute("is_public", plot.isPublic?.toString());
        rootElement.setAttribute("author", plot.user?.getId().toString());
        rootElement.setAttribute("creation_date", plot.creationDate?.time.toString());
        rootElement.setAttribute("last_update_date", plot.updatedDate?.time.toString());

        return rootElement;
    }

    private Element getTagsElement(Document doc, Plot plot) {
        Element tagsElt = doc.createElement("TAGS");

        if (plot.extTags) {
            for (PlotHasTag plotHasPlotTag : plot.extTags) {
                Element tag = doc.createElement("TAG");
                tag.setAttribute("value", plotHasPlotTag.tag.name)
                tag.setAttribute("type", plotHasPlotTag.tag.tagFamily.value)
                tag.setAttribute("weight", plotHasPlotTag.weight.toString())
                tagsElt.appendChild(tag)
            }
        }

        return tagsElt;
    }

    private Element getRolesElement(Document doc, Plot plot) {
        Element rolesElt = doc.createElement("ROLES");

        if (plot.roles) {
            final RoleXMLWriterService roleWriter = new RoleXMLWriterService()
            for (Role role : plot.roles) {
                rolesElt.appendChild(roleWriter.getRoleElement(doc, role))
            }
        }
        return rolesElt;
    }

    private Element getPastscenesElement(Document doc, Plot plot) {
        Element pastscenesElt = doc.createElement("PAST_SCENES");

        if (plot.pastescenes) {
            final PastSceneXMLWriterService pastsceneWriter = new PastSceneXMLWriterService()
            for (Pastscene pastscene : plot.pastescenes) {
                pastscenesElt.appendChild(pastsceneWriter.getPastSceneElementForPlot(doc, pastscene))
            }
        }

        return pastscenesElt;
    }

    private Element getEventsElement(Document doc, Plot plot) {
        Element eventsElt = doc.createElement("EVENTS");

        if (plot.events) {
            final EventXMLWriterService eventWriter = new EventXMLWriterService()
            for (Event event : plot.events) {
                eventsElt.appendChild(eventWriter.getEventElementForPlot(doc, event))
            }
        }

        return eventsElt;
    }

    private Element getGenericPlacesElement(Document doc, Plot plot) {
        Element genericPlacesElt = doc.createElement("GENERIC_PLACES");

        if (plot.events) {
            final GenericPlaceXMLWriterService genericPlaceWriter = new GenericPlaceXMLWriterService()
            for (Event event : plot.events) {
                if (event.genericPlace != null) {
                    genericPlacesElt.appendChild(genericPlaceWriter.getGenericPlaceElement(doc, event.genericPlace))
                }
            }

            for (Pastscene pastscene : plot.pastescenes) {
                if (pastscene.genericPlace != null) {
                    genericPlacesElt.appendChild(genericPlaceWriter.getGenericPlaceElement(doc, pastscene.genericPlace))
                }
            }
        }

        return genericPlacesElt;
    }

    private Element getGenericResourcesElement(Document doc, Plot plot) {
        Element genericResourcesElt = doc.createElement("GENERIC_RESOURCES");

        HashMap<GenericResource, Role> genericResourceRoleHashMap = new HashMap<>();
        if (plot.roles) {
            for (Role role : plot.roles) {
                if (role.roleHasEvents) {
                    for (RoleHasEvent roleHasEvent : role.roleHasEvents) {
                        if (roleHasEvent.roleHasEventHasGenericResources) {
                            final GenericResourceXMLWriterService genericResourceWriter = new GenericResourceXMLWriterService()
                            for (RoleHasEventHasGenericResource roleHasEventHasGenericResource : roleHasEvent.roleHasEventHasGenericResources) {
//                                genericResourcesElt.appendChild(genericResourceWriter.getGenericResourceElement(doc, roleHasEventHasGenericResource.genericResource, role))
                                genericResourceRoleHashMap.put(roleHasEventHasGenericResource.genericResource, role);
                            }
                        }
                    }
                }
            }
        }
        if (plot.genericResources) {
            final GenericResourceXMLWriterService genericResourceWriter = new GenericResourceXMLWriterService();
            for (GenericResource genericResource : plot.genericResources) {
                genericResourcesElt.appendChild(genericResourceWriter.getGenericResourceElement(doc, genericResource, genericResourceRoleHashMap.get(genericResource)))
            }
        }

        return genericResourcesElt;
    }

//    private Element getTextualCluesElement(Document doc, Plot plot) {
//        Element textualCluesElt = doc.createElement("TEXTUAL_CLUES");
//
//        if (plot.roles) {
//            for (Role role : plot.roles) {
//                if (role.genericTextualCluesForPossededByRoleId) {
//                    final GenericTextualClueXMLWriterService genericTextualClueXMLWriter = new GenericTextualClueXMLWriterService()
//                    for (GenericTextualClue genericTextualClue : role.genericTextualCluesForPossededByRoleId) {
//                        textualCluesElt.appendChild(genericTextualClueXMLWriter.getGenericTextualClueElement(doc, genericTextualClue))
//                    }
//                }
//            }
//        }
//
//        return textualCluesElt;
//    }
    /* !Construction Methods */
}
