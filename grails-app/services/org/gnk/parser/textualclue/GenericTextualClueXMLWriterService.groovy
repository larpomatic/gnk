package org.gnk.parser.textualclue

import org.gnk.resplacetime.GenericTextualClue
import org.w3c.dom.CDATASection
import org.w3c.dom.Document
import org.w3c.dom.Element

class GenericTextualClueXMLWriterService {

    /* Exposed Methods */
    def Element getGenericTextualClueElement(Document doc, GenericTextualClue genericTextualClue) {
        Element genericTextualClueElt = getGenericTextualClueRootElement(doc, genericTextualClue);

        genericTextualClueElt.appendChild(getGenericTextualClueTagsElement(doc, genericTextualClue));
        genericTextualClueElt.appendChild(getGenericTextualClueTextElement(doc, genericTextualClue));

        return genericTextualClueElt;
    }
    /* !Exposed Methods */

    /* Construction Methods */
    private Element getGenericTextualClueRootElement(Document doc, GenericTextualClue genericTextualClue)
    {
        Element rootElt = doc.createElement("TEXTUAL_CLUE")

        if (!genericTextualClue.DTDId)
            genericTextualClue.DTDId = genericTextualClue.id
        rootElt.setAttribute("id", genericTextualClue.DTDId.toString())
        rootElt.setAttribute("code", genericTextualClue.code)
        rootElt.setAttribute("type", "") // il n'y a pas d'attribut 'Type' sur les textualClueTag
        rootElt.setAttribute("title", genericTextualClue.title)
        //rootElt.setAttribute("gender", genericTextualClue.gender)
        if (!genericTextualClue.possessedByRole.DTDId)
            genericTextualClue.possessedByRole.DTDId = genericTextualClue.possessedByRole.id
        rootElt.setAttribute("owner_role_id", genericTextualClue.possessedByRole.DTDId.toString())
        if (!genericTextualClue.fromRole.DTDId)
            genericTextualClue.fromRole.DTDId = genericTextualClue.fromRole.id
        rootElt.setAttribute("from_role_id", genericTextualClue.fromRole.DTDId.toString())
        if (!genericTextualClue.toRole.DTDId)
            genericTextualClue.toRole.DTDId = genericTextualClue.toRole.id
        rootElt.setAttribute("to_role_id", genericTextualClue.toRole.DTDId.toString())

        return rootElt
    }

    private Element getGenericTextualClueTagsElement(Document doc, GenericTextualClue genericTextualClue) {
        Element tagsElt = doc.createElement("TAGS");

        /*if (genericTextualClue.textualClueTag) {
            Element tag = doc.createElement("TAG");
            tag.setAttribute("value", genericTextualClue.textualClueTag.tag.name)
            tag.setAttribute("type", genericTextualClue.textualClueTag.tag.tagFamily.value)
            tag.setAttribute("weight", "") // il n'y a pas de poids sur les textualClueTag
            tagsElt.appendChild(tag)
        }*/

        return tagsElt;
    }

    private Element getGenericTextualClueTextElement(Document doc, GenericTextualClue genericTextualClue) {
        Element textElt = doc.createElement("TEXT")

        if (genericTextualClue.text) {
            CDATASection textData = doc.createCDATASection("TEXT")
            textData.setData(genericTextualClue.text)
            textElt.appendChild(textData)
        }

        return textElt
    }
}

