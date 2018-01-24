package com.github.markash.ui.view;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;

import java.io.Serializable;

public class EntityFormService<T, I extends Serializable> {
    private EntitySupplier<T, I> entitySupplier;
    private BlankSupplier<T> blankSupplier;
    private EntityPersistFunction<T> persistConsumer;
    private EntityPersistFunction<T> deleteConsumer;

    public EntityFormService(EntitySupplier<T, I> entitySupplier, BlankSupplier<T> blankSupplier, EntityPersistFunction<T> persistConsumer, EntityPersistFunction<T> deleteConsumer) {
        this.entitySupplier = entitySupplier;
        this.blankSupplier = blankSupplier;
        this.persistConsumer = persistConsumer;
        this.deleteConsumer = deleteConsumer;
    }
}
