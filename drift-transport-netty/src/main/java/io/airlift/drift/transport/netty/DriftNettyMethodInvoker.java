/*
 * Copyright (C) 2013 Facebook, Inc.
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
package io.airlift.drift.transport.netty;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import io.airlift.drift.transport.InvokeRequest;
import io.airlift.drift.transport.MethodInvoker;
import io.airlift.units.Duration;

import java.util.concurrent.ScheduledExecutorService;

import static com.google.common.util.concurrent.Futures.immediateFailedFuture;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static io.airlift.drift.transport.netty.InvocationResponseFuture.createInvocationResponseFuture;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

class DriftNettyMethodInvoker
        implements MethodInvoker
{
    private final ConnectionManager connectionManager;
    private final ListeningScheduledExecutorService delayService;

    public DriftNettyMethodInvoker(ConnectionManager connectionManager, ScheduledExecutorService delayService)
    {
        this.connectionManager = requireNonNull(connectionManager, "connectionManager is null");
        this.delayService = listeningDecorator(requireNonNull(delayService, "delayService is null"));
    }

    @Override
    public ListenableFuture<Object> invoke(InvokeRequest request)
    {
        try {
            return createInvocationResponseFuture(request, connectionManager);
        }
        catch (Exception e) {
            return immediateFailedFuture(e);
        }
    }

    @Override
    public ListenableFuture<?> delay(Duration duration)
    {
        return delayService.schedule(() -> null, duration.toMillis(), MILLISECONDS);
    }
}
