package org.gnk.naming

import org.gnk.gn.Gn
import org.gnk.gn.GnHasConvention
import org.gnk.substitution.data.RelationCharacter

class ConventionVisitorService {

    def serviceMethod() {

    }

    public List<List<Object>> visit(PersoForNaming perso, LinkedList<PersoForNaming> doneperso, Integer gnId){
        List<List<Object>> res = new LinkedList<LinkedList<Object>>()
        List<String> names = new LinkedList<String>()
        res.add(doneperso)
        res.add(names)
        Gn gn = Gn.get(gnId)
        GnHasConvention gnHasConvention = GnHasConvention.findWhere(gn: gn)
        Convention convention = Convention.get(gnHasConvention.convention.id)

        LinkedList<ConventionHasRule> convRules = ConventionHasRule.findAllWhere(convention: convention)
        List<Integer> rules = new ArrayList<Integer>()
        for(ConventionHasRule cr : convRules){
            rules.add(cr.ruleId)
        }

        if((names.empty) && (rules.contains(5) || rules.contains(6))){names = visit1(perso, doneperso, true)}
        if((names.empty) && (rules.contains(1) || rules.contains(2))){names = visit1(perso, doneperso, false)}
        if((names.empty) && (rules.contains(7) || rules.contains(8))){res = visit2(perso, doneperso, "H")}
        if((names.empty) && (rules.contains(9) || rules.contains(10))){res = visit2(perso, doneperso, "F")}

        res[1] = names
        return res
    }

    List<String> visit1(PersoForNaming perso, LinkedList<PersoForNaming> doneperso, boolean hidden) {
        List<String> names = new LinkedList<String>()
        for (RelationCharacter relation : perso.relationList){
            if (hidden && relation.isHidden){
                break
            }
            if (relation.type.equals("Filiation")
                    || relation.type.equals("Parent (direct)")){
                for(PersoForNaming p : doneperso){
                    if (hidden){
                        if(p.code.equals(relation.role2.toString())){
                            return p.selectedNames
                        }
                    }
                    else{
                        if(p.code.equals(relation.role2.toString()) && (perso.getgender()).equals("M")){
                            return p.selectedNames
                        }
                    }
                }
            }
            else if (relation.type.equals("Mariage")){
                for(PersoForNaming p : doneperso){
                    if (hidden){
                        if(p.code.equals(relation.role2.toString())){
                            return p.selectedNames
                        }
                    }
                    else{
                        if(p.code.equals(relation.role2.toString()) && (perso.getgender()).equals("M")){
                            return p.selectedNames
                        }
                    }
                }
            }
        }
        return names
    }

    List<List<Object>> visit2(PersoForNaming perso, LinkedList<PersoForNaming> doneperso, String parent) {
        List<List<Object>> res = new LinkedList<LinkedList<Object>>()
        List<String> names = new LinkedList<String>()
        List<String> tmp = new LinkedList<String>()
        LinkedList<PersoForNaming> dp = doneperso
        Integer n = 0
        for (RelationCharacter relation : perso.relationList){
            if (relation.type.equals("Filiation")){
                for(PersoForNaming p : doneperso){
                    if(p.code.equals(relation.role2.toString()) && (perso.getgender()).equals(parent)){
                        for(String s : p.selectedFirstnames){
                            if((perso.getgender()).equals("F")){
                                names.add(s + "sdottir")
                            }
                            else{
                                names.add(s + "sson")
                            }
                        }
                        res.add(dp)
                        res.add(names)
                        return res
                    }
                }
            }/*else if (relation.type.equals("Parent (direct)")){
                for(PersoForNaming p : doneperso){
                    if(p.code.equals(relation.role2.toString())){
                        for(String s : p.selectedNames){
                            //print(s)
                            if(s.contains("sdottir")){
                                names.add(s.substring(0, s.indexOf("sdottir")))
                                //print(s.indexOf("sdottir"))
                            }
                            else if(s.contains("sson")){
                                names.add(s.substring(0, s.indexOf("sson")))
                                //print(s.indexOf("sson"))
                            }
                            else{
                                if((p.getgender()).equals("F")){
                                    tmp.add(s + "sdottir")
                                }
                                else{
                                    tmp.add(s + "sson")
                                }
                            }
                        }
                        if(p.selectedNames[0].contains("sdottir") || p.selectedNames[0].contains("sson")){
                            res.add(dp)
                            res.add(names)
                        }
                        else{
                            names = dp[n].selectedNames
                            dp[n].selectedNames = tmp
                            res.add(dp)
                            res.add(names)
                        }
                        return res
                    }
                    n++
                }
            }*/
        }
        res.add(dp)
        res.add(names)
        return res
    }

    void visit3(String x) {
        println("C")
    }
}
