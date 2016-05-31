package org.gnk.substitution.data

class Character {
	String id
	String gender
    String type
	List<Tag> tagList
    // unused for now
	//List<RoleCharacter> roleList
	def roleList
	List<RelationCharacter> relationList
}
