/**
 *    Copyright 2006-2019 the original author or authors.
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

import org.apache.tools.ant.util.CollectionUtils;
import org.mybatis.generator.api.dom.OutputUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yttiany
 * @author yttiany
 */
public class XmlElement  extends Element implements VisitableElement
{
    private List<Attribute> attributes;
    private List<Element> oldElements;
    private List<VisitableElement> elements = new ArrayList<>();
    private String name;

    public XmlElement(String name)
    {
        this.attributes = new ArrayList();
        this.elements = new ArrayList();
        this.name = name;
    }

    public XmlElement(XmlElement original)
    {
        this.attributes = new ArrayList();
        this.attributes.addAll(original.attributes);
        this.oldElements = new ArrayList();
        if( original.oldElements!=null ){
            this.oldElements.addAll(original.oldElements);
        }
        this.elements = new ArrayList();
        this.elements.addAll(original.elements);
        this.name = original.name;
    }

    public List<Attribute> getAttributes()
    {
        return this.attributes;
    }

    public void addAttribute(Attribute attribute)
    {
        this.attributes.add(attribute);
    }


    public void addOldElement(Element oldElements)
    {
        this.oldElements.add(oldElements);
    }

    public void addOldElement(int index, Element oldElements)
    {
        this.oldElements.add(index, oldElements);
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Element> getOldElements() {
        return oldElements;
    }

    public void setOldElements(List<Element> oldElements) {
        this.oldElements = oldElements;
    }

    public List<VisitableElement> getElements() {
        return elements;
    }

    public void setElements(List<VisitableElement> elements) {
        this.elements = elements;
    }

    public void addElement(VisitableElement element) {
        elements.add(element);
    }

    public void addVElement(int index, VisitableElement element) {
        elements.add(index, element);
    }

    public String getName()
    {
        return this.name;
    }

    @Override
    public String getFormattedContent(int indentLevel)
    {
        StringBuilder sb = new StringBuilder();

        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(this.name);

        Collections.sort(this.attributes);
        for (Attribute att : this.attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }

        if (this.elements.size() > 0) {
            sb.append(">");
            for (Element element : this.oldElements) {
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</");
            sb.append(this.name);
            sb.append('>');
        }
        else {
            sb.append(" />");
        }

        return sb.toString();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public <R> R accept(ElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public boolean hasChildren() {
        return !elements.isEmpty();
    }
}
