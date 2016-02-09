package com.wokdsem.kinject.codegen;

class MapperNames {

	private static final String SEPARATOR = "$$";
	private static final String MODULE_MAPPER = "%s" + SEPARATOR + "ModuleMapper";
	private static final String MODULE_ADAPTER = "%s" + SEPARATOR + "ModuleAdapter";
	private static final String PROVIDE_ADAPTER = "bind%s" + SEPARATOR + "%s";

	public static String getSimpleMapperModuleName(String className) {
		return String.format(MODULE_MAPPER, getSimpleName(className));
	}

	public static String getCanonicalMapperModuleName(String className) {
		return String.format(MODULE_MAPPER, className);
	}

	public static String getMapperModuleName(String className) {
		return String.format(MODULE_MAPPER, adaptClassName(className));
	}

	public static String getSimpleAdapterModuleName(String className) {
		return String.format(MODULE_ADAPTER, getSimpleName(className));
	}

	public static String getCanonicalAdapterModuleName(String className) {
		return String.format(MODULE_ADAPTER, className);
	}

	public static String getAdapterModuleName(String className) {
		return String.format(MODULE_ADAPTER, adaptClassName(className));
	}

	public static String getBindMethodName(String className, String named) {
		return String.format(PROVIDE_ADAPTER, adaptClassName(className), capitalize(named));
	}

	public static String getFieldName(String className) {
		return unCapitalize(adaptClassName(className));
	}

	private static String adaptClassName(String className) {
		StringBuilder builder = new StringBuilder();
		if (className != null) {
			for (String split : className.split("\\.")) builder.append(capitalize(split));
		}
		return builder.toString();
	}

	private static String getSimpleName(String fullName) {
		return fullName == null ?
				null :
				fullName.substring(fullName.lastIndexOf(".") + 1);
	}

	private static String capitalize(String s) {
		if (s == null || s.isEmpty()) return "";
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	private static String unCapitalize(String s) {
		if (s == null || s.isEmpty()) return "";
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

}
