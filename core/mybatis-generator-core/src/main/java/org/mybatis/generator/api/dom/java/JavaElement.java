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
package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class JavaElement {

    private List<String> javaDocLines = new ArrayList<>();

    private JavaVisibility visibility = JavaVisibility.DEFAULT;

    private boolean isStatic;

    private boolean isFinal;

    private List<String> annotations = new ArrayList<>();

    public JavaElement() {
        super();
    }

    public JavaElement(JavaElement original) {
        this.annotations.addAll(original.annotations);
        this.isStatic = original.isStatic;
        this.isFinal = original.isFinal;
        this.javaDocLines.addAll(original.javaDocLines);
        this.visibility = original.visibility;
    }


    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    public JavaVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(JavaVisibility visibility) {
        this.visibility = visibility;
    }

    public void addSuppressTypeWarningsAnnotation() {
        addAnnotation("@SuppressWarnings(\"unchecked\")"); //$NON-NLS-1$
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public void addFormattedJavadoc(StringBuilder sb, int indentLevel) {
        Iterator var3 = this.javaDocLines.iterator();

        while(var3.hasNext()) {
            String javaDocLine = (String)var3.next();
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtilities.newLine(sb);
        }

    }

    public void addFormattedAnnotations(StringBuilder sb, int indentLevel) {
        Iterator var3 = this.annotations.iterator();

        while(var3.hasNext()) {
            String annotation = (String)var3.next();
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(annotation);
            OutputUtilities.newLine(sb);
        }

    }
}
