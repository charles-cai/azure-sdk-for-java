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

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.microsoft.windowsazure.services.core.storage.Constants;
import com.microsoft.windowsazure.services.core.storage.StorageErrorCodeStrings;
import com.microsoft.windowsazure.services.core.storage.StorageException;
import com.microsoft.windowsazure.services.core.storage.utils.Utility;

/**
 * RESERVED FOR INTERNAL USE. A class used to parse a get page ranges response stream.
 */
final class GetPageRangesResponse {
    /**
     * Holds the ArrayList of Page Ranges from the response.
     */
    private ArrayList<PageRange> pageRanges = new ArrayList<PageRange>();

    /**
     * Stores the value indicating if the response has been fully parsed.
     */
    private boolean isParsed;

    /**
     * Stores the InputStream to read from.
     */
    private final InputStream streamRef;

    /**
     * Constructs the response from the inputstream.
     * 
     * @param stream
     *            the inputstream for the response the server returned.
     */
    public GetPageRangesResponse(final InputStream stream) {
        this.streamRef = stream;
    }

    /**
     * Returns an ArrayList of Page Ranges for the given page blob.
     * 
     * @return an ArrayList of Page Ranges for the given page blob.
     * @throws XMLStreamException
     * @throws StorageException
     */
    public ArrayList<PageRange> getPageRanges() throws XMLStreamException, StorageException {
        if (!this.isParsed) {
            this.parseResponse();
        }

        return this.pageRanges;
    }

    /**
     * Parses the XML stream.
     * 
     * @throws XMLStreamException
     * @throws StorageException
     */
    public void parseResponse() throws XMLStreamException, StorageException {
        final XMLStreamReader xmlr = Utility.createXMLStreamReaderFromStream(this.streamRef);

        // Start document
        int eventType = xmlr.getEventType();
        xmlr.require(XMLStreamConstants.START_DOCUMENT, null, null);

        // 1. get BlockList Header
        eventType = xmlr.next();
        xmlr.require(XMLStreamConstants.START_ELEMENT, null, BlobConstants.PAGE_LIST_ELEMENT);

        // check if there are more events in the input stream
        while (xmlr.hasNext()) {
            eventType = xmlr.next();
            if (eventType == XMLStreamConstants.START_ELEMENT || eventType == XMLStreamConstants.END_ELEMENT) {
                final String name = xmlr.getName().toString();

                if (name.equals(BlobConstants.PAGE_RANGE_ELEMENT)) {
                    this.pageRanges = BlobDeserializationHelper.readPageRanges(xmlr);
                }
                else if (name.equals(BlobConstants.PAGE_LIST_ELEMENT) && eventType == XMLStreamConstants.END_ELEMENT) {
                    break;
                }
                else {
                    throw new StorageException(StorageErrorCodeStrings.INVALID_XML_DOCUMENT,
                            "The response recieved is invalid or improperly formatted.",
                            Constants.HeaderConstants.HTTP_UNUSED_306, null, null);
                }
            }
        }

        this.isParsed = true;
    }
}
