package org.gnk.tag

import java.util.List
import org.gnk.tag.Tag

class TagService {

	def serviceMethod() {
	}

	def List<Tag> getPlotTagQuery() {
		def result = Tag.withCriteria{
			tagFamily { eq ("relevantPlot", true) }
		}

		return result;
	}

	def List<Tag> getRoleTagQuery() {
		def result = Tag.withCriteria{
			tagFamily { eq ("relevantPlot", true) }
		}

		return result;
	}
}
