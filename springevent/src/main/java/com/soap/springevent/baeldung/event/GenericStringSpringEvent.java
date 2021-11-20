package com.soap.springevent.baeldung.event;

/**
 * type erasure로 인해 필터링할 제네릭 매개변수를 해결하는 클래스 작성
 */
public class GenericStringSpringEvent extends GenericSpringEvent<String> {

    GenericStringSpringEvent(final String what, final boolean success) {
        super(what, success);
    }

}