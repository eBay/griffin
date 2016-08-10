package org.apache.bark.service;

public interface Converter<E, V> {

    V voOf(E e);
    
    E entityOf(V vo);

}
