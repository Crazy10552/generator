/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.util.Optional;

/**
 * @author yttiany
 * @author yttiany
 */
public class Document {
    private String publicId;
    private String systemId;
    private DocType docType;
    private XmlElement rootElement;

    public Document(String publicId, String systemId)
    {
        this.publicId = publicId;
        this.systemId = systemId;
        docType = new PublicDocType(publicId, systemId);
    }

    public Document(String systemId) {
        docType = new SystemDocType(systemId);
    }
    public Document()
    {
        super();
    }

    public XmlElement getRootElement()
    {
        return this.rootElement;
    }

    public void setRootElement(XmlElement rootElement)
    {
        this.rootElement = rootElement;
    }

    public String getPublicId()
    {
        return this.publicId;
    }

    public String getSystemId()
    {
        return this.systemId;
    }

    public Optional<DocType> getDocType() {
        return Optional.ofNullable(docType);
    }
}
