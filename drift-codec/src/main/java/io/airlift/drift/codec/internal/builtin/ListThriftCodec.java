/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.airlift.drift.codec.internal.builtin;

import com.google.common.base.Preconditions;
import io.airlift.drift.codec.ThriftCodec;
import io.airlift.drift.codec.internal.TProtocolReader;
import io.airlift.drift.codec.internal.TProtocolWriter;
import io.airlift.drift.codec.metadata.ThriftType;
import org.apache.thrift.protocol.TProtocol;

import javax.annotation.concurrent.Immutable;

import java.util.List;

@Immutable
public class ListThriftCodec<T>
        implements ThriftCodec<List<T>>
{
    private final ThriftCodec<T> elementCodec;
    private final ThriftType type;

    public ListThriftCodec(ThriftType type, ThriftCodec<T> elementCodec)
    {
        Preconditions.checkNotNull(type, "type is null");
        Preconditions.checkNotNull(elementCodec, "elementCodec is null");

        this.type = type;
        this.elementCodec = elementCodec;
    }

    @Override
    public ThriftType getType()
    {
        return type;
    }

    @Override
    public List<T> read(TProtocol protocol)
            throws Exception
    {
        Preconditions.checkNotNull(protocol, "protocol is null");
        return new TProtocolReader(protocol).readList(elementCodec);
    }

    @Override
    public void write(List<T> value, TProtocol protocol)
            throws Exception
    {
        Preconditions.checkNotNull(value, "value is null");
        Preconditions.checkNotNull(protocol, "protocol is null");
        new TProtocolWriter(protocol).writeList(elementCodec, value);
    }
}