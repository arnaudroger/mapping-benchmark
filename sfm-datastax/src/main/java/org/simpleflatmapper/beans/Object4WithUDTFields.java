package org.simpleflatmapper.beans;

import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "test", name = "test_table_address")
public class Object4WithUDTFields extends Object4Fields {
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
