package org.gnk.social

import org.gnk.administration.ErrorHandlerController
import org.gnk.naming.Convention
import org.gnk.naming.ConventionHasRule
import org.gnk.naming.Rule
import org.springframework.security.access.annotation.Secured

import java.awt.Graphics

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class SocialController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "listConventionRule", params: params)
    }

    def listConventionRule(Integer max, Integer offset, String sort) {
        //max = max ?: 10
        //offset = offset ?: 0
        //sort = sort ?: 'name'

        [ruleList: Rule.list(), conventionList: Convention.list()]
    }

    def editRelevantRule(int id) {
        Convention convention = Convention.findById(id)
        for (r in Rule.list()) {
            if (params.rule."${r.id}".checkBox) { //<-- this will return 'on' or ''
                println("you selected: ${r.description}");
                if (!convention.getConventionHasRules().rule.contains(r)) {
                    ConventionHasRule chr = new ConventionHasRule()
                    chr.setRule(r)
                    chr.setConvention(convention)
                    chr.save(flush: true)
                }
            } else {
                println("you DID NOT select: ${r.description}");
                ErrorHandlerController.RuleraiseError();
                if (convention.getConventionHasRules().rule.contains(r)) {
                    for (chr in ConventionHasRule.list()) {
                        if (chr.rule.equals(r)) {
                            convention.getConventionHasRules().remove(chr)
                            r.getConventionHasRules().remove(chr)
                            chr.delete()
                            break;
                        }
                    }
                }
            }
        }
        redirect(action: "listConventionRule")
    }

    def create() {

        //[tagInstance: new Tag(params)]
    }

    def save() {
        if (params.name.equals("")) {
            print "Invalid params"
            flash.message = message(code: 'Erreur : Le nom de la Convention ne peut etre vide !')
            redirect(action: "listConventionRule")
            return
        }

        Convention conventionInstance = new Convention()
        conventionInstance.description = params.name

        for (Convention convention : Convention.list()) {
            if (conventionInstance.description.toLowerCase().equals(convention.description.toLowerCase())) {
                print "Convention already exist"
                flash.message = message(code: 'Erreur : Une convention avec le meme nom existe deja !')
                redirect(action: "listConventionRule")
                return
            }
        }

        if (!conventionInstance.save(flush: true)) {
            redirect(action: "listConventionRule")
            return
        }
        flash.messageInfo = message(code: 'La convention a été crée !')
        redirect(action: "listConventionRule")
    }

    def delete(Long id) {

    }

    def deleteConvention(Long idConvention) {
        def conventionInstance = Convention.get(idConvention)
        print "name " + conventionInstance.description
        for (chr in conventionInstance.getConventionHasRules()) {
            if (chr.rule.getConventionHasRules().convention.contains(conventionInstance)) {
                def rule = chr.rule
                rule.getConventionHasRules().remove(chr)
                conventionInstance.delete()
                chr.delete()
                break;
            }
        }

        flash.messageInfo = message(code: 'La convention a été supprimé !')
        redirect(action: "listConventionRule")
    }

}
