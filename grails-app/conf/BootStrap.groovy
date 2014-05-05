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
        SecRole userRole = SecRole.findByAuthority("ROLE_USER") ?: new SecRole(authority: "ROLE_USER").save(failOnError: true)

        // USER
        User admin = User.findByUsername("admin@gnk.com");
        User user  = User.findByUsername("user@gnk.com");
        User user1 = User.findByUsername("user1@gnk.com")

        if (!user) {
            user = new User()
            user.username = "user@gnk.com"
            user.firstname = "Nico"
            user.lastname = "Sotty"
            user.password = "1"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }

        if (!user1) {
            user1 = new User()
            user1.username = "user1@gnk.com"
            user1.firstname = "Nico"
            user1.lastname = "Sotty"
            user1.password = "2"
            user1.enabled = true
            user1.gright = 0
            user1.lastConnexion = new Date()
            user1.countConnexion = 0
            user1.save(failOnError: true)
        }

        if (!admin) {
            admin = new User();

			admin.username = "admin@gnk.com"
            admin.firstname = "John"
            admin.lastname = "Doe"
            admin.password = "admin"
            admin.enabled = true
            admin.gright = 131072
            admin.countConnexion = 0
            admin.lastConnexion = new Date()
            admin.save(failOnError: true)
		}

        if (!admin.getAuthorities().contains(adminRole)) {
            UserSecRole.create admin, adminRole
        }
        if (!user.getAuthorities().contains(userRole)) {
            UserSecRole.create(user, userRole, true)
        }
        if (!user1.getAuthorities().contains(userRole)) {
            UserSecRole.create(user1, userRole, true)
        }
    }

    def destroy = {}

}
