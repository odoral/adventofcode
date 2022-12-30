package org.odoral.adventofcode.common.model;

import java.util.Objects;
import java.util.Optional;

public class Chain<T> {

    protected T value;
    protected Chain<T> next;
    protected Chain<T> previous;

    public Chain(T value) {
        this.value = value;
    }

    public void insertBefore(Chain<T> chain) {
        chain.previous = this.previous;
        chain.next = this;

        this.previous.next = chain;
        this.previous = chain;
    }
    
    public void insertAfter(Chain<T> chain) {
        next.insertBefore(chain);
    }

    public Chain<T> remove() {
        this.previous.next = this.next;
        this.next.previous = this.previous;
        this.previous = null;
        this.next = null;
        return this;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Chain<T> getNext() {
        return next;
    }

    public void setNext(Chain<T> next) {
        this.next = next;
    }

    public Chain<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Chain<T> previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return Optional.ofNullable(value).map(Objects::toString).orElse("N/A");
    }

    public static class Builder<E> {

        private Chain<E> firstChain;
        private Chain<E> lastChain;

        public Builder() {
        }

        public Builder addChain(Chain<E> chain) {
            if (lastChain == null) {
                this.firstChain = chain;
            } else {
                lastChain.next = chain;
                chain.previous = lastChain;
            }
            lastChain = chain;

            return this;
        }

        public Chain<E>[] build() {
            return new Chain[]{firstChain, lastChain};
        }
    }

}
