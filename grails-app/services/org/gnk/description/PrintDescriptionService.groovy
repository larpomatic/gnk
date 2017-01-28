package org.gnk.description

import org.gnk.publication.WordWriter
import org.gnk.selectintrigue.Description

class PrintDescriptionService {

    def printDescription(WordWriter wordWriter, ArrayList<Description> list_Description)
    {
        Description description;
        ArrayList<Description> introduction = new ArrayList<Description>();
        ArrayList<Description> contexte = new ArrayList<Description>();
        ArrayList<Description> univers = new ArrayList<Description>();
        ArrayList<Description> information = new ArrayList<Description>();
        ArrayList<Description> personnalite = new ArrayList<Description>();
        ArrayList<Description> regle = new ArrayList<Description>();
        ArrayList<Description> divers = new ArrayList<Description>();
        for (int i = 0; i < list_Description.size(); i++)
        {
            description = list_Description.get(i);
            switch (description.type)
            {
                case "Introduction" :
                    introduction.add(description)
                    break
                case "Contexte du GN" :
                    contexte.add(description)
                    break
                case "Univers du GN" :
                    univers.add(description)
                    break
                case "Informations lues dans la presse récemment" :
                    information.add(description)
                    break
                case "Points de règles" :
                    regle.add(description)
                    break
                case "Personnalités connues" :
                    personnalite.add(description)
                    break
                case "Divers" :
                    divers.add(description)
                    break
                default:
                    break
            }
        }

        if (introduction.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", introduction.get(0).type)
            for (Description d : introduction)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }

        if (contexte.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", contexte.get(0).type)
            for (Description d : contexte)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }

        if (univers.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", univers.get(0).type)
            for (Description d : univers)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }
        if (information.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", information.get(0).type)
            for (Description d : information)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }
        if (personnalite.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", personnalite.get(0).type)
            for (Description d : personnalite)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }
        if (regle.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", regle.get(0).type)
            for (Description d : regle)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }
        if (divers.size() > 0) {
            wordWriter.addStyledParagraphOfText("T3", divers.get(0).type)
            for (Description d : divers)
                wordWriter.addStyledParagraphOfText("Normal", d.pitch)
        }
    }

}
