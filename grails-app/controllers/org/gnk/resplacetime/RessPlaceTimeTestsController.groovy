package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericResource;
import org.gnk.ressplacetime.ReferentialResource;
import com.gnk.substitution.Tag

import org.gnk.ressplacetime.EventTime
import org.gnk.ressplacetime.PastsceneTime;

import org.gnk.ressplacetime.GenericPlace;
import org.gnk.ressplacetime.ReferentialPlace;

class RessPlaceTimeTestsController {

    // Paramètres des tests pour ResourceService
    String tagType = "Arme"
    String familyTagRichesseRessource = "Richesse"
    String tagRichesseRessource = "Moyen"
    String researchedUniverseRessource = "Grèce Antique"

    // Paramètres des tests pour PlaceService
    String tagSuperficie = "Cité"
    String familyTagRichessePlace = "Richesse"
    String tagRichessePlace = "Moyen"
    String researchedUniversePlace = "France Contemporaine"

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {}

    def resourceTest() {
    }

    def resourceTestWithoutBannedItems() {
        final ResourceService resourceService = new ResourceService()

        // Création d'une GenericResource (la recherche)
        GenericResource genericResource = new GenericResource()
        genericResource.code = "GEN"
        genericResource.bannedItemsList = null
        genericResource.resultList = null

        Tag typeTag = new Tag()
        typeTag.type = resourceService.typeTagInDatabase
        typeTag.value = tagType
        typeTag.weight = 100
        Tag wealthTag = new Tag()
        wealthTag.type = familyTagRichesseRessource
        wealthTag.value = tagRichesseRessource
        wealthTag.weight = 100

        genericResource.tagList = new LinkedList<Tag>()
        genericResource.tagList.add(typeTag)
        //genericResource.tagList.add(wealthTag)

        render("On recherche: une Ressource de type <b>" + tagType + "</b>, de richesse <b>" + tagRichesseRessource + "</b> pour l'univers <b>" + researchedUniverseRessource + "</b><br/>");

        // Lancement de la recherche
        genericResource = resourceService.findReferentialResource(genericResource, researchedUniverseRessource)

        String result = "Résultat obtenu: "
        for (ReferentialResource referentialResource : genericResource.resultList) {
            result += referentialResource.name + " (" + referentialResource.matchingRate + ")<br/>"
        }
        render(result)
    }

    def resourceTestWithBannedItems() {
        final ResourceService resourceService = new ResourceService()

        // Création d'une GenericResource (la recherche)
        GenericResource genericResource = new GenericResource()
        genericResource.code = "GEN"
        genericResource.bannedItemsList = null
        genericResource.resultList = null

        Tag typeTag = new Tag()
        typeTag.type = resourceService.typeTagInDatabase
        typeTag.value = tagType
        typeTag.weight = 100
        Tag wealthTag = new Tag()
        wealthTag.type = familyTagRichesseRessource
        wealthTag.value = tagRichesseRessource
        wealthTag.weight = 100

        genericResource.tagList = new LinkedList<Tag>()
        genericResource.tagList.add(typeTag)
        genericResource.tagList.add(wealthTag)

        render("On recherche: une Ressource de type <b>" + tagType + "</b>, de richesse <b>" + tagRichesseRessource + "</b> pour l'univers <b>" + researchedUniverseRessource + "</b><br/>");
        // Lancement de la recherche 1
        genericResource = resourceService.findReferentialResource(genericResource, researchedUniverseRessource)

        String result = "Résultat obtenu n°1: "
        for (ReferentialResource referentialResource : genericResource.resultList) {
            result += referentialResource.name + " (" + referentialResource.matchingRate + ")"
        }
        render(result + "<br/>")

        render("<br/>On lance la même recherche <b>en bannissant le premier résultat de la liste ci-dessus</b><br/>");
        genericResource.bannedItemsList.add(genericResource.resultList.first())
        genericResource.resultList.clear()
        // Lancement de la recherche 2
        genericResource = resourceService.findReferentialResource(genericResource, researchedUniverseRessource)
        result = "Résultat obtenu n°2: "
        for (ReferentialResource referentialResource : genericResource.resultList) {
            result += referentialResource.name + " (" + referentialResource.matchingRate + ")"
        }
        render(result + "<br/>")
    }

    def timeTest() {
    }

    def timeTestForPastscene() {
        final TimeService timeService = new TimeService()

        // Date de pivot (choisie comme étant le début du GN)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 4, 1, 10, 0);
        Date gnBeginDate = calendar.getTime();
        render("Début du GN: " + gnBeginDate.toString() + "<br/><br/>");
        PastsceneTime result;

        // Pastscene avec une date relative uniquement (ici : "il y a 5 mois")
        render("On teste: il y a 5 mois<br/>On attend: le 01.12.2009<br/>");
        PastsceneTime pastscene1 = new PastsceneTime();
        pastscene1.code = "PS1";
        pastscene1.relativeDateUnit = "M";
        pastscene1.relativeDateValue = 5;
        result = timeService.pastsceneRealDate(pastscene1, gnBeginDate);
        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                               + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");

        // Pastscene avec une date relative uniquement (ici : "il y a 13 mois")
        render("On teste: il y a 103 ans<br/>On attend: le 30.04.1907<br/>");
        PastsceneTime pastscene2 = new PastsceneTime();
        pastscene2.code = "PS2";
        pastscene2.relativeDateUnit = "Y";
        pastscene2.relativeDateValue = 103;
        result = timeService.pastsceneRealDate(pastscene2, gnBeginDate);
        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");

        // Pastscene avec une date absolue uniquement (ici : "14 juillet 1789")
        render("On teste: le 14 juillet 1789 à minuit<br/>On attend: la même date<br/>");
        PastsceneTime pastscene3 = new PastsceneTime();
        pastscene3.code = "PS3";
        pastscene3.absoluteYear = 1789;
        pastscene3.absoluteMonth = 7;
        pastscene3.absoluteDay = 14;
        pastscene3.absoluteHour = 0;
        pastscene3.absoluteMinute = 0;
        result = timeService.pastsceneRealDate(pastscene3, gnBeginDate);

        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                           + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");

        // Pastscene avec une date mixte (ici : "il y a 5 ans, en Janvier")
        render("On teste: il y a 5 ans, en Janvier<br/>On attend: le 01.01.2005<br/>");
        PastsceneTime pastscene4 = new PastsceneTime();
        pastscene4.code = "PS4";
        pastscene4.relativeDateUnit = "Y";
        pastscene4.relativeDateValue = 5;
        pastscene4.absoluteMonth = 1;
        result = timeService.pastsceneRealDate(pastscene4, gnBeginDate);
        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                           + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");
    }

    def timeTestForEvent() {
        final TimeService timeService = new TimeService()

        // Date de pivot (choisie comme étant le début du GN)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 4, 1, 10, 0);
        Date gnBeginDate = calendar.getTime();
        Integer gnDuration = 50;
        render("Début du GN: " + gnBeginDate.toString()
                + "<br/>Durée du GN: " + gnDuration.toString() + " heures<br/><br/>");
        EventTime result;

        // Event qui se passe à 20%
        render("On teste: 20% (soit T0 + 10 heures)<br/>");
        EventTime event1 = new EventTime();
        event1.code = "EV1";
        event1.timing = 20;
        result = timeService.eventRealDate(event1, gnBeginDate, gnDuration);
        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");

        // Event qui se passe à 50%
        render("On teste: 50% (soit T0 + 25 heures)<br/>");
        EventTime event2 = new EventTime();
        event2.code = "EV2";
        event2.timing = 50;
        result = timeService.eventRealDate(event2, gnBeginDate, gnDuration);
        render(result.code + ": " + result.absoluteDay.toString() + "." + result.absoluteMonth.toString() + "." + result.absoluteYear.toString()
                + " " + result.absoluteHour.toString() + ":" + result.absoluteMinute.toString() + "<br/><br/>");
    }

    def placeTest() {
    }

    def placeTestWithoutBannedItems() {
        final PlaceService placeService = new PlaceService()

        // Création d'une GenericResource (la recherche)
        GenericPlace genericPlace = new GenericPlace()
        genericPlace.code = "GEN"
        genericPlace.bannedItemsList = null
        genericPlace.resultList = null

        Tag areaTag = new Tag()
        areaTag.type = placeService.areaTagInDatabase
        areaTag.value = tagSuperficie
        areaTag.weight = 100
        Tag wealthTag = new Tag()
        wealthTag.type = familyTagRichessePlace
        wealthTag.value = tagRichessePlace
        wealthTag.weight = 100

        genericPlace.tagList = new LinkedList<Tag>()
        genericPlace.tagList.add(areaTag)
        genericPlace.tagList.add(wealthTag)

        render("On recherche: un Lieu de type <b>" + tagSuperficie + "</b>, de richesse <b>" + tagRichessePlace + "</b> pour l'univers <b>" + researchedUniversePlace + "</b><br/>");

        // Lancement de la recherche
        genericPlace = placeService.findReferentialPlace(genericPlace, researchedUniversePlace)

        String result = "Résultat obtenu: "
        for (ReferentialPlace referentialPlace : genericPlace.resultList) {
            result += referentialPlace.name + " (" + referentialPlace.matchingRate + ")<br/>"
        }
        render(result)
    }

    def placeTestWithBannedItems() {
        final PlaceService placeService = new PlaceService()

        // Création d'une GenericResource (la recherche)
        GenericPlace genericPlace = new GenericPlace()
        genericPlace.code = "GEN"
        genericPlace.bannedItemsList = null
        genericPlace.resultList = null

        Tag areaTag = new Tag()
        areaTag.type = placeService.areaTagInDatabase
        areaTag.value = tagSuperficie
        areaTag.weight = 100
        Tag wealthTag = new Tag()
        wealthTag.type = familyTagRichessePlace
        wealthTag.value = tagRichessePlace
        wealthTag.weight = 100

        genericPlace.tagList = new LinkedList<Tag>()
        genericPlace.tagList.add(areaTag)
        genericPlace.tagList.add(wealthTag)

        render("On recherche: un Lieu de type <b>" + tagSuperficie + "</b>, de richesse <b>" + tagRichessePlace + "</b> pour l'univers <b>" + researchedUniversePlace + "</b><br/>");

        // Lancement de la recherche 1
        genericPlace = placeService.findReferentialPlace(genericPlace, researchedUniversePlace)

        String result = "Résultat obtenu n°1: "
        for (ReferentialPlace referentialPlace : genericPlace.resultList) {
            result += referentialPlace.name + " (" + referentialPlace.matchingRate + ")<br/>"
        }
        render(result + "<br/>")

        render("<br/>On lance la même recherche <b>en bannissant le premier résultat de la liste ci-dessus</b><br/>");
        genericPlace.bannedItemsList.add(genericPlace.resultList.first())
        genericPlace.resultList.clear()
        // Lancement de la recherche 2
        genericPlace = placeService.findReferentialPlace(genericPlace, researchedUniversePlace)
        result = "Résultat obtenu n°2: "
        for (ReferentialPlace referentialPlace : genericPlace.resultList) {
            result += referentialPlace.name + " (" + referentialPlace.matchingRate + ")<br/>"
        }
        render(result + "<br/>")
    }
}
