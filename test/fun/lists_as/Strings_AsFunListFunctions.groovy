package fun.lists_as

import groovy.lang.Closure;

class Strings_AsFunListFunctions extends Base_AsFunListFunctions {

	@Override
	public Object getDataIn() {
		'abc'
	}

	@Override
	public void assertEmpty(Object data) {
		assert (data instanceof String) && !data
	}

	@Override
	public Object getModule() {
		fpig.string.asfunlist.functions.StringAsFunList
	}
	@Override
	Closure getToList(){
		{String string-> string.getChars() as List}
	}
}
