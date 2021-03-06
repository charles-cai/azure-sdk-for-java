/**
 * Copyright 2011 Microsoft Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.microsoft.windowsazure.services.blob.client;

/**
 * Specifies the level of public access that is allowed on the container.
 * 
 */
public enum BlobContainerPublicAccessType {
    /**
     * Specifies blob-level public access. Clients can read the content and metadata of blobs within this container, but
     * cannot read container metadata or list the blobs within the container.
     */
    BLOB,

    /**
     * Specifies container-level public access. Clients can read blob content and metadata and container metadata, and
     * can list the blobs within the container.
     **/
    CONTAINER,

    /**
     * Specifies no public access. Only the account owner can access resources in this container.
     */
    OFF
}
