package fun.lists_as

import groovy.lang.Closure;

class GroovyLists_AsFunListFunctions extends Base_AsFunListFunctions {

	@Override
	public Object getDataIn() {
		['a','b','c']
	}

	@Override
	public void assertEmpty(Object data) {
		assert (data instanceof List) && !data
	}

	@Override
	public Object getModule() {
		fpig.groovylist.asfunlist.functions.GroovyListAsFunList
	}
	@Override
	Closure getToList(){
		{list->list}
	}
}
