package com.ebay.oss.bark.service;

public interface Converter<E, V> {

    V voOf(E e);
    
    E entityOf(V vo);

}
