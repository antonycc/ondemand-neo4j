package uk.co.polycode.neo4j;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * On-demand Neo4j is an exploration of Neo4j with deployment to AWS
 * Copyright (C) 2022  Antony Cartwright, Polycode Limited
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0 for more details.
 */
public class MappingUtils {

	private static final Reflections reflections = new Reflections("uk.co.polycode.neo4j.export");

	private static final Map<Object, Method> fromMethods = new HashMap<>();

	public static Object mapToExportable(Object o) {
		try {
			Object obj = findFromMethod(o).invoke(null, o);
			return obj;
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't invoke from method with " + o.getClass().getName(), e);
		}
	}

	// TODO: Reinstate method caching and check performance change
	public static Method findFromMethod(Object fromObject) {
		//if(fromMethods.containsKey(fromObject)) {
		//	return fromMethods.get(fromObject);
		//}else {
			Optional<Class<?>> toClass = reflections.get(TypesAnnotated.with(ExportableFor.class).asClass()).stream()
					//.filter(candidateToClass -> hasExportableForAnnotationWithClass(fromObject, candidateToClass))
					.filter(candidateToClass -> hasFrom(fromObject, candidateToClass))
					.findFirst();
			if (toClass.isEmpty()) {
				throw new IllegalArgumentException("No toClass found with from(" + fromObject.getClass().getName() + ")");
			} else {
				try {
					Method fromMethodOnToClass = toClass.get().getMethod("from", fromObject.getClass());
					//fromMethods.put(fromObject, fromMethodOnToClass);
					return fromMethodOnToClass;
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException("Could not access from method on " + toClass.get().getName(), e);
				}
			}
		//}
	}

	private static boolean hasExportableForAnnotationWithClass(final Object from, final Class<?> candidateToClass) {
		return Arrays.stream(candidateToClass.getDeclaredFields())
				.anyMatch(field -> field.isAnnotationPresent(ExportableFor.class) &&
						field.getAnnotation(ExportableFor.class).clazz() == candidateToClass);
	}

	private static boolean hasFrom(final Object from, final Class<?> candidateToClass) {
		try {
			return candidateToClass.getMethod("from", from.getClass()) != null;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	public static Object reflectionDeepCopy(Object from, Object to) {
		return reflectionDeepCopy(from, to, new HashMap<Object, Object>());
	}

	// TODO: Simplify this method after full set of functional tests and a coverage check.
	public static Object reflectionDeepCopy(Object from, Object to, Map<Object, Object> references) {
		if(references.containsKey(from)) {
			return references.get(from);
		}else {
			references.put(from, to);
			Arrays.stream(from.getClass().getDeclaredFields()).forEach(fromField -> {
				try {
					fromField.setAccessible(true);
					Object fromFieldValue = fromField.get(from);
					if(fromFieldValue != null) {
						Field toField = getField(to, fromField);
						ExportableToListOf toFieldExportableToListOf = toField.getAnnotation(ExportableToListOf.class);
						if(fromField.getName().equals("famousPerson") || fromField.getName().equals("birthPlace")) { // && toFieldType.getName().contains("export")) {
							System.out.println("Mapping " + fromField.getName());
						}
						if(fromFieldValue.getClass().getName().contains("data")) { // && toFieldType.getName().contains("export")) {
							System.out.println("Mapping " + fromField + " to " + (toField != null ? toField : "null"));
						}
						if(toField != null) {
							toField.setAccessible(true);
							Class<?> toFieldType = toField.getType();
							if (hasFrom(fromFieldValue, toFieldType)) {
								Object toFieldValue = toFieldType.getDeclaredConstructor().newInstance();
								toField.set(to, reflectionDeepCopy(fromFieldValue, toFieldValue, references));
							} else if (toFieldType.isArray()) {
								// TODO: Does this work when the array if of an export type
								int length = Array.getLength(fromFieldValue);
								Object toFieldValue = Array.newInstance(toFieldType.getComponentType(), length);
								toField.set(to, reflectionDeepCopy(fromFieldValue, toFieldValue, references));
							} else if (fromFieldValue instanceof Iterable && toFieldExportableToListOf != null) {
								Iterable fromFieldValueIterable = (Iterable) fromFieldValue;
								ArrayList toFieldValueIterable = new ArrayList<>();
								fromFieldValueIterable.forEach(fromFieldValueIterableItem -> {
									try {
										Object toFieldValueIterableItem = toFieldExportableToListOf.clazz().getDeclaredConstructor().newInstance();
										if(hasFrom(fromFieldValueIterableItem, toFieldExportableToListOf.clazz())) {
											toFieldValueIterable.add(reflectionDeepCopy(fromFieldValueIterableItem, toFieldValueIterableItem, references));
										}else{
											toFieldValueIterable.add(fromFieldValueIterableItem);
										}
									} catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
									         InvocationTargetException e) {
										throw new IllegalArgumentException("Could not create collection for " + fromField.getName(), e);
									}
								});
								toField.set(to, toFieldValueIterable);
//	TODO: Support maps} else if (fromFieldValue instanceof Map) {
							} else if (toField.getType().isAssignableFrom(fromField.getType())) {
								toField.set(to, fromFieldValue);
								if(fromFieldValue.getClass().getName().contains("data")) {
									System.out.println("Should be mapping for type " + fromFieldValue.getClass().getName());
								}
							} else {
								throw new IllegalArgumentException("Can't assign " + fromFieldValue.getClass().getName() + " to " + toField.getType().getName());
							}
						}
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("Could not access field " + fromField.getName(), e);
				}
			});
			String toClassName = to.getClass().getName();
			System.out.println(toClassName);
			return to;
		}
	}

	@NotNull
	private static Field getField(final Object to, final Field fromField) throws NoSuchFieldException {
		try {
			return to.getClass().getDeclaredField(fromField.getName());
		} catch (NoSuchFieldException e) {
			if(to.getClass().getSuperclass() != null) {
				// TODO: Iterate all the way up
				return to.getClass().getSuperclass().getDeclaredField(fromField.getName());
			} else {
				throw new NoSuchFieldException("No field found for " + fromField.getName());
			}
		}
	}

}
