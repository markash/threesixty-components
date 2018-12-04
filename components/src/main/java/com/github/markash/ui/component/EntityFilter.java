package com.github.markash.ui.component;

import org.springframework.data.domain.Example;

import java.io.Serializable;

/**
 * Entity filter that uses the Spring Query By Example (QBE)
 * @param <E> The entity domain class
 */
public interface EntityFilter<E> extends Serializable {
    Example<E> getFilter();
}
