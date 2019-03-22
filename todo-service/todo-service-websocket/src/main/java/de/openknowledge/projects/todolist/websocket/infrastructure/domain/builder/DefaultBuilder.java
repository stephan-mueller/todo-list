/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.projects.todolist.websocket.infrastructure.domain.builder;

/**
 * A base class for builders, that does not support deep subclassing and handles creation an building of objects via reflection. Subclasses
 * may access the protected attribute {@link #instance} to construct the object.
 *
 * <p> Example for a builder: </p>
 *
 * <pre>
 * public class MyBuilder extends DefaultBuilder<MyEntity> {
 *
 *   public MyBuiler withMyAttribute(MyValue myValue) {
 *     instance.myValue = myValue;
 *     return this;
 *   }
 *
 *   protected void validate() {
 *     notNull(instance.myValue, "my value may not be null");
 *   }
 * }
 * </pre>
 *
 * @param <E> The type of the object being constructed.
 */
public abstract class DefaultBuilder<E> extends AbstractBuilder<E, DefaultBuilder<E>> {

  protected DefaultBuilder() {
    super();
  }

  protected DefaultBuilder(final E instance) {
    super(instance);
  }

  @Override
  protected DefaultBuilder<E> thisBuilder() {
    return this;
  }

  @Override
  protected E newInstance() {
    return ObjectBuilder.<E>fromGenericType(getClass(), DefaultBuilder.class).build();
  }
}
