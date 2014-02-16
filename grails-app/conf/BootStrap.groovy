import grails.plugins.springsecurity.SpringSecurityService
import org.gnk.tag.TagFamily
import org.gnk.user.SecRole
import org.gnk.user.User
import org.gnk.user.UserSecRole

class BootStrap {
	
	SpringSecurityService springSecurityService

    def init = { servletContext ->

        // TAG FAMILY
        if (!TagFamily.findByValue("Age")) {
            TagFamily tagFamily = new TagFamily();
            tagFamily.value = "Age"
            tagFamily.save(failOnError: true)
        }

        // TAG
//        ResourceTag resourceTag
//        if (!Tag.findByName("Jeune")) {
//            Tag tag = new Tag();
//            tag.name = "Jeune"
//            tag.tagFamily = TagFamily.findByValue("Age")
//            tag.save(failOnError: true)
//            resourceTag = new ResourceTag()
//            resourceTag.tag = tag
//            resourceTag.save()
//        }

        // RESOURCE
//        if (!Resource.findByName("Statuette")) {
//            Resource resource = new Resource();
//            resource.name = "Statuette"
//            resource.description = "Statuette d'un enfant"
//            resource.gender = "F"
//
//            resource.save(failOnError: true)
//        }

        // ROLE
        SecRole adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority: "ROLE_ADMIN").save(failOnError: true)
        SecRole.findByAuthority("ROLE_USER") ?: new SecRole(authority: "ROLE_USER").save(failOnError: true)

        // USER
        User admin = User.findByUsername("admin@gnk.com");

        if (!admin) {
            admin = new User();

			admin.username = "admin@gnk.com"
            admin.firstname = "John"
            admin.lastname = "Doe"
            admin.password = "admin"
            admin.enabled = true

            admin.save(failOnError: true)
		}

        if (!admin.getAuthorities().contains(adminRole)) {
            UserSecRole.create admin, adminRole
        }

    }

    def destroy = {}

}
