package org.gnk.utils
//import org.gnk.tag.TagFamily
/**
 * Created by pico on 26/06/2014.
 */
class Base {

//    def transformRelativePastsceneFields() {
//        for (Pastscene pastscene : Pastscene.findAll()) {
//            if (pastscene.dateYear) {
//                pastscene.isAbsoluteYear = true;
//            }
//            else {
//                pastscene.isAbsoluteYear = false;
//            }
//            if (pastscene.dateMonth) {
//                pastscene.isAbsoluteMonth = true;
//            }
//            else {
//                pastscene.isAbsoluteMonth = false;
//            }
//            if (pastscene.dateDay) {
//                pastscene.isAbsoluteDay = true;
//            }
//            else {
//                pastscene.isAbsoluteDay = false;
//            }
//            if (pastscene.dateHour) {
//                pastscene.isAbsoluteHour = true;
//            }
//            else {
//                pastscene.isAbsoluteHour = false;
//            }
//            if (pastscene.dateMinute) {
//                pastscene.isAbsoluteMinute = true;
//            }
//            else {
//                pastscene.isAbsoluteMinute = false;
//            }
//            if (pastscene.unitTimingRelative == "Y") {
//                pastscene.dateYear = pastscene.timingRelative;
//                pastscene.isAbsoluteYear = false;
//            }
//            else if (pastscene.unitTimingRelative == "M") {
//                pastscene.dateMonth = pastscene.timingRelative
//                pastscene.isAbsoluteMonth = false;
//            }
//            else if (pastscene.unitTimingRelative == "d") {
//                pastscene.dateDay = pastscene.timingRelative;
//                pastscene.isAbsoluteDay = false;
//            }
//            else if (pastscene.unitTimingRelative == "h") {
//                pastscene.dateHour = pastscene.timingRelative;
//                pastscene.isAbsoluteHour = false;
//            }
//            else if (pastscene.unitTimingRelative == "m") {
//                pastscene.dateMinute = pastscene.timingRelative;
//                pastscene.isAbsoluteMinute = false;
//            }
//            pastscene.save(flush: true);
//        }
//    }

//    def createObjectTypes() {
//        ObjectType inGame = new ObjectType();
//        inGame.setType("En jeu");
//        inGame.save(flush: true);
//        ObjectType simulated = new ObjectType();
//        simulated.setType("Simulé");
//        simulated.save(flush: true);
//        ObjectType offGame = new ObjectType();
//        offGame.setType("Hors jeu");
//        offGame.save(flush: true);
//    }

    def transfertUniversToTag() {
        int mycount = 0;
//        for (PlaceHasUnivers placeHasUnivers : PlaceHasUnivers.findAll()) {
//            Univers myunivers = placeHasUnivers.univers;
//            Place myplace = placeHasUnivers.place;
//            if (myunivers instanceof HibernateProxy) {
//                Hibernate.initialize(myunivers);
//                myunivers = (Univers) ((HibernateProxy) myunivers).getHibernateLazyInitializer().getImplementation();
//            }
//            Tag myTag = Tag.findByName(myunivers.getName());
//            if (myplace instanceof HibernateProxy) {
//                Hibernate.initialize(myplace);
//                myplace = (Place) ((HibernateProxy) myplace).getHibernateLazyInitializer().getImplementation();
//            }
//            PlaceHasTag existingTag = PlaceHasTag.findByTagAndPlace(myTag, myplace);
//            if (!existingTag) {
//                PlaceHasTag placeHasTag = new PlaceHasTag();
//                placeHasTag.setPlace(myplace);
//                placeHasTag.setVersion(placeHasUnivers.version);
//                placeHasTag.setLastUpdated(placeHasUnivers.lastUpdated);
//                placeHasTag.setDateCreated(placeHasUnivers.dateCreated);
//                placeHasTag.setWeight(placeHasUnivers.weight);
//                placeHasTag.setTag(myTag);
//                placeHasTag.save(flush:true);
//                mycount++;
//            }
//        }
//        for (ResourceHasUnivers resourceHasUnivers : ResourceHasUnivers.findAll()) {
//            Univers myunivers = resourceHasUnivers.univers;
//            Resource myresource = resourceHasUnivers.resource;
//            if (myunivers instanceof HibernateProxy) {
//                Hibernate.initialize(myunivers);
//                myunivers = (Univers) ((HibernateProxy) myunivers).getHibernateLazyInitializer().getImplementation();
//            }
//            Tag myTag = Tag.findByName(myunivers.getName());
//            if (myresource instanceof HibernateProxy) {
//                Hibernate.initialize(myresource);
//                myresource = (Resource) ((HibernateProxy) myresource).getHibernateLazyInitializer().getImplementation();
//            }
//            ResourceHasTag existingTag = ResourceHasTag.findByTagAndResource(myTag, myresource);
//            if (!existingTag) {
//                ResourceHasTag resourceHasTag = new ResourceHasTag();
//                resourceHasTag.setResource(myresource);
//                resourceHasTag.setVersion(resourceHasUnivers.version);
//                resourceHasTag.setLastUpdated(resourceHasUnivers.lastUpdated);
//                resourceHasTag.setDateCreated(resourceHasUnivers.dateCreated);
//                resourceHasTag.setWeight(resourceHasUnivers.weight);
//                resourceHasTag.setTag(myTag);
//                resourceHasTag.save(flush:true);
//                mycount++;
//            }
//        }
//        for (PlotHasUnivers plotHasUnivers : PlotHasUnivers.findAllByPlot(Plot.findById(111))) {
//            Univers myunivers = plotHasUnivers.univers;
//            Plot myPlot = plotHasUnivers.plot;
//            if (myunivers instanceof HibernateProxy) {
//                Hibernate.initialize(myunivers);
//                myunivers = (Univers) ((HibernateProxy) myunivers).getHibernateLazyInitializer().getImplementation();
//            }
//            Tag myTag = Tag.findByName(myunivers.getName());
//            if (myPlot instanceof HibernateProxy) {
//                Hibernate.initialize(myPlot);
//                myPlot = (Plot) ((HibernateProxy) myPlot).getHibernateLazyInitializer().getImplementation();
//            }
//            if (myPlot.id == 111) {
//                PlotHasTag existingTag = PlotHasTag.findByTagAndPlot(myTag, myPlot);
//                if (!existingTag) {
//                    PlotHasTag plotHasTag = new PlotHasTag();
//                    plotHasTag.setPlot(myPlot);
//                    plotHasTag.setVersion(plotHasUnivers.version);
//                    plotHasTag.setLastUpdated(plotHasUnivers.lastUpdated);
//                    plotHasTag.setDateCreated(plotHasUnivers.dateCreated);
//                    plotHasTag.setWeight(plotHasUnivers.weight);
//                    plotHasTag.setTag(myTag);
//                    plotHasTag.save(flush:true);
//                    mycount++;
//                }
//            }
//        }
//        mycount++;
//        for (Univers univers : Univers.findAll()) {
//            univers.delete();
//        }
    }

//    def transfertTagToNewTag()
//    {
//            for (Tag t : Tag.findAll())
//            {
//                Tag nt = new Tag()
//                Tag newtag = new Tag()
//                int id = t.id
//                newtag.setId(id)
//                newtag.setName(t.name)
//                newtag.setDateCreated(t.getDateCreated())
//                newtag.setLastUpdated(t.getLastUpdated())
//                newtag.setVersion(t.getVersion())
//                RoleHasTag.findAllWhere(tag: t)
//
//                newtag.save()
//            }
//        }
//
//    def transfertTagFamilyToTag()
//    {
//        HashMap<TagFamily, Tag> tab = new HashMap<TagFamily, Tag>()
//        List<TagFamily> l = TagFamily.findAll()
//
//        for(TagFamily i: l) {
//            Tag nt = new Tag()
//            nt.setName(i.getValue())
//            nt.setVersion(i.getVersion())
//            nt.setDateCreated(i.getDateCreated())
////            nt.parent(null)
//            nt.setLastUpdated(i.getLastUpdated())
//            nt.save()
//
//            for (Tag t: i.tags)
//            {
//                Tag children = Tag.findWhere(id: t.id)
//                children.setParent(nt)
//                children.save()
//            }
//
//            tab.put(i, nt)
//
//            TagRelevant tr = new TagRelevant()
//            tr.setTag(nt)
//            tr.setRelevantFirstname(i.getRelevantFirstname())
//            tr.setRelevantLastname(i.getRelevantLastname())
//            tr.setRelevantPlace(i.getRelevantPlace())
//            tr.setRelevantPlot(i.getRelevantPlot())
//            tr.setRelevantResource(i.getRelevantResource())
//            tr.setRelevantRole(i.getRelevantRole())
//
//            tr.save()
//        }
//
//        for (Map.Entry<TagFamily, Tag> i: tab.entrySet())
//        {
//            i.getValue().setParent(tab.get(i.getKey().getTagFamilyParent()))
//            i.getValue().save()
//        }
//    }
}
