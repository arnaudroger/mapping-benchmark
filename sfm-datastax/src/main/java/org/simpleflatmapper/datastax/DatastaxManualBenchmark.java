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

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.sfm.datastax.DatastaxMapper;
import org.sfm.datastax.DatastaxMapperFactory;
import org.simpleflatmapper.beans.Object16Fields;
import org.simpleflatmapper.beans.Object4Fields;
import org.simpleflatmapper.param.LimitParam;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

/*
Benchmark                          Mode  Cnt   Score   Error  Units
DatastaxMapperBenchmark.datastaxMapper  thrpt   20  22.120 ± 0.233  ops/s
DatastaxMapperBenchmark.sfmMapper       thrpt   20  29.294 ± 0.438  ops/s
Benchmark                         Mode  Cnt  Score    Error  Units
DatastaxMapperBenchmark.datastaxMapper  avgt   20  0.045 ±  0.001   s/op
DatastaxMapperBenchmark.sfmMapper       avgt   20  0.034 ±  0.001   s/op

 */
@State(Scope.Benchmark)
public class DatastaxManualBenchmark {

    DatastaxHelper datastaxHelper;

    @Setup
    public void setUp() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        datastaxHelper = new DatastaxHelper();
    }

    @TearDown
    public void tearDown() {
        datastaxHelper.close();
    }

    @Benchmark
    public void _04Fields(LimitParam limit, Blackhole blackhole) {
        for(Row result : datastaxHelper.select4Fields(limit.limit)) {
            blackhole.consume(map4(result));
        }
    }

    @Benchmark
    public void _16Fields(LimitParam limit, Blackhole blackhole) {
        for(Row result : datastaxHelper.select16Fields(limit.limit)) {
            blackhole.consume(map16(result));
        }
    }

    private Object4Fields map4(Row r) {
        Object4Fields object4Fields = new Object4Fields();
        object4Fields.setId(r.getLong(0));
        object4Fields.setYearStarted(r.getInt(1));
        object4Fields.setName(r.getString(2));
        object4Fields.setEmail(r.getString(3));
        return object4Fields;
    }

    private Object16Fields map16(Row r) {
        Object16Fields o = new Object16Fields();

        o.setId(r.getLong(0));
        o.setYearStarted(r.getInt(1));
        o.setName(r.getString(2));
        o.setEmail(r.getString(3));

        o.setField5(r.getInt(4));
        o.setField6(r.getInt(5));
        o.setField7(r.getLong(6));
        o.setField8(r.getFloat(7));
        o.setField9(r.getDouble(8));

        o.setField10(r.getInt(9));
        o.setField11(r.getInt(10));
        o.setField12(r.getLong(11));
        o.setField13(r.getFloat(12));
        o.setField14(r.getDouble(13));

        o.setField15(r.getInt(14));
        o.setField16(r.getInt(15));


        return o;
    }
}
