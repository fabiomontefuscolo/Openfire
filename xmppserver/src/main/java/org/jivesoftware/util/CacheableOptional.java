package org.jivesoftware.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Some times it is desirable to store in a {@link org.jivesoftware.util.cache.Cache} the absence of a value.
 * Unfortunately, it's not possible to store a {@code null} value in a clustered cache because of the underlying hazelcast
 * technology used for clustering. The obvious candidate would therefore be to store an {@link Optional} object in the
 * cache instead - unfortunately an Optional is not serializable. This class therefore performs this functionality - an
 * optional value that is cacheable.
 */
public class CacheableOptional<T extends Serializable> implements Serializable {

    private final T value;

    private CacheableOptional(T value) {
        this.value = value;
    }

    public static <T extends Serializable> CacheableOptional<T> of(final T value) {
        return new CacheableOptional<>(value);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T extends Serializable> CacheableOptional<T> from(final Optional<T> value) {
        return new CacheableOptional<>(value.orElse(null));
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isAbsent() {
        return value == null;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CacheableOptional<?> that = (CacheableOptional<?>) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
