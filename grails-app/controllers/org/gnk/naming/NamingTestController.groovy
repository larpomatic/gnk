package org.gnk.naming

import 	com.gnk.substitution.Tag

class NamingTestController {
	
	NamingService namingService

    def index() { }
	
	def test1() {
		
		render 'Bonjour !'
	}
	
	def test2() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<String>();
		List<String> family = new LinkedList<String>();
		 
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		render perso1.toString()
	}
	
	def test3() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		
		LinkedList persolist = new LinkedList()
		persolist.add(perso1)
		
		persolist = namingService.namingMethod(persolist)
			
		render persolist.first().toString()
	}
	
	def test4() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<String>();
		List<String> family = new LinkedList<String>();
		 
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')
		
		Tag tag = new Tag('french', 'Name Groupe Linguistique', 80, '')
		tags.add(tag)

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		render perso1.toString()
	}
	
	def test5() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		
		Tag tag = new Tag('Vil', 'Trait de personnalité', 80, null)
		tags.add(tag)
		
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		
		LinkedList persolist = new LinkedList()
		persolist.add(perso1)
		
		persolist = namingService.namingMethod(persolist)
			
		render persolist.first().toString()
	}
	
	def test6() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<String>();
		List<String> family = new LinkedList<String>();
		 
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')
		
		Tag tag = new Tag('anglais', 'Name Groupe Linguistique', 80, '')
		tags.add(tag)

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		render perso1.toString()
	}
	
	def test7() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		
		Tag tag = new Tag('anglais', 'Name Groupe Linguistique', 80, null)
		tags.add(tag)
		
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		
		LinkedList persolist = new LinkedList()
		persolist.add(perso1)
		
		persolist = namingService.namingMethod(persolist)
			
		render persolist.first().toString()
	}
	
	def test8() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<String> bannedFirstnames2 = new LinkedList<String>()
		List<String> selectedFirstnames2 = new LinkedList<String>()
		List<String> bannedNames2 = new LinkedList<String>()
		List<String> selectedNames2 = new LinkedList<String>()
		List<Tag> tags = new LinkedList<String>();
		List<Tag> tags2 = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		List<String> family2 = new LinkedList<String>();
		 
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')
		bannedFirstnames2.add('Abraxas')
		bannedNames2.add('Achard')
		
		Tag tag = new Tag('french', 'Name Groupe Linguistique', 80, '')
		tags.add(tag)
		tags2.add(tag)
		family2.add('A')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		PersoForNaming perso2 = new PersoForNaming ('B', 'm', 'Grèce Antique (Univers)', tags2, bannedNames2, selectedNames2,
			bannedFirstnames2, selectedFirstnames2, true, true, family2)
		
		render ( perso1.toString() + '<br/>' +  perso2.toString() );
	}
	
	def test9() {
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<String> bannedFirstnames2 = new LinkedList<String>()
		List<String> selectedFirstnames2 = new LinkedList<String>()
		List<String> bannedNames2 = new LinkedList<String>()
		List<String> selectedNames2 = new LinkedList<String>()
		List<Tag> tags = new LinkedList<Tag>();
		List<Tag> tags2 = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		List<String> family2 = new LinkedList<String>();
		
		Tag tag = new Tag('french', 'Name Groupe Linguistique', 80, null)
		tags.add(tag)
		tags2.add(tag)
		family2.add('A')
		
		bannedFirstnames.add('Abraxas')
		bannedNames.add('Achard')
		bannedFirstnames2.add('Abraxas')
		bannedNames2.add('Achard')

		PersoForNaming perso1 = new PersoForNaming('A', 'm', 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
		PersoForNaming perso2 = new PersoForNaming ('B', 'm', 'Grèce Antique (Univers)', tags2, bannedNames2, selectedNames2,
			bannedFirstnames2, selectedFirstnames2, true, true, family2)
		
		LinkedList persolist = new LinkedList()
		persolist.add(perso1)
		persolist.add(perso2)
		
		persolist = namingService.namingMethod(persolist)
			
		render (persolist.first().toString() + '<br/>' + persolist.last().toString() )
	}
	
	def test10() {
		PersoForNaming perso1 = createPerso('A', 'f')
		PersoForNaming perso2 = createPerso('B', 'f')
		PersoForNaming perso3 = createPerso('C', 'm')
		PersoForNaming perso4 = createPerso('D', 'm')
		PersoForNaming perso5 = createPerso('E', 'f')
		PersoForNaming perso6 = createPerso('F', 'f')
        PersoForNaming perso7 = createPerso('G', 'f')
        PersoForNaming perso8 = createPerso('h', 'f')
        PersoForNaming perso9 = createPerso('i', 'f')
        PersoForNaming perso10 = createPerso('j', 'f')
        PersoForNaming perso11 = createPerso('k', 'f')
        PersoForNaming perso12 = createPerso('l', 'f')
        PersoForNaming perso13 = createPerso('m', 'f')
        PersoForNaming perso14 = createPerso('n', 'f')
        PersoForNaming perso15 = createPerso('o', 'f')
        PersoForNaming perso16 = createPerso('p', 'f')
        PersoForNaming perso17 = createPerso('q', 'f')
        PersoForNaming perso18 = createPerso('r', 'f')
        PersoForNaming perso19 = createPerso('s', 'f')
        PersoForNaming perso20 = createPerso('t', 'f')
        PersoForNaming perso21 = createPerso('u', 'f')
        PersoForNaming perso22 = createPerso('v', 'f')
        PersoForNaming perso23 = createPerso('w', 'f')
		
		LinkedList persolist = new LinkedList()
		persolist.add(perso1)
		persolist.add(perso2)
		persolist.add(perso3)
		persolist.add(perso4)
		persolist.add(perso5)
		persolist.add(perso6)
        persolist.add(perso7)
        persolist.add(perso8)
        persolist.add(perso9)
        persolist.add(perso10)
        persolist.add(perso11)
        persolist.add(perso12)
        persolist.add(perso13)
        persolist.add(perso14)
        persolist.add(perso15)
        persolist.add(perso16)
        persolist.add(perso17)
        persolist.add(perso18)
        persolist.add(perso19)
        persolist.add(perso20)
        persolist.add(perso21)
        persolist.add(perso22)
        persolist.add(perso23)
		
		persolist = namingService.namingMethod(persolist)
		
		String res = new String()
		for (PersoForNaming perso : persolist)
			res += (perso.toString() + '<br/>')
		
		render res
	}
	
	private PersoForNaming createPerso (String code, String gender)
	{
		List<String> bannedFirstnames = new LinkedList<String>()
		List<String> selectedFirstnames = new LinkedList<String>()
		List<String> bannedNames = new LinkedList<String>()
		List<String> selectedNames = new LinkedList<String>()
		List<Tag> tags = new LinkedList<Tag>();
		List<String> family = new LinkedList<String>();
		
		return new PersoForNaming(code, gender, 'Grèce Antique (Univers)', tags, bannedNames, selectedNames,
			bannedFirstnames, selectedFirstnames, true, true, family)
	}
}
