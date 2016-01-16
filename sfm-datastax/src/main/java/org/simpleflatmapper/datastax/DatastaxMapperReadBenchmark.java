/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.simpleflatmapper.datastax;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.beans.Object16Fields;
import org.simpleflatmapper.beans.Object4Fields;
import org.simpleflatmapper.param.LimitParam;

import java.io.IOException;

/*

 */
@State(Scope.Benchmark)
public class DatastaxMapperReadBenchmark {


    Mapper<Object4Fields> mapper4;
    Mapper<Object16Fields> mapper16;

    DatastaxHelper datastaxHelper;

    @Setup
    public void setUp() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        datastaxHelper = new DatastaxHelper();
        MappingManager mappingManager = new MappingManager(datastaxHelper.session);
        mapper4 = mappingManager.mapper(Object4Fields.class);
        mapper16 = mappingManager.mapper(Object16Fields.class);
    }

    @TearDown
    public void tearDown() {
        datastaxHelper.close();
    }


    @Benchmark
    public Object4Fields _read04Fields() {
        return mapper4.get(0l);
    }

    @Benchmark
    public Object16Fields _read16Fields() {
        return mapper16.get(0l);
    }

    public static void main(String[] args) throws InterruptedException, IOException, TTransportException {
        DatastaxMapperReadBenchmark b = new DatastaxMapperReadBenchmark();
        b.setUp();

        System.out.println("4 " + b._read04Fields());
        System.out.println("16 " + b._read16Fields());
    }
}
