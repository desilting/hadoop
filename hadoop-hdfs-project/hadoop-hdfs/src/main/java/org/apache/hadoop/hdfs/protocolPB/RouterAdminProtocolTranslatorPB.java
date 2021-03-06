/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.protocolPB;

import java.io.Closeable;
import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.AddMountTableEntryRequestProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.AddMountTableEntryResponseProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.GetMountTableEntriesRequestProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.GetMountTableEntriesResponseProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.RemoveMountTableEntryRequestProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.RemoveMountTableEntryResponseProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.UpdateMountTableEntryRequestProto;
import org.apache.hadoop.hdfs.federation.protocol.proto.HdfsServerFederationProtos.UpdateMountTableEntryResponseProto;
import org.apache.hadoop.hdfs.server.federation.resolver.MountTableManager;
import org.apache.hadoop.hdfs.server.federation.store.protocol.AddMountTableEntryRequest;
import org.apache.hadoop.hdfs.server.federation.store.protocol.AddMountTableEntryResponse;
import org.apache.hadoop.hdfs.server.federation.store.protocol.GetMountTableEntriesRequest;
import org.apache.hadoop.hdfs.server.federation.store.protocol.GetMountTableEntriesResponse;
import org.apache.hadoop.hdfs.server.federation.store.protocol.RemoveMountTableEntryRequest;
import org.apache.hadoop.hdfs.server.federation.store.protocol.RemoveMountTableEntryResponse;
import org.apache.hadoop.hdfs.server.federation.store.protocol.UpdateMountTableEntryRequest;
import org.apache.hadoop.hdfs.server.federation.store.protocol.UpdateMountTableEntryResponse;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.AddMountTableEntryRequestPBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.AddMountTableEntryResponsePBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.GetMountTableEntriesRequestPBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.GetMountTableEntriesResponsePBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.RemoveMountTableEntryRequestPBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.RemoveMountTableEntryResponsePBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.UpdateMountTableEntryRequestPBImpl;
import org.apache.hadoop.hdfs.server.federation.store.protocol.impl.pb.UpdateMountTableEntryResponsePBImpl;
import org.apache.hadoop.ipc.ProtobufHelper;
import org.apache.hadoop.ipc.ProtocolMetaInterface;
import org.apache.hadoop.ipc.ProtocolTranslator;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RpcClientUtil;

import com.google.protobuf.ServiceException;

/**
 * This class forwards NN's ClientProtocol calls as RPC calls to the NN server
 * while translating from the parameter types used in ClientProtocol to the
 * new PB types.
 */
@InterfaceAudience.Private
@InterfaceStability.Stable
public class RouterAdminProtocolTranslatorPB
    implements ProtocolMetaInterface, MountTableManager,
    Closeable, ProtocolTranslator {
  final private RouterAdminProtocolPB rpcProxy;

  public RouterAdminProtocolTranslatorPB(RouterAdminProtocolPB proxy) {
    rpcProxy = proxy;
  }

  @Override
  public void close() {
    RPC.stopProxy(rpcProxy);
  }

  @Override
  public Object getUnderlyingProxyObject() {
    return rpcProxy;
  }

  @Override
  public boolean isMethodSupported(String methodName) throws IOException {
    return RpcClientUtil.isMethodSupported(rpcProxy,
        RouterAdminProtocolPB.class, RPC.RpcKind.RPC_PROTOCOL_BUFFER,
        RPC.getProtocolVersion(RouterAdminProtocolPB.class), methodName);
  }

  @Override
  public AddMountTableEntryResponse addMountTableEntry(
      AddMountTableEntryRequest request) throws IOException {
    AddMountTableEntryRequestPBImpl requestPB =
        (AddMountTableEntryRequestPBImpl)request;
    AddMountTableEntryRequestProto proto = requestPB.getProto();
    try {
      AddMountTableEntryResponseProto response =
          rpcProxy.addMountTableEntry(null, proto);
      return new AddMountTableEntryResponsePBImpl(response);
    } catch (ServiceException e) {
      throw new IOException(ProtobufHelper.getRemoteException(e).getMessage());
    }
  }

  @Override
  public UpdateMountTableEntryResponse updateMountTableEntry(
      UpdateMountTableEntryRequest request) throws IOException {
    UpdateMountTableEntryRequestPBImpl requestPB =
        (UpdateMountTableEntryRequestPBImpl)request;
    UpdateMountTableEntryRequestProto proto = requestPB.getProto();
    try {
      UpdateMountTableEntryResponseProto response =
          rpcProxy.updateMountTableEntry(null, proto);
      return new UpdateMountTableEntryResponsePBImpl(response);
    } catch (ServiceException e) {
      throw new IOException(ProtobufHelper.getRemoteException(e).getMessage());
    }
  }

  @Override
  public RemoveMountTableEntryResponse removeMountTableEntry(
      RemoveMountTableEntryRequest request) throws IOException {
    RemoveMountTableEntryRequestPBImpl requestPB =
        (RemoveMountTableEntryRequestPBImpl)request;
    RemoveMountTableEntryRequestProto proto = requestPB.getProto();
    try {
      RemoveMountTableEntryResponseProto responseProto =
          rpcProxy.removeMountTableEntry(null, proto);
      return new RemoveMountTableEntryResponsePBImpl(responseProto);
    } catch (ServiceException e) {
      throw new IOException(ProtobufHelper.getRemoteException(e).getMessage());
    }
  }

  @Override
  public GetMountTableEntriesResponse getMountTableEntries(
      GetMountTableEntriesRequest request) throws IOException {
    GetMountTableEntriesRequestPBImpl requestPB =
        (GetMountTableEntriesRequestPBImpl)request;
    GetMountTableEntriesRequestProto proto = requestPB.getProto();
    try {
      GetMountTableEntriesResponseProto response =
          rpcProxy.getMountTableEntries(null, proto);
      return new GetMountTableEntriesResponsePBImpl(response);
    } catch (ServiceException e) {
      throw new IOException(ProtobufHelper.getRemoteException(e).getMessage());
    }
  }
}
