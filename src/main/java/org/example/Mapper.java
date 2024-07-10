package org.example;

import java.beans.Introspector;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class Mapper {
    private final HashMap<Class<?>, HashMap<String, MetaData>> metaDataSetters;
    private final HashMap<Class<?>, HashMap<String, MetaData>> metaDataGetters;
    private HashSet<Class<?>> classHashSet;
    private HashSet<Class<?>> collectionClassHashSet;
    private static final String GET = "get";
    private static final String SET = "set";

    public Mapper() {
        metaDataGetters = new HashMap<>();
        metaDataSetters = new HashMap<>();
        createTypeSet();
    }

    public Mapper(ArrayList<Class<?>> classes) {
        metaDataGetters = new HashMap<>();
        metaDataSetters = new HashMap<>();
        createTypeSet();
        startUpClasses(classes);
    }

    public <T> T convert(Object objectIn, Class<T> typeOut) {
        if (metaDataGetters.get(objectIn.getClass()) == null)
            createMetaDataGet(objectIn.getClass());
        if (metaDataSetters.get(typeOut) == null)
            createMetaDataSet(typeOut);
        return convertStructure(objectIn, typeOut, metaDataGetters.get(objectIn.getClass()), metaDataSetters.get(typeOut));
    }

    private void createTypeSet() {
        classHashSet = new HashSet<>();
        classHashSet.add(String.class);
        classHashSet.add(long.class);
        classHashSet.add(Long.class);
        classHashSet.add(int.class);
        classHashSet.add(Integer.class);
        classHashSet.add(boolean.class);
        classHashSet.add(Date.class);
        classHashSet.add(Double.class);
        classHashSet.add(double.class);
        classHashSet.add(Character.class);
        classHashSet.add(char.class);
        classHashSet.add(Short.class);
        classHashSet.add(short.class);
        classHashSet.add(Float.class);
        classHashSet.add(float.class);

        collectionClassHashSet = new HashSet<>();
        collectionClassHashSet.add(List.class);
        collectionClassHashSet.add(Queue.class);
        collectionClassHashSet.add(Set.class);
    }

    private void startUpClasses(ArrayList<Class<?>> classes) {
        for (Class<?> startClass : classes) {
            createMetaDataGet(startClass);
            createMetaDataSet(startClass);
        }
    }

    private <T> T convertStructure(Object objectIn, Class<T> typeOut, HashMap<String, MetaData> hashMapIn, HashMap<String, MetaData> hashMapOut) {
        try {
            T objectOut = typeOut.getConstructor().newInstance();
            for (Map.Entry<String, MetaData> entry : hashMapOut.entrySet()) {
                MetaData metaDataIn = hashMapIn.get(entry.getKey());
                MetaData metaDataOut = hashMapOut.get((entry.getKey()));
                if (metaDataIn == null || metaDataOut == null)
                    continue;
                Object valueIn = metaDataIn.getMethod().invoke(objectIn);
                if (valueIn == null)
                    continue;
                if (metaDataOut.type.isArray()) {
                    if (metaDataOut.getMetaData() != null) {
                        Object[] o = convertArray(valueIn, metaDataIn, metaDataOut, metaDataOut.getGenericType());
                        entry.getValue().getMethod().invoke(objectOut, new Object[]{o});
                    } else {
                        entry.getValue().getMethod().invoke(objectOut, valueIn);
                    }
                } else if (valueIn instanceof Collection) {
                    if (metaDataOut.getMetaData() != null) {
                        Collection<?> objects = convertCollection(valueIn, metaDataIn, metaDataOut);
                        entry.getValue().getMethod().invoke(objectOut, objects);
                    } else {
                        entry.getValue().getMethod().invoke(objectOut, valueIn);
                    }
                } else {
                    if (metaDataOut.getMetaData() != null) {
                        valueIn = convertStructure(valueIn, metaDataOut.getType(), metaDataIn.getMetaData(), metaDataOut.getMetaData());
                    }
                    entry.getValue().getMethod().invoke(objectOut, valueIn);
                }
            }
            return objectOut;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <IN, OUT> OUT[] convertArray(Object valueIn, MetaData metaDataIn, MetaData metaDataOut, Class<IN> type) {
        Object[] valueInArray = (Object[]) valueIn;
        OUT[] newArray = (OUT[]) Array.newInstance(type, valueInArray.length);
        for (int i = 0; i < valueInArray.length; i++) {
            newArray[i] = (OUT) convertStructure(valueInArray[i], type, metaDataIn.getMetaData(), metaDataOut.getMetaData());
        }
        return newArray;
    }

    private Collection<?> convertCollection(Object valueIn, MetaData metaDataIn, MetaData metaDataOut) {
        if (metaDataOut.getType().equals(List.class)) {
            List<Object> newObjectList = new ArrayList<>();
            Collection<?> collection = (Collection<?>) valueIn;
            if (!collection.isEmpty()) {
                for (Object next : collection) {
                    Object o = convertStructure(next, metaDataOut.getGenericType(), metaDataIn.getMetaData(), metaDataOut.getMetaData());
                    newObjectList.add(o);
                }
            }
            return newObjectList;
        } else if (metaDataOut.getType().equals(Queue.class)) {
            Queue<Object> newObjectQueue = new LinkedList<>();
            Collection<?> collection = (Collection<?>) valueIn;
            if (!collection.isEmpty()) {
                for (Object next : collection) {
                    Object o = convertStructure(next, metaDataOut.getGenericType(), metaDataIn.getMetaData(), metaDataOut.getMetaData());
                    newObjectQueue.add(o);
                }
            }
            return newObjectQueue;
        } else if (metaDataOut.getType().equals(Set.class)) {
            // TODO: 8/8/2021 add variant with SET collection
            return null;
        }
        return null;
    }

    private Class<?> getGenericFromMethod(Method method, String setOrGet) {
        Type genericParameterType;
        if (setOrGet.equals(SET))
            genericParameterType = method.getGenericParameterTypes()[0];
        else
            genericParameterType = method.getGenericReturnType();
        ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    private void createMetaDataSet(Class<?> type) {
        if (metaDataSetters.get(type) == null) {
            metaDataSetters.put(type, addMetaDataStructure(type, SET));
        }
    }

    private void createMetaDataGet(Class<?> type) {
        if (metaDataGetters.get(type) == null) {
            metaDataGetters.put(type, addMetaDataStructure(type, GET));
        }
    }

    private HashMap<String, MetaData> addMetaDataStructure(Class<?> typeIn, String setOrGet) {
        HashMap<String, MetaData> hashMap = new HashMap<>();
        for (Method method : getMethodList(typeIn, setOrGet)) {
            MetaData metaData = new MetaData();
            Class<?> type = getType(method, setOrGet);
            if (collectionClassHashSet.contains(type)) {
                Class<?> genericFromMethod = getGenericFromMethod(method, setOrGet);
                metaData.setGenericType(genericFromMethod);
                if (!classHashSet.contains(genericFromMethod)) {
                    metaData.setMetaData(addMetaDataStructure(genericFromMethod, setOrGet));
                }
            } else if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                metaData.setGenericType(componentType);
                if (componentType != null && !classHashSet.contains(componentType)) {
                    metaData.setMetaData(addMetaDataStructure(componentType, setOrGet));
                }
            } else {
                if (!classHashSet.contains(type)) {
                    metaData.setMetaData(addMetaDataStructure(type, setOrGet));
                }
            }
            String paramName = Introspector.decapitalize(method.getName().substring(3));
            metaData.setParamName(paramName);
            metaData.setMethod(method);
            metaData.setType(type);
            hashMap.put(paramName, metaData);
        }
        return hashMap;
    }

    private List<Method> getMethodList(Class<?> type, String setOrGet) {
        return Arrays.stream(type.getMethods())
                .filter(x -> x.getName().startsWith(setOrGet) && !x.getName().contains("getClass"))
                .collect(Collectors.toList());
    }

    private Class<?> getType(Method method, String setOrGet) {
        if (setOrGet.equals(SET))
            return method.getParameterTypes()[0];
        else
            return method.getReturnType();
    }

    private class MetaData {

        private String paramName;
        private Method method;
        private Class<?> type;
        private Class<?> genericType;
        private HashMap<String, MetaData> metaData;

        public MetaData() {
        }

        public MetaData(String paramName, Method method, Class<?> type, Class<?> genericType) {
            this.paramName = paramName;
            this.method = method;
            this.type = type;
            this.genericType = genericType;
        }

        public MetaData(String paramName, Method method, Class<?> type) {
            this.paramName = paramName;
            this.method = method;
            this.type = type;
        }

        public MetaData(String paramName, Method method) {
            this.paramName = paramName;
            this.method = method;
        }

        public HashMap<String, MetaData> getMetaData() {
            return metaData;
        }

        public void setMetaData(HashMap<String, MetaData> metaData) {
            this.metaData = metaData;
        }

        public Class<?> getGenericType() {
            return genericType;
        }

        public void setGenericType(Class<?> genericType) {
            this.genericType = genericType;
        }

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }


    }
}
