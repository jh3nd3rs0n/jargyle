package jargyle.server;

public final class XmlBindHelper {

	private static final String NO_OPTIMIZE_PROPERTY_NAME = 
			"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize";
	
	public static boolean isOptimizedCodeGenerationDisabled() {
		String property = System.getProperty(NO_OPTIMIZE_PROPERTY_NAME);
		return property != null && property.equals("true");
	}
	
	/* 
	 * https://stackoverflow.com/questions/50237516/proper-fix-for-java-10-complaining-about-illegal-reflection-access-by-jaxb-impl#50251510
	 */
	public static void setOptimizedCodeGenerationDisabled(final boolean b) {
		System.setProperty(NO_OPTIMIZE_PROPERTY_NAME, Boolean.toString(b));
	}
	
	private XmlBindHelper() { }
	
}
