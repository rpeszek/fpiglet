package fun.lists_as

import groovy.lang.Closure;

class InputStream_AsFunListFunctions extends Base_AsFunListFunctions {

	@Override
	public Object getDataIn() {
		def data = 'a\nb\nc'
		InputStream input = new ByteArrayInputStream(data.getBytes())
		input
	}

	@Override
	public void assertEmpty(Object is) {
		assert (data instanceof InputStream)
		def myReader = new InputStreamReader(is)
		!(myReader.readLines())
	}

	@Override
	public Object getModule() {
		fpig.io.asfunlist.functions.TokenizedInputStreamAsFunList.withToken('\n')
	}
	@Override
	Closure getToList(){
		{InputStream is -> 
			def myReader = new InputStreamReader(is)
			myReader.readLines()
		}
	}
}
