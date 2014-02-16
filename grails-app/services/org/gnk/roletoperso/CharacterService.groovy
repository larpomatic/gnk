package org.gnk.roletoperso

class CharacterService {
	
	List<Character> characters = []
	int id = 1;
	
	void addCharacter(Character c) {
		characters.add(c)
		c.DTDId = getNextId()
	}
	
	Character getCharacter(int id) {
		return characters.find { Character c ->
			c.DTDId == id
		}
	}
	
	void removeCharacter(int id) {
		Character c = getCharacter(id)
		characters.remove(c)
	}
	
	int getNextId(){
		return id++;
	}
}
