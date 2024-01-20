package kreadcn.homework.model;

import lombok.Data;

@Data
public class NameValuePair<K, V> {
    K name;
    V value;

    public NameValuePair(K k, V v) {
        this.name = k;
        this.value = v;
    }
}
