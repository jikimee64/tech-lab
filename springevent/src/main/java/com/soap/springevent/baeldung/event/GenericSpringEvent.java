package com.soap.springevent.baeldung.event;

/**
 *  ApplicationEvent 에서 확장할 필요가 없습니다.
 * @param <T>
 */
public class GenericSpringEvent<T> {

    private final T what;
    protected final boolean success;

    public GenericSpringEvent(final T what, final boolean success) {
        this.what = what;
        this.success = success;
    }

    public T getWhat() {
        return what;
    }

    public boolean isSuccess() {
        return success;
    }

}