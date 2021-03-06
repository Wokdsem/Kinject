package com.wokdsem.kinject.codegen;

class MapperNames {

	private static final String SEPARATOR = "$$";
	private static final String MODULE_MAPPER = "%sMapper";
	private static final String MODULE_ADAPTER = "%s" + SEPARATOR + "ModuleAdapter";
	private static final String PROVIDE_ADAPTER = "bind%s" + SEPARATOR + "%s";

	static String getSimpleMapperModuleName(String className) {
		return String.format(MODULE_MAPPER, getSimpleName(className));
	}

	static String getSimpleAdapterModuleName(String className) {
		return String.format(MODULE_ADAPTER, getSimpleName(className));
	}

	static String getCanonicalAdapterModuleName(String className) {
		return String.format(MODULE_ADAPTER, className);
	}

	static String getBindMethodName(String className, String named) {
		return String.format(PROVIDE_ADAPTER, adaptClassName(className), capitalize(named));
	}

	static String getFieldName(String className) {
		return unCapitalize(adaptClassName(className));
	}

	private static String adaptClassName(String className) {
		StringBuilder builder = new StringBuilder();
		if (className != null) {
			for (String split : className.split("\\.")) {
				builder.append(capitalize(split));
			}
		}
		return builder.toString();
	}

	private static String getSimpleName(String fullName) {
		return fullName == null ? null : fullName.substring(fullName.lastIndexOf(".") + 1);
	}

	private static String capitalize(String s) {
		if (s == null || s.isEmpty()) {
			return "";
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	private static String unCapitalize(String s) {
		if (s == null || s.isEmpty()) {
			return "";
		}
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

}
