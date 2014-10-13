package org.gnk.parser.place

import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericPlaceHasTag
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class GenericPlaceXMLWriterService {

    /* Exposed Methods */
    def Element getGenericPlaceElement(Document doc, GenericPlace genericPlace) {
        Element genericPlaceElt = getGenericPlaceRootElement(doc, genericPlace);

        genericPlaceElt.appendChild(getGenericPlaceTagsElement(doc, genericPlace));
        genericPlaceElt.appendChild(getGenericPlaceCommentElement(doc, genericPlace));

        return genericPlaceElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getGenericPlaceRootElement(Document doc, GenericPlace genericPlace)
    {
        Element rootElt = doc.createElement("GENERIC_PLACE")

        if (!genericPlace.DTDId)
            genericPlace.DTDId = genericPlace.id
        rootElt.setAttribute("id", genericPlace.DTDId.toString())
        rootElt.setAttribute("code", genericPlace.code)
        if (genericPlace.objectType != null)
            rootElt.setAttribute("object_type_id", genericPlace.objectType.id.toString())
        else
            rootElt.setAttribute("object_type_id", "null")

        return rootElt
    }

    private Element getGenericPlaceTagsElement(Document doc, GenericPlace genericPlace) {
        Element tagsElt = doc.createElement("TAGS");

        if (genericPlace.extTags) {
            for (GenericPlaceHasTag genericPlaceHasTag : genericPlace.extTags) {
                Element tag = doc.createElement("TAG");
                tag.setAttribute("value", genericPlaceHasTag.tag.name)
                tag.setAttribute("type", genericPlaceHasTag.tag.parent.name)
                tag.setAttribute("weight", genericPlaceHasTag.weight.toString())
                tagsElt.appendChild(tag)
            }
        }

        return tagsElt;
    }

    private Element getGenericPlaceCommentElement(Document doc, GenericPlace genericPlace) {
        Element commentElt = doc.createElement("COMMENT")

        if (genericPlace.comment) {
            CDATASection commentData = doc.createCDATASection("COMMENT")
            commentData.setData(genericPlace.comment)
            commentElt.appendChild(commentData)
        }

        return commentElt
    }
}