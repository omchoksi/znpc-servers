package io.github.znetworkw.znpcservers.cache;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface TypeCache {
    public abstract static class BaseCache<T> {
        private static final Logger LOGGER = Logger.getLogger(TypeCache.BaseCache.class.getName());
        protected final TypeCache.CacheBuilder cacheBuilder;
        protected Class<?> BUILDER_CLASS;
        private T cached;

        protected BaseCache(TypeCache.CacheBuilder cacheBuilder) {
            this.cacheBuilder = cacheBuilder;

            String classes;
            try {
                for(Iterator var2 = cacheBuilder.className.iterator(); var2.hasNext(); this.BUILDER_CLASS = Class.forName(classes)) {
                    classes = (String)var2.next();
                }
            } catch (ClassNotFoundException var4) {
            }

        }

        public T load() {
            try {
                if (this.BUILDER_CLASS == null) {
                    throw new IllegalStateException();
                } else {
                    T eval = this.cached != null ? this.cached : (this.cached = this.onLoad());
                    if (eval == null) {
                        throw new NullPointerException();
                    } else {
                        return eval;
                    }
                }
            } catch (Throwable var2) {
                if (var2 instanceof IllegalStateException) {
                    this.log("No cache found for: " + this.cacheBuilder.className);
                }

                this.log("No cache found for: " + this.cacheBuilder.className + " : " + this.cacheBuilder.methods.toString());
                return null;
            }
        }

        private void log(String message) {
            LOGGER.log(Level.WARNING, message);
        }

        protected abstract T onLoad() throws Exception;

        public static class EnumLoader extends TypeCache.BaseCache<Enum<?>[]> {
            public EnumLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Enum<?>[] onLoad() {
                Enum<?>[] enumConstants = (Enum[])this.BUILDER_CLASS.getEnumConstants();
                Enum[] var2 = enumConstants;
                int var3 = enumConstants.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Enum<?> enumConstant = var2[var4];
                    TypeCache.ClassCache.register(enumConstant.name(), enumConstant, this.BUILDER_CLASS);
                }

                return enumConstants;
            }
        }

        public static class ConstructorLoader extends TypeCache.BaseCache<Constructor<?>> {
            public ConstructorLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Constructor<?> onLoad() throws NoSuchMethodException {
                Constructor<?> constructor = null;
                if (Iterables.size(this.cacheBuilder.parameterTypes) > 1) {
                    Iterator var2 = this.cacheBuilder.parameterTypes.iterator();

                    while(var2.hasNext()) {
                        Class[] keyParameters = (Class[])var2.next();

                        try {
                            constructor = this.BUILDER_CLASS.getDeclaredConstructor(keyParameters);
                        } catch (NoSuchMethodException var5) {
                        }
                    }
                } else {
                    constructor = Iterables.size(this.cacheBuilder.parameterTypes) > 0 ? this.BUILDER_CLASS.getDeclaredConstructor((Class[])Iterables.get(this.cacheBuilder.parameterTypes, 0)) : this.BUILDER_CLASS.getDeclaredConstructor();
                }

                if (constructor != null) {
                    constructor.setAccessible(true);
                }

                return constructor;
            }
        }

        public static class FieldLoader extends TypeCache.BaseCache<Field> {
            public FieldLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Field onLoad() throws NoSuchFieldException {
                if (this.cacheBuilder.expectType != null) {
                    Field[] var1 = this.BUILDER_CLASS.getDeclaredFields();
                    int var2 = var1.length;

                    for(int var3 = 0; var3 < var2; ++var3) {
                        Field field = var1[var3];
                        if (field.getType() == this.cacheBuilder.expectType) {
                            field.setAccessible(true);
                            return field;
                        }
                    }
                }

                Field field = this.BUILDER_CLASS.getDeclaredField(this.cacheBuilder.fieldName);
                field.setAccessible(true);
                return field;
            }

            public TypeCache.BaseCache.FieldLoader.AsValueField asValueField() {
                return new TypeCache.BaseCache.FieldLoader.AsValueField(this);
            }

            private static class AsValueField extends TypeCache.BaseCache<Object> {
                private final TypeCache.BaseCache.FieldLoader fieldLoader;

                public AsValueField(TypeCache.BaseCache.FieldLoader fieldLoader) {
                    super(fieldLoader.cacheBuilder);
                    this.fieldLoader = fieldLoader;
                }

                protected Object onLoad() throws IllegalAccessException, NoSuchFieldException {
                    Field field = this.fieldLoader.onLoad();
                    return field.get((Object)null);
                }
            }
        }

        public static class MethodLoader extends TypeCache.BaseCache<Method> {
            public MethodLoader(TypeCache.CacheBuilder builder) {
                super(builder);
            }

            protected Method onLoad() {
                Method methodThis = null;
                List<String> methods = this.cacheBuilder.methods;
                boolean hasExpectedType = this.cacheBuilder.expectType != null;
                if (methods.isEmpty() && hasExpectedType) {
                    Method[] var4 = this.BUILDER_CLASS.getDeclaredMethods();
                    int var5 = var4.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        Method method = var4[var6];
                        if (method.getReturnType() == this.cacheBuilder.expectType) {
                            return method;
                        }
                    }
                }

                Iterator var9 = this.cacheBuilder.methods.iterator();

                while(var9.hasNext()) {
                    String methodName = (String)var9.next();

                    try {
                        Method maybeGet;
                        if (!Iterables.isEmpty(this.cacheBuilder.parameterTypes)) {
                            maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName, (Class[])Iterables.get(this.cacheBuilder.parameterTypes, 0));
                        } else {
                            maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName);
                        }

                        if (this.cacheBuilder.expectType == null || this.cacheBuilder.expectType == maybeGet.getReturnType()) {
                            maybeGet.setAccessible(true);
                            methodThis = maybeGet;
                        }
                    } catch (NoSuchMethodException var8) {
                    }
                }

                return methodThis;
            }
        }

        public static class ClazzLoader extends TypeCache.BaseCache<Class<?>> {
            public ClazzLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Class<?> onLoad() {
                return this.BUILDER_CLASS;
            }
        }
    }

    public static class CacheBuilder {
        private static final String EMPTY_STRING = "";
        private final CachePackage cachePackage;
        private final CacheCategory cacheCategory;
        private final String fieldName;
        private final List<String> className;
        private final List<String> methods;
        private final String additionalData;
        private final Class<?> clazz;
        private final Iterable<Class<?>[]> parameterTypes;
        private final Class<?> expectType;

        public CacheBuilder(CachePackage cachePackage) {
            this(cachePackage, CacheCategory.DEFAULT, new ArrayList(), "", new ArrayList(), "", ImmutableList.of(), (Class)null);
        }

        protected CacheBuilder(CachePackage cachePackage, CacheCategory cacheCategory, List<String> className, String fieldName, List<String> methods, String additionalData, Iterable<Class<?>[]> parameterTypes, Class<?> expectType) {
            this.cachePackage = cachePackage;
            this.cacheCategory = cacheCategory;
            this.className = className;
            this.methods = methods;
            this.fieldName = fieldName;
            this.additionalData = additionalData;
            this.parameterTypes = parameterTypes;
            this.clazz = null;
            this.expectType = expectType;
        }

        public TypeCache.CacheBuilder withCategory(CacheCategory cacheCategory) {
            return new TypeCache.CacheBuilder(this.cachePackage, cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withClassName(String className) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, ImmutableList.<String>builder().addAll(this.className).add(this.formatClass(className)).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withClassName(Class<?> clazz) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, ImmutableList.<String >builder().addAll(this.className).add(clazz == null ? "" : clazz.getName()).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withMethodName(String methodName) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, ImmutableList.<String>builder().addAll(this.methods).add(methodName).build(), this.additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withFieldName(String fieldName) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, this.className, fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withAdditionalData(String additionalData) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, additionalData, this.parameterTypes, this.expectType);
        }

        public TypeCache.CacheBuilder withParameterTypes(Class<?>... types) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, Iterables.concat(this.parameterTypes, ImmutableList.of(types)), this.expectType);
        }

        public TypeCache.CacheBuilder withExpectResult(Class<?> expectType) {
            return new TypeCache.CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, expectType);
        }

        protected String formatClass(String className) {
            switch(this.cachePackage) {
                case MINECRAFT_SERVER:
                case CRAFT_BUKKIT:
                    return String.format((this.cachePackage == CachePackage.CRAFT_BUKKIT ? this.cachePackage.getFixedPackageName() : this.cachePackage.getForCategory(this.cacheCategory, this.additionalData)) + ".%s", className);
                case DEFAULT:
                    return className;
                default:
                    throw new IllegalArgumentException("Unexpected package " + this.cachePackage.name());
            }
        }
    }

    public static class ClassCache {
        protected static final ConcurrentMap<TypeCache.ClassCache.CacheKey, Object> CACHE = new ConcurrentHashMap();

        public ClassCache() {
        }

        public static Object find(String name, Class<?> objectClass) {
            return CACHE.get(new TypeCache.ClassCache.CacheKey(name, objectClass));
        }

        public static void register(String name, Object object, Class<?> objectClass) {
            CACHE.putIfAbsent(new TypeCache.ClassCache.CacheKey(name, objectClass), object);
        }

        private static class CacheKey {
            private final Class<?> type;
            private final String value;

            public CacheKey(String value, Class<?> type) {
                this.type = type;
                this.value = value;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (o != null && this.getClass() == o.getClass()) {
                    TypeCache.ClassCache.CacheKey classKey = (TypeCache.ClassCache.CacheKey)o;
                    return Objects.equals(this.type, classKey.type) && Objects.equals(this.value, classKey.value);
                } else {
                    return false;
                }
            }

            public int hashCode() {
                return Objects.hash(new Object[]{this.type, this.value});
            }
        }
    }
}
