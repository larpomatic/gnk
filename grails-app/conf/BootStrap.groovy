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
        User user1 = User.findByUsername("nicolas.sotty@gmail.com")
        User user2 = User.findByUsername("said.essaki@gmail.com")
        User user3 = User.findByUsername("v@v.fr")
        User user4 = User.findByUsername("jacnic@epita.fr")
        User user5 = User.findByUsername("pico.2607@gmail.com")
        User user6 = User.findByUsername("alex-belingard@live.fr")
        User user7 = User.findByUsername("mehdibest91@gmail.com")

        if (!user7){
            user = new  User()
            user.username = "mehdibest91@gmail.com"
            user.firstname = "Mehdi"
            user.lastname = "Baless"
            user.password = "mehdi"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }
        if (!user6){
            user = new  User()
            user.username = "alex-belingard@live.fr"
            user.firstname = "Alexandre"
            user.lastname = "Belingard"
            user.password = "test"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }
        if (!user5){
            user = new  User()
            user.username = "pico.2607@gmail.com"
            user.firstname = "Pierre"
            user.lastname = "Coppee"
            user.password = "gnk"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }

        if (!user4){
            user = new  User()
            user.username = "jacnic@epita.fr"
            user.firstname = "Nicolas"
            user.lastname = "Jaccoud"
            user.password = "jacnic"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }
        if (!user3){
            user = new  User()
            user.username = "v@v.fr"
            user.firstname = "Vincent"
            user.lastname = "Larroque"
            user.password = "lol"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }
        if (!user2){
            user = new  User()
            user.username = "said.essaki@gmail.com"
            user.firstname = "Said"
            user.lastname = "Essaki"
            user.password = "root"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }

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
            user1.username = "nicolas.sotty@gmail.com"
            user1.firstname = "Nicolas"
            user1.lastname = "Sotty"
            user1.password = "admin"
            user1.enabled = true
            user1.gright = 131072
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
        if (!user1.getAuthorities().contains(adminRole)) {
            UserSecRole.create(user1, adminRole, true)
        }
        if (!user2.getAuthorities().contains(userRole)) {
            UserSecRole.create(user2, userRole, true)
        }
        if (!user3.getAuthorities().contains(userRole)) {
            UserSecRole.create(user3, userRole, true)
        }
        if (!user4.getAuthorities().contains(userRole)) {
            UserSecRole.create(user4, userRole, true)
        }
        if (!user5.getAuthorities().contains(userRole)) {
            UserSecRole.create(user5, userRole, true)
        }
        if (!user6.getAuthorities().contains(userRole)) {
            UserSecRole.create(user6, userRole, true)
        }
        if (!user7.getAuthorities().contains(userRole)) {
            UserSecRole.create(user7, userRole, true)
        }
    }

    def destroy = {}

}
