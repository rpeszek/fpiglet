package fun.lists_as

import groovy.lang.Closure;

class TokenizedStrings_AsFunListFunctions extends Base_AsFunListFunctions {

	@Override
	public Object getDataIn() {
		'a,b,c'
	}

	@Override
	public void assertEmpty(Object data) {
		assert (data instanceof String) && !data
	}

	@Override
	public Object getModule() {
		fpig.string.asfunlist.functions.TokenizedStringAsFunList.withToken(',')
	}
	@Override
	Closure getToList(){
		{String string-> string.split(',')}
	}
}
